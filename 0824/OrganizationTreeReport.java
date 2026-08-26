import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class OrganizationTreeReport {
    static class OrgNode {
        String name;
        OrgNode left;
        OrgNode right;

        OrgNode(String name) {
            this.name = name;
        }
    }

    static class OrganizationTree {
        private final OrgNode root;

        OrganizationTree(OrgNode root) {
            this.root = root;
        }

        public String findParent(String target) {
            if (target == null || root == null || root.name.equals(target)) {
                return null;
            }
            OrgNode parent = findParent(root, target);
            return parent == null ? null : parent.name;
        }

        private OrgNode findParent(OrgNode node, String target) {
            if (node == null) {
                return null;
            }
            if ((node.left != null && node.left.name.equals(target))
                    || (node.right != null && node.right.name.equals(target))) {
                return node;
            }
            OrgNode inLeft = findParent(node.left, target);
            return inLeft != null ? inLeft : findParent(node.right, target);
        }

        public int findDepth(String target) {
            if (target == null) {
                return -1;
            }
            return findDepth(root, target, 0);
        }

        private int findDepth(OrgNode node, String target, int depth) {
            if (node == null) {
                return -1;
            }
            if (node.name.equals(target)) {
                return depth;
            }
            int leftDepth = findDepth(node.left, target, depth + 1);
            return leftDepth != -1
                    ? leftDepth
                    : findDepth(node.right, target, depth + 1);
        }

        public List<String> pathFromRoot(String target) {
            if (target == null) {
                return Collections.emptyList();
            }
            List<String> path = new ArrayList<>();
            if (buildPath(root, target, path)) {
                return path;
            }
            return Collections.emptyList();
        }

        private boolean buildPath(OrgNode node, String target, List<String> path) {
            if (node == null) {
                return false;
            }
            path.add(node.name);
            if (node.name.equals(target)) {
                return true;
            }
            if (buildPath(node.left, target, path)
                    || buildPath(node.right, target, path)) {
                return true;
            }
            path.remove(path.size() - 1);
            return false;
        }

        public void printByLevel() {
            if (root == null) {
                System.out.println("empty");
                return;
            }

            Queue<OrgNode> queue = new ArrayDeque<>();
            queue.offer(root);
            int depth = 0;
            while (!queue.isEmpty()) {
                int nodesThisLevel = queue.size();
                List<String> names = new ArrayList<>();
                for (int index = 0; index < nodesThisLevel; index++) {
                    OrgNode current = queue.poll();
                    names.add(current.name);
                    if (current.left != null) {
                        queue.offer(current.left);
                    }
                    if (current.right != null) {
                        queue.offer(current.right);
                    }
                }
                System.out.println("depth " + depth + ": " + names);
                depth++;
            }
        }
    }

    private static void printSearchReport(OrganizationTree tree, String target) {
        String parent = tree.findParent(target);
        int depth = tree.findDepth(target);
        List<String> path = tree.pathFromRoot(target);
        System.out.println("target=" + target);
        System.out.println("parent=" + (parent == null ? "none" : parent));
        System.out.println("depth=" + (depth == -1 ? "not found" : depth));
        System.out.println("path=" + path);
    }

    public static void main(String[] args) {
        OrgNode root = new OrgNode("HeadOffice");
        root.left = new OrgNode("Sales");
        root.right = new OrgNode("Technology");
        root.left.left = new OrgNode("Domestic");
        root.left.right = new OrgNode("Export");
        root.right.left = new OrgNode("Platform");
        root.right.right = new OrgNode("Support");

        OrganizationTree tree = new OrganizationTree(root);
        tree.printByLevel();
        printSearchReport(tree, "Platform");
        printSearchReport(tree, "HR");
        printSearchReport(tree, "HeadOffice");

        System.out.println("Empty organization:");
        new OrganizationTree(null).printByLevel();
    }
}
