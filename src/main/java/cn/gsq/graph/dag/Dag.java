package cn.gsq.graph.dag;

import cn.gsq.graph.exception.DagCycleException;
import cn.gsq.graph.exception.DuplicatedEdgeException;
import cn.gsq.graph.exception.NonexistentVertexException;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * Project : galaxy
 * Class : cn.gsq.graph.dag.Dag
 *
 * @author : gsq
 * @date : 2023-03-28 11:30
 * @note : It's not technology, it's art !
 **/
public class Dag<T> extends Vertex {

    //keep track all contained vertices
    private Map<UUID, Vertex<?>> vertices = new HashMap<>();

    public Dag(T t) {
        super(t);
    }

    /**
     * Put an edge between two vertices.
     * @param fromV
     * @param toV
     * @throws DagCycleException
     * @throws DuplicatedEdgeException 
     */
    public void putEdge(Vertex<?> fromV, Vertex<?> toV)
            throws DagCycleException, DuplicatedEdgeException {
        testSelfLoop(fromV, toV);

        Edge e = new Edge(fromV.getId(), toV.getId());
        fromV.addOutDegree(e);
        toV.addInDegree(e);
        boolean fromVNew = addVertex(fromV);
        boolean toVNew = addVertex(toV);

        if (!isDag()) {
            //roll back changes if the new vertices and edge introduce a loop.
            fromV.removeOutDegree(e);
            toV.removeInDegree(e);
            if (fromVNew) vertices.remove(fromV.getId());
            if (toVNew)   vertices.remove(toV.getId());
            throw new DagCycleException("DAG图中不允许存在循环死锁。");
        }
    }

    public void removeEdge(Vertex<?> fromV, Vertex<?> toV) throws NonexistentVertexException {
        testVertexExist(fromV);
        testVertexExist(toV);

        Edge e = new Edge(fromV.getId(), toV.getId());
        fromV.removeOutDegree(e);
        toV.removeInDegree(e);
    }

    private void testVertexExist(Vertex v) throws NonexistentVertexException {
        if (!vertices.containsKey(v.getId()))
            throw new NonexistentVertexException("Nonexistent vertext " + v.getId());
    }

    private void testSelfLoop(Vertex<?> fromV, Vertex<?> toV)
            throws DagCycleException {
        if (fromV == toV)
            throw new DagCycleException("DAG图中不允许有自循环死锁。");
    }

    /**
     * 
     * @param v
     * @return true if it's a new vertex.
     */
    public boolean addVertex(Vertex<?> v) {
        if (!vertices.containsKey(v.getId())) {
            vertices.put(v.getId(), v);
            return true;
        }
        return false;
    }

    /**
     * @return the vertices
     */
    public Collection<Vertex<?>> getVertices() {
       return vertices.values();
    }

    /**
     * @Description : 克隆一个完整的图
     * @Param : []
     * @Return : java.util.Map<java.util.UUID,cn.gsq.graph.dag.Vertex<?>>
     * @Author : gsq
     * @Date : 3:14 下午
     * @note : An art cell !
    **/
    public Map<UUID, Vertex<?>> getVerticesToMap() {
        return this.vertices;
    }

    /**
     * @param vertex the vertices to set
     */
    public void addVertices(Vertex<?> vertex) {
        this.vertices.put(vertex.getId(), vertex);
    }

    /**
     * @param vertices the vertices to set
     */
    public void removeVertices(Vertex<?> vertices) {
        this.vertices.remove(vertices);
    }

    /**
     * verify the di-graph is a dag
     * @return
     */
    public boolean isDag() {
        //soft clone all vertices in the dag just for topological sorting
        Map<UUID, Vertex<?>> map = this.vertices.entrySet().parallelStream().
                collect(Collectors.toMap( p -> p.getKey(), p -> p.getValue().softClone()));
        Queue<Vertex<?>> s = topologicalSorting(map,
                 new LinkedBlockingQueue<>());
        return !s.isEmpty();
    }

