import java.util.ArrayList;
import java.util.List;

public class OrderManagementBst {
    public static void main(String[] args) {
        OrderBst orders = new OrderBst();
        System.out.println("add=" + orders.add(new Order(300, "Amy", 1200.0, "PENDING")));
        System.out.println("add=" + orders.add(new Order(100, "Ben", 450.5, "PENDING")));
        System.out.println("add=" + orders.add(new Order(500, "Cara", 899.0, "SHIPPED")));
        System.out.println("negativeAmount=" + orders.add(new Order(700, "Drew", -20.0, "PENDING")));
        System.out.println("duplicate=" + orders.add(new Order(100, "Other", 10.0, "PENDING")));

        System.out.println("removeBeforeCancel=" + orders.remove(300));
        System.out.println("cancel=" + orders.cancel(300));
        System.out.println("removeAfterCancel=" + orders.remove(300));
        System.out.println("missing=" + orders.remove(999));

        System.out.println("updateStatus=" + orders.updateStatus(500, "DELIVERED"));
        System.out.println("find=" + orders.find(500));

        System.out.println("range=" + orders.idRange(100, 500));
        System.out.println("totalAmount=" + orders.totalAmount());

        System.out.println("-- inorder report --");
        for (Order order : orders.inorder()) {
            System.out.println(order);
        }
    }
}

class Order {
    int orderId;
    String customer;
    double amount;
    String status;

    Order(int orderId, String customer, double amount, String status) {
        this.orderId = orderId;
        this.customer = customer;
        this.amount = amount;
        this.status = status;
    }

    @Override
    public String toString() {
        return orderId + " " + customer + " amount=" + amount + " status=" + status;
    }
}

class OrderNode {
    Order data;
    OrderNode left;
    OrderNode right;

    OrderNode(Order data) {
        this.data = data;
    }
}

class OrderBst {
    private OrderNode root;

    boolean add(Order order) {
        if (order.amount < 0) {
            return false;
        }
        if (root == null) {
            root = new OrderNode(order);
            return true;
        }
        OrderNode current = root;
        while (true) {
            if (order.orderId == current.data.orderId) {
                return false;
            }
            if (order.orderId < current.data.orderId) {
                if (current.left == null) {
                    current.left = new OrderNode(order);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new OrderNode(order);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Order find(int orderId) {
        OrderNode current = root;
        while (current != null) {
            if (orderId == current.data.orderId) {
                return current.data;
            }
            current = orderId < current.data.orderId ? current.left : current.right;
        }
        return null;
    }

    boolean updateStatus(int orderId, String newStatus) {
        Order order = find(orderId);
        if (order == null) {
            return false;
        }
        order.status = newStatus;
        return true;
    }

    boolean cancel(int orderId) {
        return updateStatus(orderId, "CANCELLED");
    }

    boolean remove(int orderId) {
        Order order = find(orderId);
        if (order == null || !"CANCELLED".equals(order.status)) {
            return false;
        }
        root = remove(root, orderId);
        return true;
    }

    private OrderNode remove(OrderNode node, int orderId) {
        if (orderId < node.data.orderId) {
            node.left = remove(node.left, orderId);
        } else if (orderId > node.data.orderId) {
            node.right = remove(node.right, orderId);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            OrderNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = remove(node.right, successor.data.orderId);
        }
        return node;
    }

    private OrderNode minimumNode(OrderNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    List<Order> idRange(int low, int high) {
        List<Order> result = new ArrayList<>();
        if (low <= high) {
            idRange(root, low, high, result);
        }
        return result;
    }

    private void idRange(OrderNode node, int low, int high, List<Order> result) {
        if (node == null) {
            return;
        }
        if (low < node.data.orderId) {
            idRange(node.left, low, high, result);
        }
        if (low <= node.data.orderId && node.data.orderId <= high) {
            result.add(node.data);
        }
        if (node.data.orderId < high) {
            idRange(node.right, low, high, result);
        }
    }

    double totalAmount() {
        return totalAmount(root);
    }

    private double totalAmount(OrderNode node) {
        if (node == null) {
            return 0;
        }
        return node.data.amount + totalAmount(node.left) + totalAmount(node.right);
    }

    List<Order> inorder() {
        List<Order> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(OrderNode node, List<Order> result) {
        if (node == null) {
            return;
        }
        inorder(node.left, result);
        result.add(node.data);
        inorder(node.right, result);
    }
}
