package cn.gsq.graph.dag;

import java.util.Map;
import java.util.UUID;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.dag.DagRelationship
 *
 * @author : gsq
 * @date : 2023-03-28 11:33
 * @note : It's not technology, it's art !
 **/
public interface DagRelationship extends Relationship {

    void setVertexInDag(Map<UUID, Vertex<?>> m);

}
