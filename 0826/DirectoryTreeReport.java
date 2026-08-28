import java.util.ArrayList;
import java.util.List;

public class DirectoryTreeReport {
    public static void main(String[] args) {
        FsNode root = new FsNode("project");

        FsNode src = new FsNode("src");
        src.addChild(new FsNode("Main.java", 120));
        src.addChild(new FsNode("Utils.java", 80));

        FsNode docs = new FsNode("docs");
        docs.addChild(new FsNode("readme.md", 40));

        root.addChild(src);
        root.addChild(docs);
        root.addChild(new FsNode("build.gradle", 15));

        FileSystemAnalyzer analyzer = new FileSystemAnalyzer(root);
        int rootSize = analyzer.computeDirectorySizes();

        System.out.println("rootSize=" + rootSize);
        System.out.println("srcSize=" + src.size);
        System.out.println("docsSize=" + docs.size);
        System.out.println("totalNodeCount=" + analyzer.totalNodeCount());
        System.out.println("fileCount=" + analyzer.fileCount());
        System.out.println("directoryCount=" + analyzer.directoryCount());
        System.out.println("height=" + analyzer.height());

        FsNode largest = analyzer.largestFile();
        System.out.println("largestFile=" + largest.name + " size=" + largest.size);
    }
}

class FsNode {
    String name;
    boolean directory;
    int size;
    List<FsNode> children;

    FsNode(String name, int size) {
        this.name = name;
        this.directory = false;
        this.size = size;
        this.children = new ArrayList<>();
    }

    FsNode(String name) {
        this.name = name;
        this.directory = true;
        this.size = 0;
        this.children = new ArrayList<>();
    }

    void addChild(FsNode child) {
        children.add(child);
    }
}

class FileSystemAnalyzer {
    private final FsNode root;

    FileSystemAnalyzer(FsNode root) {
        this.root = root;
    }

    int computeDirectorySizes() {
        return computeDirectorySizes(root);
    }

    private int computeDirectorySizes(FsNode node) {
        if (!node.directory) {
            return node.size;
        }
        int total = 0;
        for (FsNode child : node.children) {
            total += computeDirectorySizes(child);
        }
        node.size = total;
        return total;
    }

    int totalNodeCount() {
        return totalNodeCount(root);
    }

    private int totalNodeCount(FsNode node) {
        int count = 1;
        for (FsNode child : node.children) {
            count += totalNodeCount(child);
        }
        return count;
    }

    int fileCount() {
        return fileCount(root);
    }

    private int fileCount(FsNode node) {
        int count = node.directory ? 0 : 1;
        for (FsNode child : node.children) {
            count += fileCount(child);
        }
        return count;
    }

    int directoryCount() {
        return directoryCount(root);
    }

    private int directoryCount(FsNode node) {
        int count = node.directory ? 1 : 0;
        for (FsNode child : node.children) {
            count += directoryCount(child);
        }
        return count;
    }

    int height() {
        return height(root);
    }

    private int height(FsNode node) {
        if (node.children.isEmpty()) {
            return 0;
        }
        int max = -1;
        for (FsNode child : node.children) {
            max = Math.max(max, height(child));
        }
        return max + 1;
    }

    FsNode largestFile() {
        FsNode[] holder = new FsNode[1];
        largestFile(root, holder);
        return holder[0];
    }

    private void largestFile(FsNode node, FsNode[] holder) {
        if (!node.directory) {
            if (holder[0] == null || node.size > holder[0].size) {
                holder[0] = node;
            }
        }
        for (FsNode child : node.children) {
            largestFile(child, holder);
        }
    }
}