    /**
     * topological sort algorithm
     * @return
     */
    public Queue<Vertex<?>> topologicalSorting(Map<UUID, Vertex<?>> nodes, Queue<Vertex<?>> sorted) {
        //nodes -> all remaining vertices in the graph to be sorted
        Queue<Vertex<?>> s = new LinkedBlockingQueue<>(getRootVertices(nodes.values()));
        if (s.isEmpty()) {
            //cycle detected when nodes are not empty() so return empty sets since only to sort a dag
            //this is an error condition
            return nodes.isEmpty() ? sorted : new LinkedBlockingQueue<>();
        }

        s.parallelStream().forEach( n -> {
             n.getOutDegree().values().stream().forEach( e->
                  nodes.get(e.getToV()).removeInDegree(e.getId())
             );
             sorted.add(n);
             nodes.remove(n.getId());
        });
        return topologicalSorting(nodes, sorted);
    }

    /**
     * @Description : 试图创建一个有泛型的返回值
     * @Param : [nodes, sorted]
     * @Return : java.util.Queue<cn.gsq.graph.dag.Vertex<T>>
     * @Author : gsq
     * @Date : 5:42 下午
     * @note : An art cell !
    **/
    public Queue<Vertex<T>> topologicalSortingPlus(Map<UUID, Vertex<T>> nodes, Queue<Vertex<T>> sorted) {
        //nodes -> all remaining vertices in the graph to be sorted
        Queue<Vertex<T>> s = new LinkedBlockingQueue<>(getRootVerticesPlus(nodes.values()));
        if (s.isEmpty()) {
            //cycle detected when nodes are not empty() so return empty sets since only to sort a dag
            //this is an error condition
            return nodes.isEmpty() ? sorted : new LinkedBlockingQueue<>();
        }

        s.parallelStream().forEach( n -> {
            n.getOutDegree().values().stream().forEach( e->
                    nodes.get(e.getToV()).removeInDegree(e.getId())
            );
            sorted.add(n);
            nodes.remove(n.getId());
        });
        return topologicalSortingPlus(nodes, sorted);
    }

    /**
     * return a list of root vertices. Root vertices are vertex which has only out degree edges but no in degree edges.
     * @param vPool
     * @return
     */
    public static Set<Vertex<?>> getRootVertices(Collection<Vertex<?>> vPool) {
        return vPool.parallelStream().
            filter(v -> v.getInDegree().size() == 0).
            collect(Collectors.toSet());
    }

    /**
     * @Description : 试图创建一个有泛型的返回值
     * @Param : [vPool]
     * @Return : java.util.Set<cn.gsq.graph.dag.Vertex<T>>
     * @Author : gsq
     * @Date : 5:44 下午
     * @note : An art cell !
    **/
    public static <T> Set<Vertex<T>> getRootVerticesPlus(Collection<Vertex<T>> vPool) {
        return vPool.parallelStream().
                filter(v -> v.getInDegree().size() == 0).
                collect(Collectors.toSet());
    }

    public Set<Vertex<?>> getRootVertices() {
        return getRootVertices(this.vertices.values());
    }

    /**
     * Remove the root and its associated in-degree against the receiving vertices.
     * @param vPool
     */
    public void romoveRootVertices(Collection<Vertex<?>> vPool) {
        vPool.parallelStream().forEach( n -> {
            n.getOutDegree().values().stream().forEach( e->
                 this.vertices.get(e.getToV()).removeInDegree(e.getId())
            );
            this.vertices.remove(n.getId());
       });
    }

    /**
     * If T is a relationship interface, vertex can apply its relationship to T.
     * Recursively apply relationships to all the vertices.
     * Relationship refers to parent and child UUID.
     */
    public void applyRelationship() {
        this.vertices.values().forEach(v -> v.applyRelationship());
        if (task instanceof DagRelationship) {
            DagRelationship r = (DagRelationship) task;
            r.setVertexInDag(this.vertices);
        }
        super.applyRelationship();
    }

}
