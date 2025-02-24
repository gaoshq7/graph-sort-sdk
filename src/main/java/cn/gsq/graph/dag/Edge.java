package cn.gsq.graph.dag;

import java.util.UUID;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.dag.Edge
 *
 * @author : gsq
 * @date : 2023-03-28 11:56
 * @note : It's not technology, it's art !
 **/
public final class Edge extends Element{

    private UUID fromV;
    private UUID toV;
    public UUID getFromV() {
        return fromV;
    }

    public UUID getToV() {
        return toV;
    }

    public Edge (UUID fromV, UUID toV) {
        super();
        this.fromV = fromV;
        this.toV = toV;
    }
    /**
     * Test if e is the same edge.
     * hasHash equals is not recommended due to object creation is not managed.
     * @param e
     * @return
     */
    public boolean isSameEdge(Edge e) {
        return this.fromV == e.getFromV()
                && this.toV == e.getToV();
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Edge) {
            return isSameEdge((Edge)o);
        } else {
            return false;
        }
    }
}
