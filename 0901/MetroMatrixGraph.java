import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetroMatrixGraph {
    private final List<String> stations;
    private final Map<String, Integer> stationIndex = new HashMap<>();
    private final boolean[][] matrix;
    private int edgeCount;

    public MetroMatrixGraph(List<String> stations) {
        if (stations == null || stations.isEmpty()) {
            throw new IllegalArgumentException("stations 不可為空");
        }
        List<String> copy = new ArrayList<>();
        for (String station : stations) {
            String normalized = requireStation(station);
            if (stationIndex.containsKey(normalized)) {
                throw new IllegalArgumentException("站名重複：" + normalized);
            }
            stationIndex.put(normalized, copy.size());
            copy.add(normalized);
        }
        this.stations = List.copyOf(copy);
        this.matrix = new boolean[copy.size()][copy.size()];
    }

    public boolean addEdge(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        if (a == b) {
            return false;
        }
        if (matrix[a][b]) {
            return false;
        }
        matrix[a][b] = true;
        matrix[b][a] = true;
        edgeCount++;
        return true;
    }

    public boolean removeEdge(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        if (!matrix[a][b]) {
            return false;
        }
        matrix[a][b] = false;
        matrix[b][a] = false;
        edgeCount--;
        return true;
    }

    public boolean hasEdge(String first, String second) {
        return matrix[indexOf(first)][indexOf(second)];
    }

    public int degree(String station) {
        int row = indexOf(station);
        int count = 0;
        for (boolean connected : matrix[row]) {
            if (connected) {
                count++;
            }
        }
        return count;
    }

    public List<String> neighbors(String station) {
        int row = indexOf(station);
        List<String> result = new ArrayList<>();
        for (int column = 0; column < stations.size(); column++) {
            if (matrix[row][column]) {
                result.add(stations.get(column));
            }
        }
        return result;
    }

    public int edgeCount() {
        return edgeCount;
    }

    public String matrixReport() {
        StringBuilder report = new StringBuilder("站點");
        for (String station : stations) {
            report.append('\t').append(station);
        }
        report.append(System.lineSeparator());

        for (int row = 0; row < stations.size(); row++) {
            report.append(stations.get(row));
            for (int column = 0; column < stations.size(); column++) {
                report.append('\t').append(matrix[row][column] ? 1 : 0);
            }
            if (row < stations.size() - 1) {
                report.append(System.lineSeparator());
            }
        }
        return report.toString();
    }

    private int indexOf(String station) {
        Integer index = stationIndex.get(requireStation(station));
        if (index == null) {
            throw new IllegalArgumentException("不存在的站點：" + station);
        }
        return index;
    }

    private static String requireStation(String station) {
        if (station == null || station.isBlank()) {
            throw new IllegalArgumentException("station 不可為空白");
        }
        return station.trim();
    }

    public static void main(String[] args) {
        MetroMatrixGraph metro = new MetroMatrixGraph(
                List.of("台北車站", "中山", "雙連", "民權西路"));
        metro.addEdge("台北車站", "中山");
        metro.addEdge("中山", "雙連");
        metro.addEdge("雙連", "民權西路");
        metro.addEdge("台北車站", "中山");

        System.out.println("中山鄰站=" + metro.neighbors("中山"));
        System.out.println("中山 degree=" + metro.degree("中山"));
        System.out.println("edge count=" + metro.edgeCount());
        System.out.println(metro.matrixReport());
    }
}
