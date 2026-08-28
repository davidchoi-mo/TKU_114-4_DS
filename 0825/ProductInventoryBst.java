import java.util.ArrayList;
import java.util.List;

public class ProductInventoryBst {
    static final class Product {
        final int id;
        final String name;
        int stock;

        Product(int id, String name, int stock) {
            if (stock < 0) {
                throw new IllegalArgumentException("初始庫存不得小於 0");
            }
            this.id = id;
            this.name = name;
            this.stock = stock;
        }

        @Override
        public String toString() {
            return id + " " + name + " stock=" + stock;
        }
    }

    private static final class Node {
        Product data;
        Node left;
        Node right;

        Node(Product data) {
            this.data = data;
        }
    }

    private Node root;
    private int size;

    public boolean add(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product 不得為 null");
        }
        if (root == null) {
            root = new Node(product);
            size = 1;
            return true;
        }

        Node current = root;
        while (true) {
            if (product.id == current.data.id) {
                return false;
            }
            if (product.id < current.data.id) {
                if (current.left == null) {
                    current.left = new Node(product);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(product);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    public Product find(int id) {
        Node current = root;
        while (current != null) {
            if (id == current.data.id) {
                return current.data;
            }
            current = id < current.data.id ? current.left : current.right;
        }
        return null;
    }

    public boolean restock(int id, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        Product product = find(id);
        if (product == null) {
            return false;
        }
        product.stock += quantity;
        return true;
    }

    public boolean deductStock(int id, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        Product product = find(id);
        if (product == null || product.stock < quantity) {
            return false;
        }
        product.stock -= quantity;
        return true;
    }

    public boolean delete(int id) {
        if (find(id) == null) {
            return false;
        }
        root = delete(root, id);
        size--;
        return true;
    }

    private Node delete(Node node, int id) {
        if (node == null) {
            return null;
        }
        if (id < node.data.id) {
            node.left = delete(node.left, id);
        } else if (id > node.data.id) {
            node.right = delete(node.right, id);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            Node successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = delete(node.right, successor.data.id);
        }
        return node;
    }

    private Node minimumNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public List<Product> inorderList() {
        List<Product> products = new ArrayList<>();
        inorder(root, products);
        return products;
    }

    private void inorder(Node node, List<Product> products) {
        if (node == null) {
            return;
        }
        inorder(node.left, products);
        products.add(node.data);
        inorder(node.right, products);
    }

    public void inorderReport() {
        System.out.println("商品庫存報表（依 id 排序）:");
        if (root == null) {
            System.out.println("  無商品");
            return;
        }
        for (Product product : inorderList()) {
            System.out.println("  " + product);
        }
    }

    public int size() {
        return size;
    }

    public boolean isValid() {
        return isValid(root, null, null);
    }

    private boolean isValid(Node node, Long minExclusive, Long maxExclusive) {
        if (node == null) {
            return true;
        }
        long key = node.data.id;
        if ((minExclusive != null && key <= minExclusive)
                || (maxExclusive != null && key >= maxExclusive)) {
            return false;
        }
        return isValid(node.left, minExclusive, key)
                && isValid(node.right, key, maxExclusive);
    }

    public static void main(String[] args) {
        ProductInventoryBst inventory = new ProductInventoryBst();
        System.out.println("新增 Keyboard: "
                + inventory.add(new Product(300, "Keyboard", 5)));
        System.out.println("新增 Mouse: "
                + inventory.add(new Product(100, "Mouse", 8)));
        System.out.println("新增 Monitor: "
                + inventory.add(new Product(500, "Monitor", 2)));
        System.out.println("新增 Hub: "
                + inventory.add(new Product(200, "Hub", 4)));
        System.out.println("重複 id 100: "
                + inventory.add(new Product(100, "Duplicate", 1)));

        System.out.println("查詢 200: " + inventory.find(200));
        System.out.println("補貨 200（+6）: " + inventory.restock(200, 6));
        System.out.println("扣除 100（-3）: " + inventory.deductStock(100, 3));
        System.out.println("庫存不足 500（-5）: " + inventory.deductStock(500, 5));
        System.out.println("刪除 300: " + inventory.delete(300));

        inventory.inorderReport();
        System.out.println("size=" + inventory.size() + ", valid=" + inventory.isValid());
    }
}
