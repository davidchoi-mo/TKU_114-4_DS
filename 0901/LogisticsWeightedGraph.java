import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

public class LogisticsWeightedGraph {
    public record Edge(String from, String to, int cost) {}

    private final Map<String, Map<String, Integer>> outgoing = new LinkedHashMap<>();
    private int edgeCount;

    public boolean addVertex(String vertex) {
        String normalized = requireVertexName(vertex);
        return outgoing.putIfAbsent(normalized, new LinkedHashMap<>()) == null;
    }

    public boolean addEdge(String from, String to, int cost) {
        validateEdgeInput(from, to, cost);
        Map<String, Integer> edges = outgoing.get(from.trim());
        if (edges.containsKey(to.trim())) {
            return false;
        }
        edges.put(to.trim(), cost);
        edgeCount++;
        return true;
    }

    public boolean updateEdge(String from, String to, int newCost) {
        validateEdgeInput(from, to, newCost);
        Map<String, Integer> edges = outgoing.get(from.trim());
        if (!edges.containsKey(to.trim())) {
            return false;
        }
        edges.put(to.trim(), newCost);
        return true;
    }

    public boolean removeEdge(String from, String to) {
        String source = requireExistingVertex(from);
        String target = requireExistingVertex(to);
        Integer removed = outgoing.get(source).remove(target);
        if (removed == null) {
            return false;
        }
        edgeCount--;
        return true;
    }

    public OptionalInt getCost(String from, String to) {
        String source = requireExistingVertex(from);
        String target = requireExistingVertex(to);
        Integer cost = outgoing.get(source).get(target);
        return cost == null ? OptionalInt.empty() : OptionalInt.of(cost);
    }

    public List<Edge> outgoingEdges(String vertex) {
        String source = requireExistingVertex(vertex);
        List<Edge> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : outgoing.get(source).entrySet()) {
            result.add(new Edge(source, entry.getKey(), entry.getValue()));
        }
        result.sort(Comparator.comparing(Edge::to));
        return result;
    }

    public int edgeCount() {
        return edgeCount;
    }

    private void validateEdgeInput(String from, String to, int cost) {
        requireExistingVertex(from);
        requireExistingVertex(to);
        if (cost < 0) {
            throw new IllegalArgumentException("物流成本不可為負數：" + cost);
        }
    }

    private String requireExistingVertex(String vertex) {
        String normalized = requireVertexName(vertex);
        if (!outgoing.containsKey(normalized)) {
            throw new IllegalArgumentException("不存在的 vertex：" + normalized);
        }
        return normalized;
    }

    private static String requireVertexName(String vertex) {
        if (vertex == null || vertex.isBlank()) {
            throw new IllegalArgumentException("vertex 不可為空白");
        }
        return vertex.trim();
    }

    public static void main(String[] args) {
        LogisticsWeightedGraph graph = new LogisticsWeightedGraph();
        for (String warehouse : List.of("台北倉", "台中倉", "高雄倉")) {
            graph.addVertex(warehouse);
        }

        graph.addEdge("台北倉", "台中倉", 1200);
        graph.addEdge("台中倉", "高雄倉", 1500);
        graph.addEdge("台北倉", "高雄倉", 2800);
        graph.updateEdge("台北倉", "高雄倉", 2600);

        System.out.println("台北倉 outgoing=" + graph.outgoingEdges("台北倉"));
        System.out.println("台北到高雄成本=" + graph.getCost("台北倉", "高雄倉").orElse(-1));
        System.out.println("edge count=" + graph.edgeCount());
        System.out.println("移除台北到高雄=" + graph.removeEdge("台北倉", "高雄倉"));

        try {
            graph.addEdge("台北倉", "台中倉", -100);
        } catch (IllegalArgumentException exception) {
            System.out.println("拒絕負權重：" + exception.getMessage());
        }
    }
}
