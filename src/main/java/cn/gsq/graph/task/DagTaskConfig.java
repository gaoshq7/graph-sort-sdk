package cn.gsq.graph.task;

import cn.gsq.graph.dag.DagRelationship;
import cn.gsq.graph.dag.Vertex;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.task.DagTaskConfig
 *
 * @author : gsq
 * @date : 2023-03-28 13:11
 * @note : It's not technology, it's art !
 **/
public class DagTaskConfig extends VertexTaskConfig implements DagRelationship {

    @SuppressWarnings("rawtypes")
    Set<Task> containedTasks = new HashSet<>();

    public DagTaskConfig () {}

    @Override
    public void setVertexInDag(Map<UUID, Vertex<?>> m) {
        m.values().forEach( v -> {
            Object o = v.getTask();
            if (o instanceof Task) {
                containedTasks.add((Task) o);
            }
        });
    }

    public Set<Task> getContainedTasks() {
        return containedTasks;
    }

}
