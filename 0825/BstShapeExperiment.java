import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BstShapeExperiment {
    private static final class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    private static final class IntBst {
        private Node root;
        private int size;

        boolean add(int value) {
            if (root == null) {
                root = new Node(value);
                size = 1;
                return true;
            }
            Node current = root;
            while (true) {
                if (value == current.value) {
                    return false;
                }
                if (value < current.value) {
                    if (current.left == null) {
                        current.left = new Node(value);
                        size++;
                        return true;
                    }
                    current = current.left;
                } else {
                    if (current.right == null) {
                        current.right = new Node(value);
                        size++;
                        return true;
                    }
                    current = current.right;
                }
            }
        }

        int searchComparisonCount(int target) {
            int comparisons = 0;
            Node current = root;
            while (current != null) {
                comparisons++;
                if (target == current.value) {
                    return comparisons;
                }
                current = target < current.value ? current.left : current.right;
            }
            return comparisons;
        }

        int totalSearchComparisonCount(int[] values) {
            int total = 0;
            for (int value : values) {
                total += searchComparisonCount(value);
            }
            return total;
        }

        int height() {
            return height(root);
        }

        private int height(Node node) {
            if (node == null) {
                return -1;
            }
            return 1 + Math.max(height(node.left), height(node.right));
        }

        int size() {
            return size;
        }

        List<Integer> inorderList() {
            List<Integer> result = new ArrayList<>();
            inorder(root, result);
            return result;
        }

        private void inorder(Node node, List<Integer> result) {
            if (node == null) {
                return;
            }
            inorder(node.left, result);
            result.add(node.value);
            inorder(node.right, result);
        }
    }

    private static void runExperiment(String name, int[] insertionOrder, int[] searchValues) {
        IntBst tree = new IntBst();
        for (int value : insertionOrder) {
            tree.add(value);
        }

        int totalComparisons = tree.totalSearchComparisonCount(searchValues);
        double averageComparisons = (double) totalComparisons / searchValues.length;
        System.out.printf(
                "%-12s | size=%2d | height=%2d | total=%3d | average=%.2f%n",
                name, tree.size(), tree.height(), totalComparisons, averageComparisons);
        System.out.println("  插入順序: " + Arrays.toString(insertionOrder));
        System.out.println("  inorder : " + tree.inorderList());
    }

    public static void main(String[] args) {
        int[] allValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        int[] ascendingOrder = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15
        };
        int[] balancedOrder = {
                8, 4, 12, 2, 6, 10, 14, 1, 3, 5, 7, 9, 11, 13, 15
        };
        int[] mixedOrder = {
                8, 3, 13, 1, 6, 10, 15, 4, 7, 9, 12, 14, 2, 5, 11
        };

        System.out.println("BST Shape Experiment");
        System.out.println("height 定義：empty=-1、leaf=0");
        System.out.println("---------------------------------------------------------------");
        runExperiment("遞增順序", ascendingOrder, allValues);
        runExperiment("平衡順序", balancedOrder, allValues);
        runExperiment("混合順序", mixedOrder, allValues);
        System.out.println("---------------------------------------------------------------");
        System.out.println("結論：插入順序會改變 tree shape；height 越低，搜尋比較次數通常越少。");
    }
}
