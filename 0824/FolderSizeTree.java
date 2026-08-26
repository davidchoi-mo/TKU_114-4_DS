import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FolderSizeTree {
    static class FolderNode {
        String name;
        long ownSize;
        FolderNode left;
        FolderNode right;

        FolderNode(String name, long ownSize) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("folder name must not be blank");
            }
            if (ownSize < 0) {
                throw new IllegalArgumentException("folder size must not be negative");
            }
            this.name = name;
            this.ownSize = ownSize;
        }
    }

    static class SubtreeInfo {
        final FolderNode root;
        final long totalSize;

        SubtreeInfo(FolderNode root, long totalSize) {
            this.root = root;
            this.totalSize = totalSize;
        }

        @Override
        public String toString() {
            return root.name + " (" + totalSize + ")";
        }
    }

    private static class Analysis {
        SubtreeInfo largest;
    }

    public static long subtreeSize(FolderNode node) {
        if (node == null) {
            return 0;
        }
        long leftSize = subtreeSize(node.left);
        long rightSize = subtreeSize(node.right);
        return leftSize + rightSize + node.ownSize;
    }

    public static Optional<SubtreeInfo> largestSubtree(FolderNode root) {
        if (root == null) {
            return Optional.empty();
        }
        Analysis analysis = new Analysis();
        analyzePostorder(root.left, analysis);
        analyzePostorder(root.right, analysis);
        return Optional.ofNullable(analysis.largest);
    }

    private static long analyzePostorder(FolderNode node, Analysis analysis) {
        if (node == null) {
            return 0;
        }
        long leftSize = analyzePostorder(node.left, analysis);
        long rightSize = analyzePostorder(node.right, analysis);
        long totalSize = leftSize + rightSize + node.ownSize;

        if (analysis.largest == null || totalSize > analysis.largest.totalSize) {
            analysis.largest = new SubtreeInfo(node, totalSize);
        }
        return totalSize;
    }

    public static List<String> leafFolders(FolderNode root) {
        List<String> result = new ArrayList<>();
        collectLeafFolders(root, result);
        return result;
    }

    private static void collectLeafFolders(FolderNode node, List<String> result) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            result.add(node.name);
            return;
        }
        collectLeafFolders(node.left, result);
        collectLeafFolders(node.right, result);
    }

    private static void printPostorderSizes(FolderNode node) {
        if (node == null) {
            return;
        }
        printPostorderSizes(node.left);
        printPostorderSizes(node.right);
        System.out.println(node.name + " subtree size=" + subtreeSize(node));
    }

    public static void main(String[] args) {
        FolderNode root = new FolderNode("root", 10);
        root.left = new FolderNode("documents", 30);
        root.right = new FolderNode("media", 20);
        root.left.left = new FolderNode("reports", 120);
        root.left.right = new FolderNode("notes", 40);
        root.right.left = new FolderNode("photos", 500);
        root.right.right = new FolderNode("music", 300);

        System.out.println("Postorder subtree report:");
        printPostorderSizes(root);
        System.out.println("total size=" + subtreeSize(root));
        System.out.println("largest proper subtree="
                + largestSubtree(root).map(SubtreeInfo::toString).orElse("none"));
        System.out.println("leaf folders=" + leafFolders(root));

        System.out.println("empty total size=" + subtreeSize(null));
        System.out.println("empty largest subtree="
                + largestSubtree(null).map(SubtreeInfo::toString).orElse("none"));
    }
}
