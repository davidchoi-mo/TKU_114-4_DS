import java.util.NoSuchElementException;

public class BinaryTreeStatistics {
    static class NumberNode {
        int value;
        NumberNode left;
        NumberNode right;

        NumberNode(int value) {
            this.value = value;
        }
    }

    public static int size(NumberNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    public static long sum(NumberNode node) {
        if (node == null) {
            return 0;
        }
        return node.value + sum(node.left) + sum(node.right);
    }

    public static int maximum(NumberNode node) {
        if (node == null) {
            throw new NoSuchElementException("empty tree has no maximum value");
        }

        int maximum = node.value;
        if (node.left != null) {
            maximum = Math.max(maximum, maximum(node.left));
        }
        if (node.right != null) {
            maximum = Math.max(maximum, maximum(node.right));
        }
        return maximum;
    }

    public static int leafCount(NumberNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return leafCount(node.left) + leafCount(node.right);
    }

    public static int height(NumberNode node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public static boolean contains(NumberNode node, int target) {
        if (node == null) {
            return false;
        }
        return node.value == target
                || contains(node.left, target)
                || contains(node.right, target);
    }

    private static void printReport(String name, NumberNode root) {
        System.out.println("--- " + name + " ---");
        System.out.println("size=" + size(root));
        System.out.println("sum=" + sum(root));
        System.out.println("leaves=" + leafCount(root));
        System.out.println("height=" + height(root));
        if (root == null) {
            System.out.println("maximum=undefined (empty tree)");
        } else {
            System.out.println("maximum=" + maximum(root));
        }
    }

    public static void main(String[] args) {
        NumberNode root = new NumberNode(8);
        root.left = new NumberNode(-3);
        root.right = new NumberNode(12);
        root.left.left = new NumberNode(-10);
        root.left.right = new NumberNode(5);
        root.right.left = new NumberNode(9);
        root.right.right = new NumberNode(20);

        printReport("general tree", root);
        System.out.println("contains 9=" + contains(root, 9));
        System.out.println("contains 100=" + contains(root, 100));
        printReport("empty tree", null);
        printReport("single-node tree", new NumberNode(-7));
    }
}
