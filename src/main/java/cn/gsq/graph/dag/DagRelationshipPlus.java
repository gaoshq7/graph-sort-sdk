package cn.gsq.graph.dag;

import java.util.Map;
import java.util.UUID;

public interface DagRelationshipPlus<T> extends Relationship{
    void setVertexInDag(Map<UUID, Vertex<T>> m);
}
