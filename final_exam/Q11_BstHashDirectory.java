import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Q11_BstHashDirectory {
    private static final class Node {
        private final int id;
        private Node left;
        private Node right;

        private Node(int id) {
            this.id = id;
        }
    }

    private Node root;
    private final Map<Integer, String> namesById = new HashMap<>();

    public boolean add(int id, String name) {
        if (id <= 0 || name == null) {
            return false;
        }

        String trimmedName = name.trim();
        if (trimmedName.isEmpty() || namesById.containsKey(id)) {
            return false;
        }

        root = insert(root, id);
        namesById.put(id, trimmedName);
        return true;
    }

    public String findName(int id) {
        return namesById.get(id);
    }

    public boolean remove(int id) {
        if (!namesById.containsKey(id)) {
            return false;
        }

        root = delete(root, id);
        namesById.remove(id);
        return true;
    }

    public List<Integer> idsBetween(int low, int high) {
        List<Integer> result = new ArrayList<>();
        if (low > high) {
            return result;
        }

        collectBetween(root, low, high, result);
        return result;
    }

    public int size() {
        return namesById.size();
    }

    private Node insert(Node node, int id) {
        if (node == null) {
            return new Node(id);
        }

        if (id < node.id) {
            node.left = insert(node.left, id);
        } else {
            node.right = insert(node.right, id);
        }
        return node;
    }

    private Node delete(Node node, int id) {
        if (node == null) {
            return null;
        }

        if (id < node.id) {
            node.left = delete(node.left, id);
        } else if (id > node.id) {
            node.right = delete(node.right, id);
        } else if (node.left == null) {
            return node.right;
        } else if (node.right == null) {
            return node.left;
        } else {
            Node successor = minimum(node.right);
            Node replacement = new Node(successor.id);
            replacement.left = node.left;
            replacement.right = delete(node.right, successor.id);
            return replacement;
        }
        return node;
    }

    private Node minimum(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private void collectBetween(
            Node node, int low, int high, List<Integer> result) {
        if (node == null) {
            return;
        }

        if (node.id > low) {
            collectBetween(node.left, low, high, result);
        }
        if (node.id >= low && node.id <= high) {
            result.add(node.id);
        }
        if (node.id < high) {
            collectBetween(node.right, low, high, result);
        }
    }
}
