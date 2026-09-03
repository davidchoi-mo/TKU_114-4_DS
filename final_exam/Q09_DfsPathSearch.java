import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Q09_DfsPathSearch {

    public static List<String> dfs(Map<String, List<String>> graph, String start) {
        List<String> traversalOrder = new ArrayList<>();

        if (graph == null || start == null || !graph.containsKey(start)) {
            return traversalOrder;
        }

        Set<String> visited = new HashSet<>();
        dfsRecursive(graph, start, visited, traversalOrder);
        return traversalOrder;
    }

    private static void dfsRecursive(
            Map<String, List<String>> graph,
            String current,
            Set<String> visited,
            List<String> traversalOrder) {

        if (!visited.add(current)) {
            return;
        }

        traversalOrder.add(current);

        List<String> neighbors = graph.get(current);
        if (neighbors == null) {
            return;
        }

        for (String neighbor : neighbors) {
            if (neighbor != null
                    && graph.containsKey(neighbor)
                    && !visited.contains(neighbor)) {
                dfsRecursive(graph, neighbor, visited, traversalOrder);
            }
        }
    }

    public static boolean reachable(
            Map<String, List<String>> graph,
            String start,
            String target) {

        if (graph == null
                || start == null
                || target == null
                || !graph.containsKey(start)
                || !graph.containsKey(target)) {
            return false;
        }

        return dfs(graph, start).contains(target);
    }
}
