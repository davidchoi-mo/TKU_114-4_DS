import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CollectionChoiceReport {
    private static void printChoice(
            int number,
            String requirement,
            String interfaceName,
            String implementationName,
            Object result) {
        System.out.println(number + ". " + requirement);
        System.out.println("   interface      : " + interfaceName);
        System.out.println("   implementation : " + implementationName);
        System.out.println("   操作結果        : " + result);
    }

    private static void searchHistoryExample() {
        List<String> history = new ArrayList<>();
        history.add("Java Deque");
        history.add("ArrayList vs LinkedList");
        history.add("Java Deque");              
        printChoice(1, "保留搜尋紀錄且允許重複", "List<String>",
                "ArrayList<String>", history);
    }

    private static void memberIdExample() {
        Set<String> memberIds = new LinkedHashSet<>();
        memberIds.add("M001");
        memberIds.add("M002");
        boolean duplicateAdded = memberIds.add("M001");
        printChoice(2, "保存不重複會員編號", "Set<String>",
                "LinkedHashSet<String>",
                memberIds + "，再次加入 M001=" + duplicateAdded);
    }

    private static void scoreLookupExample() {
        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("S001", 88);
        scores.put("S002", 95);
        printChoice(3, "以學號查詢成績", "Map<String, Integer>",
                "LinkedHashMap<String, Integer>",
                "S002 的成績=" + scores.get("S002") + "，全部=" + scores);
    }

    private static void printQueueExample() {
        Deque<String> printQueue = new ArrayDeque<>();
        printQueue.offerLast("報告.pdf");
        printQueue.offerLast("作業.docx");
        printQueue.offerLast("圖片.png");
        String firstPrinted = printQueue.pollFirst();
        printChoice(4, "依到達順序處理列印工作", "Deque<String>",
                "ArrayDeque<String>",
                "先列印=" + firstPrinted + "，剩餘=" + printQueue);
    }

    private static void undoExample() {
        Deque<String> undoStack = new ArrayDeque<>();
        undoStack.addLast("輸入標題");
        undoStack.addLast("插入圖片");
        undoStack.addLast("刪除段落");
        String undoneAction = undoStack.pollLast();
        printChoice(5, "復原最近操作", "Deque<String>",
                "ArrayDeque<String>",
                "復原=" + undoneAction + "，剩餘=" + undoStack);
    }

    public static void main(String[] args) {
        System.out.println("=== 集合選擇報告與實作 ===");
        searchHistoryExample();
        memberIdExample();
        scoreLookupExample();
        printQueueExample();
        undoExample();
    }
}
