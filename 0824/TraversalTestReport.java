import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TraversalTestReport {
    static class TreeNode {
        String value;
        TreeNode left;
        TreeNode right;

        TreeNode(String value) {
            this.value = value;
        }
    }

    static List<String> preorder(TreeNode root) {
        List<String> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    private static void preorder(TreeNode node, List<String> result) {
        if (node == null) return;
        result.add(node.value);
        preorder(node.left, result);
        preorder(node.right, result);
    }

    static List<String> inorder(TreeNode root) {
        List<String> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private static void inorder(TreeNode node, List<String> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    static List<String> postorder(TreeNode root) {
        List<String> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }

    private static void postorder(TreeNode node, List<String> result) {
        if (node == null) return;
        postorder(node.left, result);
        postorder(node.right, result);
        result.add(node.value);
    }

    static List<String> levelOrder(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            result.add(node.value);
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        return result;
    }

    private static boolean check(
            String traversalName, List<String> expected, List<String> actual) {
        boolean same = expected.equals(actual);
        System.out.printf("  %-11s expected=%-24s actual=%-24s same=%s%n",
                traversalName, expected, actual, same);
        return same;
    }

    private static boolean testTree(
            String treeName,
            TreeNode root,
            List<String> expectedPreorder,
            List<String> expectedInorder,
            List<String> expectedPostorder,
            List<String> expectedLevelOrder) {
        System.out.println("--- " + treeName + " ---");
        boolean passed = true;
        passed &= check("preorder", expectedPreorder, preorder(root));
        passed &= check("inorder", expectedInorder, inorder(root));
        passed &= check("postorder", expectedPostorder, postorder(root));
        passed &= check("level-order", expectedLevelOrder, levelOrder(root));
        System.out.println("  tree result: " + (passed ? "PASS" : "FAIL"));
        return passed;
    }

    public static void main(String[] args) {
        int passedTrees = 0;
        int totalTrees = 6;

        if (testTree("empty", null,
                List.of(), List.of(), List.of(), List.of())) {
            passedTrees++;
        }

        TreeNode single = new TreeNode("A");
        if (testTree("single-node", single,
                List.of("A"), List.of("A"), List.of("A"), List.of("A"))) {
            passedTrees++;
        }

        TreeNode onlyLeft = new TreeNode("A");
        onlyLeft.left = new TreeNode("B");
        onlyLeft.left.left = new TreeNode("C");
        if (testTree("only-left", onlyLeft,
                List.of("A", "B", "C"),
                List.of("C", "B", "A"),
                List.of("C", "B", "A"),
                List.of("A", "B", "C"))) {
            passedTrees++;
        }

        TreeNode onlyRight = new TreeNode("A");
        onlyRight.right = new TreeNode("B");
        onlyRight.right.right = new TreeNode("C");
        if (testTree("only-right", onlyRight,
                List.of("A", "B", "C"),
                List.of("A", "B", "C"),
                List.of("C", "B", "A"),
                List.of("A", "B", "C"))) {
            passedTrees++;
        }

        TreeNode complete = new TreeNode("A");
        complete.left = new TreeNode("B");
        complete.right = new TreeNode("C");
        complete.left.left = new TreeNode("D");
        complete.left.right = new TreeNode("E");
        complete.right.left = new TreeNode("F");
        complete.right.right = new TreeNode("G");
        if (testTree("complete", complete,
                List.of("A", "B", "D", "E", "C", "F", "G"),
                List.of("D", "B", "E", "A", "F", "C", "G"),
                List.of("D", "E", "B", "F", "G", "C", "A"),
                List.of("A", "B", "C", "D", "E", "F", "G"))) {
            passedTrees++;
        }

        TreeNode irregular = new TreeNode("A");
        irregular.left = new TreeNode("B");
        irregular.right = new TreeNode("C");
        irregular.left.right = new TreeNode("D");
        irregular.right.left = new TreeNode("E");
        irregular.right.left.right = new TreeNode("F");
        if (testTree("irregular", irregular,
                List.of("A", "B", "D", "C", "E", "F"),
                List.of("B", "D", "A", "E", "F", "C"),
                List.of("D", "B", "F", "E", "C", "A"),
                List.of("A", "B", "C", "D", "E", "F"))) {
            passedTrees++;
        }

        System.out.printf("%nSummary: %d/%d tree structures passed.%n",
                passedTrees, totalTrees);
    }
}
