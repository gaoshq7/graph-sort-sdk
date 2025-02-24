package cn.gsq.graph.dag;

import java.util.UUID;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.dag.Element
 *
 * @author : gsq
 * @date : 2023-03-28 12:01
 * @note : It's not technology, it's art !
 **/
abstract public class Element {

    private UUID id = UUID.randomUUID();

    public UUID getId() {
        return this.id;
    }

    /**
     * for soft clone use only
     * @param uuid
     */
    void setUUID(UUID uuid) {
        this.id = uuid;
    }
}
