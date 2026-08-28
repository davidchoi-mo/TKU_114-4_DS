import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Q02_ServiceOrder {
    public static class LineItem {
        private final String name;
        private final int unitPrice;
        private final int quantity;

        public LineItem(String name, int unitPrice, int quantity) {
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public int subtotal() {
            return unitPrice * quantity;
        }
    }

    private final String orderId;
    private final List<LineItem> items;

    public Q02_ServiceOrder(String orderId) {
        if (isBlank(orderId)) {
            throw new IllegalArgumentException("orderId must not be null or blank");
        }

        this.orderId = orderId;
        this.items = new ArrayList<>();
    }

    public boolean addItem(String name, int unitPrice, int quantity) {
        if (isBlank(name) || unitPrice < 0 || quantity <= 0) {
            return false;
        }

        items.add(new LineItem(name, unitPrice, quantity));
        return true;
    }

    public int itemCount() {
        return items.size();
    }

    public int totalAmount() {
        int total = 0;
        for (LineItem item : items) {
            total += item.subtotal();
        }
        return total;
    }

    public String largestItemName() {
        if (items.isEmpty()) {
            return "";
        }

        LineItem largest = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            LineItem current = items.get(i);
            if (current.subtotal() > largest.subtotal()) {
                largest = current;
            }
        }
        return largest.getName();
    }

    public List<String> itemSummaries() {
        List<String> summaries = new ArrayList<>();
        for (LineItem item : items) {
            summaries.add(item.getName() + ":" + item.subtotal());
        }
        return Collections.unmodifiableList(summaries);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
