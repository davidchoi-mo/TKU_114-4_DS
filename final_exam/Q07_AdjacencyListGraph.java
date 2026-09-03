import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Q07_AdjacencyListGraph {
    private final Map<String, Set<String>> adjacencyList = new LinkedHashMap<>();

    public boolean addVertex(String vertex) {
        if (vertex == null || adjacencyList.containsKey(vertex)) {
            return false;
        }

        adjacencyList.put(vertex, new LinkedHashSet<>());
        return true;
    }

    public boolean addEdge(String from, String to) {
        if (!hasVertex(from) || !hasVertex(to) || from.equals(to)) {
            return false;
        }

        return adjacencyList.get(from).add(to);
    }

    public boolean removeEdge(String from, String to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return false;
        }

        return adjacencyList.get(from).remove(to);
    }

    public List<String> outgoing(String vertex) {
        if (!hasVertex(vertex)) {
            return Collections.emptyList();
        }

        return new ArrayList<>(adjacencyList.get(vertex));
    }

    public int inDegree(String vertex) {
        if (!hasVertex(vertex)) {
            return 0;
        }

        int degree = 0;
        for (Set<String> neighbors : adjacencyList.values()) {
            if (neighbors.contains(vertex)) {
                degree++;
            }
        }
        return degree;
    }

    public int edgeCount() {
        int count = 0;
        for (Set<String> neighbors : adjacencyList.values()) {
            count += neighbors.size();
        }
        return count;
    }

    private boolean hasVertex(String vertex) {
        return vertex != null && adjacencyList.containsKey(vertex);
    }
}
