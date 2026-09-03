import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Q10_UnweightedShortestPath {

    private Q10_UnweightedShortestPath() {
    }

    public static List<String> shortestPath(
            Map<String, List<String>> graph,
            String start,
            String target) {

        if (graph == null || start == null || target == null
                || !graph.containsKey(start) || !graph.containsKey(target)) {
            return Collections.emptyList();
        }

        if (start.equals(target)) {
            return Collections.singletonList(start);
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessor = new HashMap<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            List<String> neighbors = graph.get(current);

            if (neighbors == null) {
                continue;
            }

            for (String neighbor : neighbors) {
                if (neighbor == null || visited.contains(neighbor)) {
                    continue;
                }

                visited.add(neighbor);
                predecessor.put(neighbor, current);

                if (neighbor.equals(target)) {
                    return buildPath(predecessor, start, target);
                }

                queue.offer(neighbor);
            }
        }

        return Collections.emptyList();
    }

    private static List<String> buildPath(
            Map<String, String> predecessor,
            String start,
            String target) {

        List<String> path = new ArrayList<>();
        String current = target;

        while (current != null) {
            path.add(current);
            if (current.equals(start)) {
                break;
            }
            current = predecessor.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}
