public class LinkedTaskListSystem {
    static class Task {
        private final String id;
        private final String title;

        Task(String id, String title) {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("Task id 不可空白");
            }
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("Task title 不可空白");
            }
            this.id = id;
            this.title = title;
        }

        String getId() {
            return id;
        }

        @Override
        public String toString() {
            return id + "-" + title;
        }
    }

    static class TaskNode {
        private final Task task;
        private TaskNode next;

        TaskNode(Task task) {
            this.task = task;
        }
    }

    static class TaskLinkedList {
        private TaskNode head;
        private int size;

        public boolean addFirst(Task task) {
            if (containsId(task.getId())) {
                printDuplicate(task.getId());
                return false;
            }

            TaskNode newNode = new TaskNode(task);
            newNode.next = head;
            head = newNode;
            size++;
            return true;
        }

        public boolean addLast(Task task) {
            if (containsId(task.getId())) {
                printDuplicate(task.getId());
                return false;
            }

            TaskNode newNode = new TaskNode(task);
            if (head == null) {
                head = newNode;
                size++;
                return true;
            }

            TaskNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
            size++;
            return true;
        }

        public Task findById(String id) {
            TaskNode node = findNodeById(id);
            return node == null ? null : node.task;
        }

        public Task removeById(String id) {
            if (head == null) {
                return null;
            }

            if (head.task.getId().equals(id)) {
                Task removed = head.task;
                head = head.next;
                size--;
                return removed;
            }

            TaskNode current = head;
            while (current.next != null) {
                if (current.next.task.getId().equals(id)) {
                    Task removed = current.next.task;
                    current.next = current.next.next;
                    size--;
                    return removed;
                }
                current = current.next;
            }
            return null;
        }

        public boolean insertAfter(String existingId, Task task) {
            if (containsId(task.getId())) {
                printDuplicate(task.getId());
                return false;
            }

            TaskNode existingNode = findNodeById(existingId);
            if (existingNode == null) {
                System.out.println("插入失敗：找不到 " + existingId);
                return false;
            }

            TaskNode newNode = new TaskNode(task);
            newNode.next = existingNode.next;
            existingNode.next = newNode;
            size++;
            return true;
        }

        public int size() {
            return size;
        }

        public void printAll() {
            System.out.print("Task List(size=" + size + ")：");
            if (head == null) {
                System.out.println("[]");
                return;
            }

            System.out.print("[");
            TaskNode current = head;
            while (current != null) {
                System.out.print(current.task);
                if (current.next != null) {
                    System.out.print(" -> ");
                }
                current = current.next;
            }
            System.out.println("]");
        }

        private boolean containsId(String id) {
            return findNodeById(id) != null;
        }

        private TaskNode findNodeById(String id) {
            TaskNode current = head;
            while (current != null) {
                if (current.task.getId().equals(id)) {
                    return current;
                }
                current = current.next;
            }
            return null;
        }

        private void printDuplicate(String id) {
            System.out.println("新增失敗：Task id " + id + " 已存在");
        }
    }

    private static void printResult(String action, Object result) {
        System.out.println(action + "：" + (result == null ? "找不到" : result));
    }

    public static void main(String[] args) {
        TaskLinkedList tasks = new TaskLinkedList();

        System.out.println("=== 單向鏈結清單 ===");
        tasks.printAll();
        printResult("空 list 搜尋 T1", tasks.findById("T1"));
        printResult("空 list 刪除 T1", tasks.removeById("T1"));

        tasks.addLast(new Task("T1", "需求分析"));
        tasks.addLast(new Task("T2", "程式設計"));
        tasks.addLast(new Task("T3", "測試"));
        tasks.addFirst(new Task("T0", "建立專案"));
        tasks.insertAfter("T1", new Task("T15", "程式碼審查"));
        tasks.addLast(new Task("T2", "重複 id 測試"));
        tasks.insertAfter("T99", new Task("T4", "找不到位置測試"));
        tasks.printAll();

        printResult("搜尋 T2", tasks.findById("T2"));
        printResult("搜尋 T99", tasks.findById("T99"));
        printResult("刪除 head T0", tasks.removeById("T0"));
        tasks.printAll();
        printResult("刪除 middle T15", tasks.removeById("T15"));
        tasks.printAll();
        printResult("刪除 tail T3", tasks.removeById("T3"));
        tasks.printAll();
        printResult("刪除不存在 T99", tasks.removeById("T99"));
    }
}
