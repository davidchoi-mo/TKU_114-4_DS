import java.util.ArrayList;
import java.util.List;

public class IntegratedStructureAudit {
    public enum DataStructure {
        LIST("List / ArrayList"),
        QUEUE("Queue / ArrayDeque"),
        BST("Balanced BST / TreeMap"),
        HEAP("Heap / PriorityQueue"),
        HASH_TABLE("Hash Table / HashMap"),
        GRAPH("Graph adjacency list");

        private final String displayName;

        DataStructure(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Requirement {
        INDEX_ACCESS,
        FIFO_PROCESSING,
        SORTED_RANGE_QUERY,
        NEXT_PRIORITY,
        KEY_LOOKUP,
        RELATION_TRAVERSAL
    }

    public static final class Scenario {
        private final String name;
        private final Requirement requirement;
        private final DataStructure chosen;

        public Scenario(String name, Requirement requirement,
                        DataStructure chosen) {
            this.name = name;
            this.requirement = requirement;
            this.chosen = chosen;
        }
    }

    public static final class AuditResult {
        private final String scenarioName;
        private final boolean reasonable;
        private final DataStructure chosen;
        private final DataStructure recommended;
        private final String reason;
        private final String bigO;

        private AuditResult(String scenarioName, boolean reasonable,
                            DataStructure chosen, DataStructure recommended,
                            String reason, String bigO) {
            this.scenarioName = scenarioName;
            this.reasonable = reasonable;
            this.chosen = chosen;
            this.recommended = recommended;
            this.reason = reason;
            this.bigO = bigO;
        }

        @Override
        public String toString() {
            if (recommended == null) {
                return scenarioName + " | INVALID | " + reason;
            }
            return String.format(
                    "%s | %s | chosen=%s | recommended=%s | %s | %s",
                    scenarioName,
                    reasonable ? "合理" : "不合理",
                    chosen == null ? "null" : chosen.getDisplayName(),
                    recommended.getDisplayName(),
                    reason,
                    bigO);
        }
    }

    public static AuditResult audit(Scenario scenario) {
        if (scenario == null) {
            return invalidResult("null scenario", "測試情境不可為 null");
        }
        if (scenario.name == null || scenario.name.isBlank()) {
            return invalidResult("unnamed scenario", "情境名稱不可為空");
        }
        if (scenario.requirement == null) {
            return invalidResult(scenario.name, "需求不可為 null");
        }

        DataStructure recommended = recommendedFor(scenario.requirement);
        boolean reasonable = scenario.chosen == recommended;
        return new AuditResult(
                scenario.name,
                reasonable,
                scenario.chosen,
                recommended,
                reasonFor(scenario.requirement),
                bigOFor(scenario.requirement));
    }

    public static List<AuditResult> auditAll(List<Scenario> scenarios) {
        List<AuditResult> results = new ArrayList<>();
        if (scenarios == null) {
            return results;
        }
        for (Scenario scenario : scenarios) {
            results.add(audit(scenario));
        }
        return results;
    }

    private static AuditResult invalidResult(String name, String reason) {
        return new AuditResult(name, false, null, null, reason, "N/A");
    }

    private static DataStructure recommendedFor(Requirement requirement) {
        return switch (requirement) {
            case INDEX_ACCESS -> DataStructure.LIST;
            case FIFO_PROCESSING -> DataStructure.QUEUE;
            case SORTED_RANGE_QUERY -> DataStructure.BST;
            case NEXT_PRIORITY -> DataStructure.HEAP;
            case KEY_LOOKUP -> DataStructure.HASH_TABLE;
            case RELATION_TRAVERSAL -> DataStructure.GRAPH;
        };
    }

    private static String reasonFor(Requirement requirement) {
        return switch (requirement) {
            case INDEX_ACCESS -> "需要頻繁依 index 讀取元素";
            case FIFO_PROCESSING -> "資料必須依先進先出順序處理";
            case SORTED_RANGE_QUERY -> "需要維持排序並執行範圍查詢";
            case NEXT_PRIORITY -> "需要反覆取得目前最高優先資料";
            case KEY_LOOKUP -> "需要依唯一 key 快速查找資料";
            case RELATION_TRAVERSAL -> "需要保存多對多關係並走訪相鄰節點";
        };
    }

    private static String bigOFor(Requirement requirement) {
        return switch (requirement) {
            case INDEX_ACCESS -> "get: O(1)";
            case FIFO_PROCESSING -> "offer/poll: O(1)";
            case SORTED_RANGE_QUERY -> "search/insert: O(log n)（平衡時）";
            case NEXT_PRIORITY -> "peek: O(1), add/remove: O(log n)";
            case KEY_LOOKUP -> "get/put: 平均 O(1)，最差 O(n)";
            case RELATION_TRAVERSAL -> "BFS/DFS: O(V + E)";
        };
    }

    public static void main(String[] args) {
        List<Scenario> scenarios = List.of(
                new Scenario("依座號取得學生", Requirement.INDEX_ACCESS,
                        DataStructure.LIST),
                new Scenario("用 Graph 依座號取學生", Requirement.INDEX_ACCESS,
                        DataStructure.GRAPH),
                new Scenario("客服依到達順序處理", Requirement.FIFO_PROCESSING,
                        DataStructure.QUEUE),
                new Scenario("用 BST 處理 FIFO", Requirement.FIFO_PROCESSING,
                        DataStructure.BST),
                new Scenario("查詢指定成績區間", Requirement.SORTED_RANGE_QUERY,
                        DataStructure.BST),
                new Scenario("用 Hash Table 做排序區間", Requirement.SORTED_RANGE_QUERY,
                        DataStructure.HASH_TABLE),
                new Scenario("取得最高優先工作", Requirement.NEXT_PRIORITY,
                        DataStructure.HEAP),
                new Scenario("用 Queue 取最高優先工作", Requirement.NEXT_PRIORITY,
                        DataStructure.QUEUE),
                new Scenario("依學號查學生", Requirement.KEY_LOOKUP,
                        DataStructure.HASH_TABLE),
                new Scenario("用 List 依 key 查詢", Requirement.KEY_LOOKUP,
                        DataStructure.LIST),
                new Scenario("搜尋校園道路", Requirement.RELATION_TRAVERSAL,
                        DataStructure.GRAPH),
                new Scenario("用 Heap 保存道路關係", Requirement.RELATION_TRAVERSAL,
                        DataStructure.HEAP));

        System.out.println("=== Integrated structure audit ===");
        for (AuditResult result : auditAll(scenarios)) {
            System.out.println(result);
        }

        System.out.println("=== Boundary cases ===");
        System.out.println("empty list result count = "
                + auditAll(List.of()).size());
        System.out.println(audit(null));
        System.out.println(audit(new Scenario(
                "missing requirement", null, DataStructure.LIST)));
    }
}
