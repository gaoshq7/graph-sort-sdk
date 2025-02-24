package cn.gsq.graph.task;

import cn.gsq.graph.dag.Dag;

import java.util.HashSet;
import java.util.Set;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.task.TaskFactory
 *
 * @author : gsq
 * @date : 2023-03-28 13:59
 * @note : It's not technology, it's art !
 **/
public class TaskFactory {

    static public Task build(Dag<DagTaskConfig> dag) {
        Set<Task> tasks = new HashSet<>();
        dag.applyRelationship();
        dag.getVertices().parallelStream().forEach( v -> {
            tasks.add(new Task((VertexTaskConfig)v.getTask()));
        });
        Task dagTask = new Task((DagTaskConfig)dag.getTask());
        dagTask.addTaskVertices(tasks);
        return dagTask;
    }

}
