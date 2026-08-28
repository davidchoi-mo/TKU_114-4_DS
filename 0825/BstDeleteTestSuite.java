import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BstDeleteTestSuite {
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

        boolean contains(int value) {
            Node current = root;
            while (current != null) {
                if (value == current.value) {
                    return true;
                }
                current = value < current.value ? current.left : current.right;
            }
            return false;
        }

        boolean delete(int value) {
            if (!contains(value)) {
                return false;
            }
            root = delete(root, value);
            size--;
            return true;
        }

        private Node delete(Node node, int value) {
            if (node == null) {
                return null;
            }
            if (value < node.value) {
                node.left = delete(node.left, value);
            } else if (value > node.value) {
                node.right = delete(node.right, value);
            } else {
                if (node.left == null) {
                    return node.right;
                }
                if (node.right == null) {
                    return node.left;
                }

                Node successor = minimumNode(node.right);
                node.value = successor.value;
                node.right = delete(node.right, successor.value);
            }
            return node;
        }

        private Node minimumNode(Node node) {
            while (node.left != null) {
                node = node.left;
            }
            return node;
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

        boolean isValid() {
            return isValid(root, null, null);
        }

        private boolean isValid(Node node, Long minExclusive, Long maxExclusive) {
            if (node == null) {
                return true;
            }
            long value = node.value;
            if ((minExclusive != null && value <= minExclusive)
                    || (maxExclusive != null && value >= maxExclusive)) {
                return false;
            }
            return isValid(node.left, minExclusive, value)
                    && isValid(node.right, value, maxExclusive);
        }
    }

    private static int passed;
    private static int failed;

    private static void check(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS - " + testName);
        } else {
            failed++;
            System.out.println("FAIL - " + testName);
        }
    }

    private static IntBst treeOf(int... values) {
        IntBst tree = new IntBst();
        for (int value : values) {
            tree.add(value);
        }
        return tree;
    }

    private static void testEmptyTree() {
        IntBst tree = new IntBst();
        check("empty：刪除回傳 false", !tree.delete(10));
        check("empty：size 保持 0", tree.size() == 0);
        check("empty：仍是 valid BST", tree.isValid());
    }

    private static void testMissingValue() {
        IntBst tree = treeOf(50, 30, 70);
        check("missing：刪除回傳 false", !tree.delete(99));
        check("missing：內容不變",
                tree.inorderList().equals(Arrays.asList(30, 50, 70)));
        check("missing：size 不變", tree.size() == 3);
    }

    private static void testSingleRoot() {
        IntBst tree = treeOf(50);
        check("single root：刪除成功", tree.delete(50));
        check("single root：刪除後為 empty", tree.inorderList().isEmpty());
        check("single root：size 為 0", tree.size() == 0);
    }

    private static void testRootWithOneChild() {
        IntBst tree = treeOf(50, 30);
        check("root one child：刪除成功", tree.delete(50));
        check("root one child：child 成為新 root",
                tree.inorderList().equals(Arrays.asList(30)));
        check("root one child：valid 且 size=1", tree.isValid() && tree.size() == 1);
    }

    private static void testRootWithTwoChildren() {
        IntBst tree = treeOf(50, 30, 70, 20, 40, 60, 80, 65);
        check("root two children：刪除成功", tree.delete(50));
        check("root two children：successor 正確接回",
                tree.inorderList().equals(Arrays.asList(20, 30, 40, 60, 65, 70, 80)));
        check("root two children：無重複且 valid", tree.isValid());
        check("root two children：size=7", tree.size() == 7);
    }

    private static void testDeleteUntilEmpty() {
        IntBst tree = treeOf(4, 2, 6, 1, 3, 5, 7);
        int[] deletionOrder = {4, 2, 6, 1, 3, 5, 7};
        boolean everyDeleteSucceeded = true;
        boolean alwaysValid = true;

        for (int value : deletionOrder) {
            everyDeleteSucceeded &= tree.delete(value);
            alwaysValid &= tree.isValid();
        }

        check("連續刪除：每次都成功", everyDeleteSucceeded);
        check("連續刪除：每一步都 valid", alwaysValid);
        check("連續刪除：最後為 empty", tree.size() == 0 && tree.inorderList().isEmpty());
    }

    public static void main(String[] args) {
        testEmptyTree();
        testMissingValue();
        testSingleRoot();
        testRootWithOneChild();
        testRootWithTwoChildren();
        testDeleteUntilEmpty();

        System.out.println("------------------------------");
        System.out.println("測試結果：passed=" + passed + ", failed=" + failed);
        if (failed > 0) {
            throw new AssertionError("有 " + failed + " 個測試失敗");
        }
    }
}
