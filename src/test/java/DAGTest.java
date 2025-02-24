import cn.gsq.graph.dag.DagPlus;
import cn.gsq.graph.dag.Vertex;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Project : galaxy
 * Class : PACKAGE_NAME.DAGTest
 *
 * @author : gsq
 * @date : 2023-03-28 14:55
 * @note : It's not technology, it's art !
 **/
public class DAGTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void DagTest() {
        AProcess jn = new AProcess("journalnode");
        AProcess nn = new AProcess("namenode");
        AProcess zk = new AProcess("zkfc");
        AProcess dn = new AProcess("datanode");

        Vertex<AProcess> vjn = new Vertex<>(jn);
        Vertex<AProcess> vnn = new Vertex<>(nn);
        Vertex<AProcess> vzk = new Vertex<>(zk);
        Vertex<AProcess> vdn = new Vertex<>(dn);

        DagPlus<AProcess> dag = new DagPlus<>();
        dag.putEdge(vjn, vnn);
        dag.putEdge(vnn, vzk);
        dag.putEdge(vzk, vdn);

//        Queue<Vertex<AProcess>> queue = dag.topologicalSorting(dag.getVerticesToMap(), new ConcurrentLinkedQueue<>());

        jn.add(nn);
        nn.add(zk);
        zk.add(dn);

        List<AProcess> processes = CollUtil.newArrayList(zk, dn, jn, nn);
        Queue<Vertex<AProcess>> queue = getDagResult(processes);


        for(Vertex<AProcess> vertex : queue) {
            System.out.println(vertex.getTask().getName());
        }

    }

    @Getter
    @Setter
    public static class AProcess {
        private final String name;
        private Integer num;
        private List<AProcess> children = CollUtil.newLinkedList();
        public AProcess(String name) {
            this.name = name;
        }
        public void add(AProcess process) {
            this.children.add(process);
        }
    }

    public static <T extends AProcess> Queue<Vertex<T>> getDagResult(List<T> apps) {
        DagPlus<T> dag = new DagPlus<>();
        // 创建GAG图顶点集合
        Map<String, Vertex<T>> vertices = MapUtil.newHashMap();
        apps.forEach(app -> vertices.put(app.getName(), new Vertex<>(app)));
        // 添加DAG图的关系
        for(T app : apps) {
            List<T> children = (List<T>) app.getChildren();
            // 将父顶点加入DAG图
            Vertex<T> pv = vertices.get(app.getName());
            dag.addVertex(pv);
            // 建立子顶点与父顶点的关系
            for(T child : children) {
                // 下游应用如果不在安装集合中则不参与DAG图的构建
                if(apps.contains(child)) {
                    Vertex<T> cv = vertices.get(child.getName());
                    dag.putEdge(pv, cv);
                }
            }
        }
        return dag.topologicalSorting(dag.getVerticesToMap(), new ConcurrentLinkedQueue<>());
    }

}
