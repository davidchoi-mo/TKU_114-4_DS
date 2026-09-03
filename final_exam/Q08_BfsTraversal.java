import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Q08_BfsTraversal {

    public static List<String> bfs(
            Map<String, List<String>> graph, String start) {
        if (graph == null || start == null || !graph.containsKey(start)) {
            return Collections.emptyList();
        }

        List<String> traversal = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();

        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            traversal.add(current);

            List<String> neighbours = graph.get(current);
            if (neighbours == null) {
                continue;
            }

            for (String neighbour : neighbours) {
                if (neighbour != null && visited.add(neighbour)) {
                    queue.offer(neighbour);
                }
            }
        }

        return traversal;
    }

    public static Map<String, Integer> distanceFrom(
            Map<String, List<String>> graph, String start) {
        if (graph == null || start == null || !graph.containsKey(start)) {
            return Collections.emptyMap();
        }

        Map<String, Integer> distance = new LinkedHashMap<>();
        Queue<String> queue = new ArrayDeque<>();

        distance.put(start, 0);
        queue.offer(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            List<String> neighbours = graph.get(current);
            if (neighbours == null) {
                continue;
            }

            int nextDistance = distance.get(current) + 1;
            for (String neighbour : neighbours) {
                if (neighbour != null && !distance.containsKey(neighbour)) {
                    distance.put(neighbour, nextDistance);
                    queue.offer(neighbour);
                }
            }
        }

        return distance;
    }
}
