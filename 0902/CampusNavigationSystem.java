import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CampusNavigationSystem {
    private final Map<String, List<String>> roads = new HashMap<>();

    public boolean addLocation(String location) {
        if (location == null || location.isBlank() || roads.containsKey(location)) {
            return false;
        }
        roads.put(location, new ArrayList<>());
        return true;
    }

    public boolean connectLocations(String first, String second) {
        if (first == null || second == null
                || !roads.containsKey(first) || !roads.containsKey(second)
                || first.equals(second)) {
            return false;
        }

        boolean changed = false;
        if (!roads.get(first).contains(second)) {
            roads.get(first).add(second);
            changed = true;
        }
        if (!roads.get(second).contains(first)) {
            roads.get(second).add(first);
            changed = true;
        }
        return changed;
    }

    public List<String> shortestPath(String start, String target) {
        if (start == null || target == null
                || !roads.containsKey(start) || !roads.containsKey(target)) {
            return List.of();
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> previous = new HashMap<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(target)) {
                break;
            }

            for (String next : roads.getOrDefault(current, List.of())) {
                if (roads.containsKey(next) && visited.add(next)) {
                    previous.put(next, current);
                    queue.offer(next);
                }
            }
        }

        if (!visited.contains(target)) {
            return List.of();
        }

        List<String> path = new ArrayList<>();
        for (String current = target; current != null; current = previous.get(current)) {
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }

    public int edgeCount(String start, String target) {
        List<String> path = shortestPath(start, target);
        return path.isEmpty() ? -1 : path.size() - 1;
    }

    public boolean isEmpty() {
        return roads.isEmpty();
    }

    private static void printRoute(CampusNavigationSystem campus,
                                   String start, String target) {
        List<String> path = campus.shortestPath(start, target);
        System.out.printf("%s -> %s | path=%s | edgeCount=%d%n",
                start, target, path, campus.edgeCount(start, target));
    }

    public static void main(String[] args) {
        CampusNavigationSystem campus = new CampusNavigationSystem();
        for (String location : List.of(
                "Gate", "Library", "Cafeteria", "Lab", "Dormitory", "Gym")) {
            campus.addLocation(location);
        }

        campus.connectLocations("Gate", "Library");
        campus.connectLocations("Gate", "Cafeteria");
        campus.connectLocations("Library", "Lab");
        campus.connectLocations("Cafeteria", "Lab");
        campus.connectLocations("Lab", "Dormitory");

        System.out.println("=== Campus navigation ===");
        printRoute(campus, "Gate", "Dormitory");
        printRoute(campus, "Gate", "Gate");
        printRoute(campus, "Gate", "Gym");
        printRoute(campus, "Gate", "MissingBuilding");

        CampusNavigationSystem emptyCampus = new CampusNavigationSystem();
        System.out.println("empty graph path = "
                + emptyCampus.shortestPath("A", "B"));
    }
}
