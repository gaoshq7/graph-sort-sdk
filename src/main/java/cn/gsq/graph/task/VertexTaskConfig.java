package cn.gsq.graph.task;

import cn.gsq.graph.dag.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.task.VertexTaskConfig
 *
 * @author : gsq
 * @date : 2023-03-28 13:59
 * @note : It's not technology, it's art !
 **/
public class VertexTaskConfig<Callable> implements Relationship {

    final static int DEFAULT_ONE_MINUTE = 60;
    Set<UUID> parents = new HashSet<UUID>();
    Set<UUID> children = new HashSet<UUID>();
    boolean isDag = false;

    Callable callable;

    UUID id;
    //timeout must be enforced
    private long timeout = DEFAULT_ONE_MINUTE;

    public VertexTaskConfig (Callable t){
         callable = t;
    }
    public VertexTaskConfig (){
    }
    public Callable getTask() {
        return this.callable;
    }
    public void setTimeout(long to) {
        this.timeout = to;
    }
    public long getTimeout() {
        return this.timeout;
    }
    public UUID getPid() {
       return this.id;
    }
    public boolean removeParent(UUID id) {
       return parents.remove(id);
    }

    @Override
    public void addParent(UUID id) {
        parents.add(id);
    }

    @Override
    public void addChild(UUID id) {
        children.add(id);
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }
}
