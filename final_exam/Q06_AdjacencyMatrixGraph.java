import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Q06_AdjacencyMatrixGraph {
    private final List<String> vertices;
    private final Map<String, Integer> vertexIndices;
    private final boolean[][] adjacencyMatrix;

    public Q06_AdjacencyMatrixGraph(java.util.List<String> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("vertices cannot be null");
        }

        this.vertices = new ArrayList<String>(vertices.size());
        this.vertexIndices = new HashMap<String, Integer>();

        for (String vertex : vertices) {
            if (vertex == null) {
                throw new IllegalArgumentException("vertex cannot be null");
            }
            if (vertexIndices.containsKey(vertex)) {
                throw new IllegalArgumentException("duplicate vertex: " + vertex);
            }

            vertexIndices.put(vertex, this.vertices.size());
            this.vertices.add(vertex);
        }

        this.adjacencyMatrix = new boolean[this.vertices.size()][this.vertices.size()];
    }

    public boolean addEdge(String first, String second) {
        Integer firstIndex = vertexIndices.get(first);
        Integer secondIndex = vertexIndices.get(second);

        if (firstIndex == null || secondIndex == null
                || firstIndex.equals(secondIndex)
                || adjacencyMatrix[firstIndex][secondIndex]) {
            return false;
        }

        adjacencyMatrix[firstIndex][secondIndex] = true;
        adjacencyMatrix[secondIndex][firstIndex] = true;
        return true;
    }

    public boolean removeEdge(String first, String second) {
        Integer firstIndex = vertexIndices.get(first);
        Integer secondIndex = vertexIndices.get(second);

        if (firstIndex == null || secondIndex == null
                || firstIndex.equals(secondIndex)
                || !adjacencyMatrix[firstIndex][secondIndex]) {
            return false;
        }

        adjacencyMatrix[firstIndex][secondIndex] = false;
        adjacencyMatrix[secondIndex][firstIndex] = false;
        return true;
    }

    public boolean hasEdge(String first, String second) {
        Integer firstIndex = vertexIndices.get(first);
        Integer secondIndex = vertexIndices.get(second);

        return firstIndex != null
                && secondIndex != null
                && !firstIndex.equals(secondIndex)
                && adjacencyMatrix[firstIndex][secondIndex];
    }

    public int degree(String vertex) {
        Integer vertexIndex = vertexIndices.get(vertex);
        if (vertexIndex == null) {
            return 0;
        }

        int degree = 0;
        for (boolean hasEdge : adjacencyMatrix[vertexIndex]) {
            if (hasEdge) {
                degree++;
            }
        }
        return degree;
    }

    public java.util.List<String> neighbors(String vertex) {
        Integer vertexIndex = vertexIndices.get(vertex);
        List<String> result = new ArrayList<String>();

        if (vertexIndex == null) {
            return result;
        }

        for (int i = 0; i < vertices.size(); i++) {
            if (adjacencyMatrix[vertexIndex][i]) {
                result.add(vertices.get(i));
            }
        }
        return result;
    }
}
