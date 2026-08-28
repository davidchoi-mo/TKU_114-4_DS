import java.util.ArrayList;
import java.util.List;

public class TreeShapeComparison {
    private static final int KEY_COUNT = 15;
    private static final int MISSING_KEY = 16;

    public static void main(String[] args) {
        List<Integer> ascending = new ArrayList<>();
        for (int value = 1; value <= KEY_COUNT; value++) {
            ascending.add(value);
        }

        List<Integer> descending = new ArrayList<>();
        for (int value = KEY_COUNT; value >= 1; value--) {
            descending.add(value);
        }

        List<Integer> nearBalanced = new ArrayList<>();
        collectBalancedOrder(1, KEY_COUNT, nearBalanced);

        report("升冪 (Ascending)", ascending);
        report("降冪 (Descending)", descending);
        report("接近平衡 (Near-Balanced)", nearBalanced);
    }

    private static void report(String label, List<Integer> insertOrder) {
        ShapeBst tree = new ShapeBst();
        for (int value : insertOrder) {
            tree.add(value);
        }

        int totalComparisons = 0;
        for (int key = 1; key <= KEY_COUNT; key++) {
            totalComparisons += tree.searchComparisons(key);
        }
        int missingComparisons = tree.searchComparisons(MISSING_KEY);

        System.out.println(label);
        System.out.println("  insertOrder=" + insertOrder);
        System.out.println("  height=" + tree.height());
        System.out.println("  totalSearchComparisons(all " + KEY_COUNT + " keys)=" + totalComparisons);
        System.out.println("  missingKey(" + MISSING_KEY + ")Comparisons=" + missingComparisons);
    }

    private static void collectBalancedOrder(int low, int high, List<Integer> order) {
        if (low > high) {
            return;
        }
        int mid = (low + high) / 2;
        order.add(mid);
        collectBalancedOrder(low, mid - 1, order);
        collectBalancedOrder(mid + 1, high, order);
    }
}

class ShapeNode {
    int value;
    ShapeNode left;
    ShapeNode right;

    ShapeNode(int value) {
        this.value = value;
    }
}

class ShapeBst {
    private ShapeNode root;

    boolean add(int value) {
        if (root == null) {
            root = new ShapeNode(value);
            return true;
        }
        ShapeNode current = root;
        while (true) {
            if (value == current.value) {
                return false;
            }
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new ShapeNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ShapeNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    int height() {
        return height(root);
    }

    private int height(ShapeNode node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    int searchComparisons(int target) {
        int comparisons = 0;
        ShapeNode current = root;
        while (current != null) {
            comparisons++;
            if (target == current.value) {
                return comparisons;
            }
            current = target < current.value ? current.left : current.right;
        }
        return comparisons;
    }
}
