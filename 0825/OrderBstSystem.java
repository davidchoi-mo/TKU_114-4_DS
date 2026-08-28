import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderBstSystem {
    static final class Order {
        final int orderId;
        final String customerName;
        double amount;

        Order(int orderId, String customerName, double amount) {
            if (amount < 0) {
                throw new IllegalArgumentException("amount 不得小於 0");
            }
            this.orderId = orderId;
            this.customerName = customerName;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "%d %s amount=%.2f",
                    orderId, customerName, amount);
        }
    }

    private static final class Node {
        Order data;
        Node left;
        Node right;

        Node(Order data) {
            this.data = data;
        }
    }

    private Node root;
    private int size;

    public boolean add(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order 不得為 null");
        }
        if (root == null) {
            root = new Node(order);
            size = 1;
            return true;
        }

        Node current = root;
        while (true) {
            if (order.orderId == current.data.orderId) {
                return false;
            }
            if (order.orderId < current.data.orderId) {
                if (current.left == null) {
                    current.left = new Node(order);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(order);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    public Order find(int orderId) {
        Node current = root;
        while (current != null) {
            if (orderId == current.data.orderId) {
                return current.data;
            }
            current = orderId < current.data.orderId ? current.left : current.right;
        }
        return null;
    }

    public boolean cancel(int orderId) {
        if (find(orderId) == null) {
            return false;
        }
        root = delete(root, orderId);
        size--;
        return true;
    }

    private Node delete(Node node, int orderId) {
        if (node == null) {
            return null;
        }
        if (orderId < node.data.orderId) {
            node.left = delete(node.left, orderId);
        } else if (orderId > node.data.orderId) {
            node.right = delete(node.right, orderId);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            Node successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = delete(node.right, successor.data.orderId);
        }
        return node;
    }

    private Node minimumNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public boolean updateAmount(int orderId, double newAmount) {
        if (newAmount < 0) {
            return false;
        }
        Order order = find(orderId);
        if (order == null) {
            return false;
        }
        order.amount = newAmount;
        return true;
    }

    public List<Order> range(int low, int high) {
        List<Order> result = new ArrayList<>();
        if (low <= high) {
            range(root, low, high, result);
        }
        return result;
    }

    private void range(Node node, int low, int high, List<Order> result) {
        if (node == null) {
            return;
        }
        if (node.data.orderId > low) {
            range(node.left, low, high, result);
        }
        if (node.data.orderId >= low && node.data.orderId <= high) {
            result.add(node.data);
        }
        if (node.data.orderId < high) {
            range(node.right, low, high, result);
        }
    }

    public void printRangeReport(int low, int high) {
        System.out.println("訂單範圍 [" + low + ", " + high + "]:");
        if (low > high) {
            System.out.println("  無效範圍：low 不得大於 high");
            return;
        }
        List<Order> orders = range(low, high);
        if (orders.isEmpty()) {
            System.out.println("  無訂單");
            return;
        }
        for (Order order : orders) {
            System.out.println("  " + order);
        }
    }

    public String summary() {
        if (root == null) {
            return "orders=0, totalAmount=0.00, minOrderId=N/A, maxOrderId=N/A";
        }
        double totalAmount = sumAmount(root);
        int minOrderId = minimumNode(root).data.orderId;
        int maxOrderId = maximumNode(root).data.orderId;
        return String.format(Locale.US,
                "orders=%d, totalAmount=%.2f, minOrderId=%d, maxOrderId=%d",
                size, totalAmount, minOrderId, maxOrderId);
    }

    private double sumAmount(Node node) {
        if (node == null) {
            return 0.0;
        }
        return node.data.amount + sumAmount(node.left) + sumAmount(node.right);
    }

    private Node maximumNode(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    public boolean isValid() {
        return isValid(root, null, null);
    }

    private boolean isValid(Node node, Long minExclusive, Long maxExclusive) {
        if (node == null) {
            return true;
        }
        long key = node.data.orderId;
        if ((minExclusive != null && key <= minExclusive)
                || (maxExclusive != null && key >= maxExclusive)) {
            return false;
        }
        return isValid(node.left, minExclusive, key)
                && isValid(node.right, key, maxExclusive);
    }

    public static void main(String[] args) {
        OrderBstSystem system = new OrderBstSystem();
        System.out.println("新增 300: " + system.add(new Order(300, "Alice", 1200.0)));
        System.out.println("新增 100: " + system.add(new Order(100, "Bob", 450.5)));
        System.out.println("新增 500: " + system.add(new Order(500, "Carol", 2300.0)));
        System.out.println("新增 200: " + system.add(new Order(200, "David", 799.0)));
        System.out.println("新增 400: " + system.add(new Order(400, "Eve", 1500.0)));
        System.out.println("重複 orderId 300: "
                + system.add(new Order(300, "Duplicate", 1.0)));

        System.out.println("查詢 200: " + system.find(200));
        System.out.println("更新 200 金額: " + system.updateAmount(200, 899.0));
        system.printRangeReport(150, 450);
        System.out.println("取消 300: " + system.cancel(300));
        System.out.println("取消不存在的 999: " + system.cancel(999));
        System.out.println("Summary: " + system.summary());
        System.out.println("valid=" + system.isValid());
    }
}
