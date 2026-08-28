import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Q09_TreeTraversal {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    public static List<Integer> preorder(Node root) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

    private static void preorderHelper(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }

        result.add(node.value);
        preorderHelper(node.left, result);
        preorderHelper(node.right, result);
    }

    public static List<Integer> inorder(Node root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private static void inorderHelper(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }

        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    public static List<Integer> postorder(Node root) {
        List<Integer> result = new ArrayList<>();
        postorderHelper(root, result);
        return result;
    }

    private static void postorderHelper(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }

        postorderHelper(node.left, result);
        postorderHelper(node.right, result);
        result.add(node.value);
    }

    public static List<Integer> levelorder(Node root) {
        List<Integer> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Deque<Node> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            result.add(current.value);

            if (current.left != null) {
                queue.offer(current.left);
            }

            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return result;
    }
}