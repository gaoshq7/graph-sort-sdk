package cn.gsq.graph.task;

import cn.gsq.graph.actor.Actor;
import cn.gsq.graph.actor.Pid;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.task.Task
 *
 * @author : gsq
 * @date : 2023-03-28 13:29
 * @note : It's not technology, it's art !
 **/
public class Task implements Actor, Callable<Boolean> {

    CountDownLatch parentLatch;

    @SuppressWarnings("rawtypes")
    Set<Task> containedTasks = new HashSet<>();

    Pid pid = Pid.getInstance();
    VertexTaskConfig config;
    boolean isDag = false;
    private long runtimeTO = -1;

    public Task (VertexTaskConfig config) {
       this.isDag = config instanceof DagTaskConfig;
       this.config = config;
       parentLatch = new CountDownLatch(config.parents.size());
       pid.register(this);
    }

    public void addTaskVertices(Set<Task> tasks) {
       containedTasks.addAll(tasks);
    }

    public void setTimeout(long timeout) {
       runtimeTO = timeout;
    }
    @Override
    public Boolean call() throws Exception {
       long allocTimeout = runtimeTO > 0 ? Math.min(this.config.getTimeout(), runtimeTO)
    		   : this.config.getTimeout();
       long start = Util.getNowInSeconds();
       waitForParents(allocTimeout);

       long newTimeout = Util.getRemainingTimeout(allocTimeout, start);
       if (newTimeout < 1) return new Boolean(false);

       if(isDag) dag(newTimeout); else runTask(newTimeout);
       this.clearDependencies();
       return true;
    }

    private void waitForParents(long timeout) {
       try {
           parentLatch.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
    }

    private void runTask(long timeout) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future f = executor.submit((Callable) config.getTask());
        try {
            f.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {

            return;
        }
    }

    private void dag(long timeout) {
        long start = Util.getNowInSeconds();
        System.out.println("thread pool size ~ " + containedTasks.size());
        int threadPoolSize = containedTasks.size();
        if (threadPoolSize < 1) {
            return;
        }

        Set<Future<Boolean>> futures = new HashSet<>();
        CompletionService<Boolean> ecs = 
               new ExecutorCompletionService<>(Executors.newFixedThreadPool(threadPoolSize));
        containedTasks.forEach(t -> {
           futures.add(ecs.submit(t));
        });

        int completed = 0;
        while (completed < threadPoolSize) {
        	
            if (Util.isTimedOut(start, timeout)) break;
            //Optional<Future<Boolean>> o = Optional.of(ecs.poll());
            //TODO: handle failure case with time out
            Future<Boolean> r = ecs.poll();
            completed++;

            Util.sleepMilliseconds(10);
        }
    }

    private void clearDependencies() {
       this.config.children.parallelStream().forEach(e -> 
           this.send((UUID)e, new CompletionMessage(getPid()))
       );
    }
    @Override
    public UUID getPid() {
        return this.config.getPid();
    }

    @Override
    public void receive(UUID fromId, Object message) {
        if (message instanceof CompletionMessage) {
            CompletionMessage msg = (CompletionMessage)message;
            if (config.removeParent(msg.getFromUUID())) {
                parentLatch.countDown();
            }
        }
    }

    @Override
    public long getTimeoutMilliSeconds() {
       return this.config.getTimeout();
    }

}
