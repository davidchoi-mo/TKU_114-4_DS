import java.util.ArrayList;
import java.util.List;

public class GenericRepositorySystem {
    public static void main(String[] args) {
        Repository<String> stringRepository = new Repository<>();
        stringRepository.add("Java");
        stringRepository.add("Collections");
        stringRepository.add("Generics");

        System.out.println("=== Repository<String> ===");
        stringRepository.printAll();
        System.out.println("get(1) = " + stringRepository.get(1));
        System.out.println("remove(0) = " + stringRepository.remove(0));
        System.out.println("size = " + stringRepository.size());
        System.out.println("完整內容 = " + stringRepository);

        Repository<Product> productRepository = new Repository<>();
        Product keyboard = new Product("P001", "Keyboard", 1290.0);
        Product mouse = new Product("P002", "Mouse", 690.0);
        Product monitor = new Product("P003", "Monitor", 5290.0);
        productRepository.add(keyboard);
        productRepository.add(mouse);
        productRepository.add(monitor);

        System.out.println("\n=== Repository<Product> ===");
        productRepository.printAll();
        System.out.println("get(2) = " + productRepository.get(2));
        System.out.println("remove(mouse) = " + productRepository.remove(mouse));
        System.out.println("size = " + productRepository.size());
        System.out.println("完整內容 = " + productRepository);
    }
}

class Repository<T> {
    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public T get(int index) {
        return items.get(index);
    }

    public T remove(int index) {
        return items.remove(index);
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public int size() {
        return items.size();
    }

    public void printAll() {
        for (int i = 0; i < items.size(); i++) {
            System.out.println("[" + i + "] " + items.get(i));
        }
    }

    @Override
    public String toString() {
        return items.toString();
    }
}

class Product {
    private final String id;
    private final String name;
    private final double price;

    Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', price=%.2f}",
                id, name, price);
    }
}
