import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ServiceCenterWorkflow {
    enum TicketStatus {
        WAITING("等待中"),
        COMPLETED("已完成"),
        CANCELLED("已取消");

        private final String label;

        TicketStatus(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    static class ServiceTicket {
        private final String id;
        private final String customerName;
        private final String issue;
        private TicketStatus status = TicketStatus.WAITING;

        ServiceTicket(String id, String customerName, String issue) {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("Ticket id 不可空白");
            }
            if (customerName == null || customerName.isBlank()) {
                throw new IllegalArgumentException("顧客姓名不可空白");
            }
            if (issue == null || issue.isBlank()) {
                throw new IllegalArgumentException("問題內容不可空白");
            }
            this.id = id;
            this.customerName = customerName;
            this.issue = issue;
        }

        @Override
        public String toString() {
            return id + "(" + customerName + ", " + issue + ", " + status + ")";
        }
    }

    private final Map<String, ServiceTicket> ticketsById = new LinkedHashMap<>();
    private final Deque<ServiceTicket> waitingQueue = new ArrayDeque<>();
    private final Deque<ServiceTicket> completedStack = new ArrayDeque<>();
    private final Set<String> usedIds = new HashSet<>();

    public boolean createTicket(String id, String customerName, String issue) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Ticket id 不可空白");
        }
        if (!usedIds.add(id)) {
            System.out.println("建立失敗：Ticket id " + id + " 已存在");
            return false;
        }

        ServiceTicket ticket = new ServiceTicket(id, customerName, issue);
        ticketsById.put(id, ticket);
        waitingQueue.offerLast(ticket);
        System.out.println("建立成功：" + ticket);
        return true;
    }

    public ServiceTicket processNext() {
        ServiceTicket ticket = waitingQueue.pollFirst();
        if (ticket == null) {
            System.out.println("處理失敗：Waiting Queue 為空");
            return null;
        }

        ticket.status = TicketStatus.COMPLETED;
        completedStack.addLast(ticket);
        System.out.println("處理完成：" + ticket);
        return ticket;
    }

    public boolean cancelWaiting(String id) {
        Iterator<ServiceTicket> iterator = waitingQueue.iterator();
        while (iterator.hasNext()) {
            ServiceTicket ticket = iterator.next();
            if (ticket.id.equals(id)) {
                iterator.remove();
                ticket.status = TicketStatus.CANCELLED;
                System.out.println("取消成功：" + ticket);
                return true;
            }
        }

        ServiceTicket knownTicket = ticketsById.get(id);
        if (knownTicket == null) {
            System.out.println("取消失敗：找不到 Ticket " + id);
        } else {
            System.out.println("取消失敗：Ticket " + id + " 不在 Waiting Queue，狀態="
                    + knownTicket.status);
        }
        return false;
    }

    public ServiceTicket undoLastCompletion() {
        ServiceTicket ticket = completedStack.pollLast();
        if (ticket == null) {
            System.out.println("Undo 失敗：沒有已完成的 Ticket");
            return null;
        }

        ticket.status = TicketStatus.WAITING;
        waitingQueue.offerFirst(ticket);
        System.out.println("Undo 成功並放回 Queue 前端：" + ticket);
        return ticket;
    }

    public ServiceTicket findById(String id) {
        ServiceTicket ticket = ticketsById.get(id);
        System.out.println(ticket == null
                ? "查詢結果：找不到 " + id
                : "查詢結果：" + ticket);
        return ticket;
    }

    public void printSummary() {
        int cancelledCount = 0;
        for (ServiceTicket ticket : ticketsById.values()) {
            if (ticket.status == TicketStatus.CANCELLED) {
                cancelledCount++;
            }
        }

        System.out.println("--- 服務中心摘要 ---");
        System.out.println("全部 Ticket：" + ticketsById.size());
        System.out.println("等待中：" + waitingQueue.size() + " " + waitingQueue);
        System.out.println("已完成：" + completedStack.size() + " " + completedStack);
        System.out.println("已取消：" + cancelledCount);
    }

    public static void main(String[] args) {
        ServiceCenterWorkflow center = new ServiceCenterWorkflow();

        System.out.println("=== 服務中心排隊與取消 ===");
        center.processNext();
        center.undoLastCompletion();
        center.createTicket("S001", "王小明", "無法登入");
        center.createTicket("S002", "陳美玲", "忘記密碼");
        center.createTicket("S003", "林志豪", "帳單問題");
        center.createTicket("S002", "重複測試", "重複 id");
        center.cancelWaiting("S999");
        center.processNext();
        center.processNext();
        center.cancelWaiting("S001");
        center.cancelWaiting("S003");
        center.undoLastCompletion();
        center.undoLastCompletion(); 
        center.findById("S002");
        center.findById("S999");
        center.printSummary();
    }
}
