import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;


public class DeliveryWorkflowSystem {
    enum Status {
        WAITING("等待配送"),
        COMPLETED("配送完成");

        private final String label;

        Status(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    static class Delivery {
        private final String id;
        private final String destination;
        private Status status = Status.WAITING;

        Delivery(String id, String destination) {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("配送編號不可空白");
            }
            if (destination == null || destination.isBlank()) {
                throw new IllegalArgumentException("配送地點不可空白");
            }
            this.id = id;
            this.destination = destination;
        }

        @Override
        public String toString() {
            return id + "(" + destination + ", " + status + ")";
        }
    }

    private final Map<String, Delivery> deliveriesById = new LinkedHashMap<>();
    private final Deque<Delivery> waitingQueue = new ArrayDeque<>();
    private final Deque<Delivery> completedStack = new ArrayDeque<>();

    public boolean addDelivery(String id, String destination) {
        if (deliveriesById.containsKey(id)) {
            System.out.println("新增失敗：配送編號 " + id + " 已存在");
            return false;
        }

        Delivery delivery = new Delivery(id, destination);
        deliveriesById.put(id, delivery);
        waitingQueue.offerLast(delivery);
        System.out.println("新增配送：" + delivery);
        return true;
    }

    public Delivery processNext() {
        Delivery delivery = waitingQueue.pollFirst();
        if (delivery == null) {
            System.out.println("處理失敗：沒有等待配送的工作");
            return null;
        }

        delivery.status = Status.COMPLETED;
        completedStack.addLast(delivery);
        System.out.println("完成配送：" + delivery);
        return delivery;
    }

    public Delivery undo() {
        Delivery delivery = completedStack.pollLast();
        if (delivery == null) {
            System.out.println("Undo 失敗：沒有完成紀錄");
            return null;
        }

        delivery.status = Status.WAITING;
        waitingQueue.offerFirst(delivery);
        System.out.println("復原完成紀錄：" + delivery);
        return delivery;
    }

    public Delivery findById(String id) {
        Delivery delivery = deliveriesById.get(id);
        System.out.println(delivery == null
                ? "查詢結果：找不到 " + id
                : "查詢結果：" + delivery);
        return delivery;
    }

    public void printStatistics() {
        System.out.println("--- 物流統計 ---");
        System.out.println("全部工作：" + deliveriesById.size());
        System.out.println("等待配送：" + waitingQueue.size() + " " + waitingQueue);
        System.out.println("配送完成：" + completedStack.size() + " " + completedStack);
    }

    public static void main(String[] args) {
        DeliveryWorkflowSystem system = new DeliveryWorkflowSystem();

        System.out.println("=== 物流工作流程 ===");
        system.processNext();
        system.undo();
        system.addDelivery("D001", "台北");
        system.addDelivery("D002", "台中");
        system.addDelivery("D003", "高雄");
        system.addDelivery("D002", "重複編號");
        system.processNext();
        system.processNext();
        system.undo();
        system.findById("D002");
        system.findById("D999");
        system.processNext();
        system.printStatistics();
    }
}
