import java.util.ArrayList;
import java.util.List;

public class Q11_BstDeletion {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    private Node root;
    private int size;

    public boolean add(int value) {
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

    public boolean remove(int value) {
        Node parent = null;
        Node target = root;

        while (target != null && target.value != value) {
            parent = target;
            target = value < target.value ? target.left : target.right;
        }

        if (target == null) {
            return false;
        }

        if (target.left != null && target.right != null) {
            Node successorParent = target;
            Node successor = target.right;

            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            target.value = successor.value;
            parent = successorParent;
            target = successor;
        }

        Node replacement = target.left != null ? target.left : target.right;

        if (parent == null) {
            root = replacement;
        } else if (parent.left == target) {
            parent.left = replacement;
        } else {
            parent.right = replacement;
        }

        size--;
        return true;
    }

    public boolean contains(int value) {
        Node current = root;

        while (current != null) {
            if (value == current.value) {
                return true;
            }
            current = value < current.value ? current.left : current.right;
        }

        return false;
    }

    public int size() {
        return size;
    }

    public List<Integer> inorder() {
        List<Integer> values = new ArrayList<>();
        inorder(root, values);
        return values;
    }

    private void inorder(Node node, List<Integer> values) {
        if (node == null) {
            return;
        }

        inorder(node.left, values);
        values.add(node.value);
        inorder(node.right, values);
    }

    public boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(Node node, long lowerBound, long upperBound) {
        if (node == null) {
            return true;
        }

        if (node.value <= lowerBound || node.value >= upperBound) {
            return false;
        }

        return isValid(node.left, lowerBound, node.value)
                && isValid(node.right, node.value, upperBound);
    }
}
