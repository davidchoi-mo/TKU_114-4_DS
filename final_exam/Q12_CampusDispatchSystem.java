import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Q12_CampusDispatchSystem {
    public record Request(String id, String location, int priority, long sequence) {}

    private final Map<String, Set<String>> roads = new HashMap<>();
    private final Map<String, Request> requestsById = new HashMap<>();
    private final PriorityQueue<Request> pendingRequests = new PriorityQueue<>(
            Comparator.comparingInt(Request::priority)
                    .thenComparingLong(Request::sequence));

    public boolean addLocation(String location) {
        if (!isValidText(location) || roads.containsKey(location)) {
            return false;
        }

        roads.put(location, new LinkedHashSet<>());
        return true;
    }

    public boolean addRoad(String first, String second) {
        if (!isValidText(first)
                || !isValidText(second)
                || first.equals(second)
                || !roads.containsKey(first)
                || !roads.containsKey(second)
                || roads.get(first).contains(second)) {
            return false;
        }

        roads.get(first).add(second);
        roads.get(second).add(first);
        return true;
    }

    public boolean submit(Request request) {
        if (request == null
                || !isValidText(request.id())
                || !isValidText(request.location())
                || !roads.containsKey(request.location())
                || requestsById.containsKey(request.id())) {
            return false;
        }

        requestsById.put(request.id(), request);
        pendingRequests.offer(request);
        return true;
    }

    public Request nextReachable(String serviceCenter) {
        if (!isValidText(serviceCenter) || !roads.containsKey(serviceCenter)) {
            return null;
        }

        Set<String> reachableLocations = reachableFrom(serviceCenter);
        List<Request> unreachableRequests = new ArrayList<>();
        Request selected = null;

        while (!pendingRequests.isEmpty()) {
            Request current = pendingRequests.poll();
            if (reachableLocations.contains(current.location())) {
                selected = current;
                break;
            }
            unreachableRequests.add(current);
        }

        pendingRequests.addAll(unreachableRequests);

        if (selected != null) {
            requestsById.remove(selected.id());
        }
        return selected;
    }

    public List<String> route(String start, String target) {
        if (!isValidText(start)
                || !isValidText(target)
                || !roads.containsKey(start)
                || !roads.containsKey(target)) {
            return Collections.emptyList();
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(target)) {
                return buildPath(parent, start, target);
            }

            for (String neighbor : roads.get(current)) {
                if (visited.add(neighbor)) {
                    parent.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }

        return Collections.emptyList();
    }

    public int pendingCount() {
        return requestsById.size();
    }

    private Set<String> reachableFrom(String start) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();
        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : roads.get(current)) {
                if (visited.add(neighbor)) {
                    queue.offer(neighbor);
                }
            }
        }

        return visited;
    }

    private List<String> buildPath(
            Map<String, String> parent, String start, String target) {
        LinkedList<String> path = new LinkedList<>();
        String current = target;

        while (current != null) {
            path.addFirst(current);
            if (current.equals(start)) {
                return path;
            }
            current = parent.get(current);
        }

        return Collections.emptyList();
    }

    private boolean isValidText(String value) {
        return value != null && !value.isBlank();
    }
}
