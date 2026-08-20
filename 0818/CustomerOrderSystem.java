class Customer {
    private final String customerId;
    private final String name;

    Customer(String customerId, String name) {
        this.customerId = normalize(customerId, "UNKNOWN");
        this.name = normalize(name, "Unknown");
    }

    private static String normalize(String value, String defaultValue) {
        return value == null || value.trim().isEmpty()
                ? defaultValue : value.trim();
    }

    String summary() {
        return customerId + " " + name;
    }
}

class OrderItem {
    private final String productId;
    private final String productName;
    private final int unitPrice;
    private final int quantity;

    OrderItem(String productId, String productName, int unitPrice, int quantity) {
        this.productId = normalize(productId, "UNKNOWN");
        this.productName = normalize(productName, "Unknown");
        this.unitPrice = Math.max(0, unitPrice);
        this.quantity = Math.max(0, quantity);
    }

    private static String normalize(String value, String defaultValue) {
        return value == null || value.trim().isEmpty()
                ? defaultValue : value.trim();
    }

    int getQuantity() {
        return quantity;
    }

    int subtotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return productId + " " + productName + " $" + unitPrice
                + " x " + quantity + " = $" + subtotal();
    }
}

class CustomerOrder {
    private final String orderId;
    private final Customer customer;
    private final OrderItem[] items;
    private int itemCount;

    CustomerOrder(String orderId, Customer customer, int capacity) {
        this.orderId = orderId == null || orderId.trim().isEmpty()
                ? "UNKNOWN" : orderId.trim();
        this.customer = customer == null
                ? new Customer("UNKNOWN", "Unknown") : customer;
        this.items = new OrderItem[Math.max(1, capacity)];
        this.itemCount = 0;
    }

    boolean addItem(OrderItem item) {
        if (item == null || itemCount >= items.length) {
            return false;
        }
        items[itemCount] = item;
        itemCount++;
        return true;
    }

    int calculateTotal() {
        int total = 0;
        for (int i = 0; i < itemCount; i++) {
            total += items[i].subtotal();
        }
        return total;
    }

    int getTotalQuantity() {
        int totalQuantity = 0;
        for (int i = 0; i < itemCount; i++) {
            totalQuantity += items[i].getQuantity();
        }
        return totalQuantity;
    }

    String summary() {
        StringBuilder result = new StringBuilder();
        result.append("訂單：").append(orderId).append('\n');
        result.append("顧客：").append(customer.summary()).append('\n');
        for (int i = 0; i < itemCount; i++) {
            result.append("  ").append(items[i]).append('\n');
        }
        result.append("品項種類：").append(itemCount).append('\n');
        result.append("商品總數：").append(getTotalQuantity()).append('\n');
        result.append("訂單總額：$").append(calculateTotal());
        return result.toString();
    }
}

public class CustomerOrderSystem {
    public static void main(String[] args) {
        Customer customer = new Customer("C101", "Amy");
        CustomerOrder order = new CustomerOrder("O9001", customer, 4);

        order.addItem(new OrderItem("P101", "Keyboard", 1200, 1));
        order.addItem(new OrderItem("P102", "Mouse", 600, 2));
        order.addItem(new OrderItem("P103", "USB Cable", 250, 3));

        System.out.println(order.summary());
    }
}
