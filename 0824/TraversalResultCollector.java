import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TraversalResultCollector {
    static class TreeNode {
        String value;
        TreeNode left;
        TreeNode right;

        TreeNode(String value) {
            this.value = value;
        }
    }

    public static List<String> preorder(TreeNode root) {
        List<String> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    private static void preorder(TreeNode node, List<String> result) {
        if (node == null) {
            return;
        }
        result.add(node.value);
        preorder(node.left, result);
        preorder(node.right, result);
    }

    public static List<String> inorder(TreeNode root) {
        List<String> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private static void inorder(TreeNode node, List<String> result) {
        if (node == null) {
            return;
        }
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    public static List<String> postorder(TreeNode root) {
        List<String> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }

    private static void postorder(TreeNode node, List<String> result) {
        if (node == null) {
            return;
        }
        postorder(node.left, result);
        postorder(node.right, result);
        result.add(node.value);
    }

    public static List<String> levelOrder(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
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

    private static void printTraversals(String name, TreeNode root) {
        System.out.println("--- " + name + " ---");
        System.out.println("preorder=" + preorder(root));
        System.out.println("inorder=" + inorder(root));
        System.out.println("postorder=" + postorder(root));
        System.out.println("level-order=" + levelOrder(root));
    }

    public static void main(String[] args) {
        TreeNode single = new TreeNode("A");

        TreeNode leftSkewed = new TreeNode("A");
        leftSkewed.left = new TreeNode("B");
        leftSkewed.left.left = new TreeNode("C");

        TreeNode complete = new TreeNode("A");
        complete.left = new TreeNode("B");
        complete.right = new TreeNode("C");
        complete.left.left = new TreeNode("D");
        complete.left.right = new TreeNode("E");
        complete.right.left = new TreeNode("F");
        complete.right.right = new TreeNode("G");

        printTraversals("empty tree", null);
        printTraversals("single-node tree", single);
        printTraversals("left-skewed tree", leftSkewed);
        printTraversals("complete tree", complete);
    }
}
