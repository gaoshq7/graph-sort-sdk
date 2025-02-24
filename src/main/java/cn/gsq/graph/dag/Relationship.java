package cn.gsq.graph.dag;

import java.util.UUID;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.dag.Relationship
 *
 * @author : gsq
 * @date : 2023-03-28 12:16
 * @note : It's not technology, it's art !
 **/
public interface Relationship {

    void addParent(UUID id);

    void addChild(UUID id);

    void setId(UUID id);
}
