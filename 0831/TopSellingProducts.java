import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TopSellingProducts {
    public static final class Product {
        private final String id;
        private final long sales;

        public Product(String id, long sales) {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("id cannot be blank");
            }
            if (sales < 0) {
                throw new IllegalArgumentException("sales cannot be negative");
            }
            this.id = id.trim();
            this.sales = sales;
        }

        public String getId() {
            return id;
        }

        public long getSales() {
            return sales;
        }

        @Override
        public String toString() {
            return id + "|" + sales;
        }
    }

    private static final Comparator<Product> WORST_FIRST = (first, second) -> {
        int bySales = Long.compare(first.getSales(), second.getSales());
        if (bySales != 0) {
            return bySales;
        }
        return second.getId().compareTo(first.getId());
    };

    private static final Comparator<Product> RESULT_ORDER = (first, second) -> {
        int bySales = Long.compare(second.getSales(), first.getSales());
        if (bySales != 0) {
            return bySales;
        }
        return first.getId().compareTo(second.getId());
    };

    public static List<Product> topK(List<Product> products, int k) {
        if (products == null || k <= 0) {
            return List.of();
        }

        Map<String, Long> mergedSales = new LinkedHashMap<>();
        for (Product product : products) {
            if (product == null) {
                continue;
            }
            mergedSales.merge(product.getId(), product.getSales(), Math::addExact);
        }

        PriorityQueue<Product> topProducts = new PriorityQueue<>(WORST_FIRST);
        for (Map.Entry<String, Long> entry : mergedSales.entrySet()) {
            topProducts.offer(new Product(entry.getKey(), entry.getValue()));
            if (topProducts.size() > k) {
                topProducts.poll();
            }
        }

        List<Product> result = new ArrayList<>(topProducts);
        result.sort(RESULT_ORDER);
        return result;
    }

    public static void main(String[] args) {
        List<Product> salesData = List.of(
                new Product("P100", 80),
                new Product("P200", 120),
                new Product("P300", 95),
                new Product("P100", 70),
                new Product("P400", 150),
                new Product("P050", 150),
                new Product("P300", 55));

        System.out.println("TOP_3");
        for (Product product : topK(salesData, 3)) {
            System.out.println(product);
        }
        System.out.println("TOP_0|" + topK(salesData, 0));
    }
}
