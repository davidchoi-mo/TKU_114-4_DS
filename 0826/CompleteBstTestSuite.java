import java.util.List;

public class CompleteBstTestSuite {
    private static int passCount = 0;
    private static int failCount = 0;

    private static void check(String description, boolean condition) {
        if (condition) {
            passCount++;
            System.out.println("PASS - " + description);
        } else {
            failCount++;
            System.out.println("FAIL - " + description);
        }
    }

    public static void main(String[] args) {
        AuditBst tree = new AuditBst();

        check("empty size is 0", tree.size() == 0);
        check("empty height is -1", tree.height() == -1);
        check("empty contains returns false", !tree.contains(50));
        check("empty isValid returns true", tree.isValid());
        check("empty remove returns false", !tree.remove(50));
        check("empty inorder is an empty list", tree.inorder().isEmpty());

        check("add root returns true", tree.add(50));
        check("size is 1 after root add", tree.size() == 1);
        check("height is 0 for single root", tree.height() == 0);
        check("contains root value", tree.contains(50));

        check("duplicate add returns false", !tree.add(50));
        check("size unchanged after duplicate", tree.size() == 1);

        for (int value : new int[]{30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        check("size is 7 after full build", tree.size() == 7);
        check("isValid true after full build", tree.isValid());
        check("contains 80 after full build", tree.contains(80));
        check("height is 2 for the standard tree", tree.height() == 2);

        check("range(35,70) matches expected result",
                tree.range(35, 70).equals(List.of(40, 50, 60, 70)));
        check("range(70,35) is empty when low>high", tree.range(70, 35).isEmpty());
        check("inorder is sorted ascending",
                tree.inorder().equals(List.of(20, 30, 40, 50, 60, 70, 80)));

        check("deleteCase(20) is LEAF", tree.deleteCase(20).equals("LEAF"));
        check("remove(20) returns true", tree.remove(20));
        check("20 is gone after removal", !tree.contains(20));
        check("size is 6 after leaf removal", tree.size() == 6);
        check("isValid true after leaf removal", tree.isValid());

        check("deleteCase(30) is ONE_CHILD", tree.deleteCase(30).equals("ONE_CHILD"));
        check("remove(30) returns true", tree.remove(30));
        check("child 40 survives the removal", tree.contains(40));
        check("30 is gone after removal", !tree.contains(30));
        check("size is 5 after one-child removal", tree.size() == 5);
        check("isValid true after one-child removal", tree.isValid());

        check("deleteCase(70) is TWO_CHILDREN", tree.deleteCase(70).equals("TWO_CHILDREN"));
        check("remove(70) returns true", tree.remove(70));
        check("60 survives the two-child removal", tree.contains(60));
        check("80 survives the two-child removal", tree.contains(80));
        check("70 is gone after removal", !tree.contains(70));
        check("size is 4 after two-child removal", tree.size() == 4);
        check("inorder still sorted after two-child removal",
                tree.inorder().equals(List.of(40, 50, 60, 80)));
        check("isValid true after two-child removal", tree.isValid());

        check("remove of a missing key returns false", !tree.remove(999));
        check("contains of a missing key returns false", !tree.contains(999));

        check("isValid true right before corruption", tree.isValid());
        tree.corruptForTest();
        check("isValid false after forced invariant violation", !tree.isValid());

        System.out.println("TOTAL " + (passCount + failCount)
                + " assertions, PASS=" + passCount + " FAIL=" + failCount);
    }
}

class AuditNode {
    int value;
    AuditNode left;
    AuditNode right;

    AuditNode(int value) {
        this.value = value;
    }
}

class AuditBst {
    private AuditNode root;

    boolean add(int value) {
        if (root == null) {
            root = new AuditNode(value);
            return true;
        }
        AuditNode current = root;
        while (true) {
            if (value == current.value) {
                return false;
            }
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new AuditNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new AuditNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int target) {
        AuditNode current = root;
        while (current != null) {
            if (target == current.value) {
                return true;
            }
            current = target < current.value ? current.left : current.right;
        }
        return false;
    }

    int size() {
        return size(root);
    }

    private int size(AuditNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(AuditNode node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    String deleteCase(int target) {
        AuditNode node = findNode(target);
        if (node == null) {
            return "MISSING";
        }
        if (node.left == null && node.right == null) {
            return "LEAF";
        }
        if (node.left == null || node.right == null) {
            return "ONE_CHILD";
        }
        return "TWO_CHILDREN";
    }

    private AuditNode findNode(int target) {
        AuditNode current = root;
        while (current != null) {
            if (target == current.value) {
                return current;
            }
            current = target < current.value ? current.left : current.right;
        }
        return null;
    }

    boolean remove(int target) {
        if (!contains(target)) {
            return false;
        }
        root = remove(root, target);
        return true;
    }

    private AuditNode remove(AuditNode node, int target) {
        if (target < node.value) {
            node.left = remove(node.left, target);
        } else if (target > node.value) {
            node.right = remove(node.right, target);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            AuditNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private AuditNode minimumNode(AuditNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    List<Integer> inorder() {
        List<Integer> result = new java.util.ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(AuditNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    List<Integer> range(int low, int high) {
        List<Integer> result = new java.util.ArrayList<>();
        if (low <= high) {
            range(root, low, high, result);
        }
        return result;
    }

    private void range(AuditNode node, int low, int high, List<Integer> result) {
        if (node == null) {
            return;
        }
        if (low < node.value) {
            range(node.left, low, high, result);
        }
        if (low <= node.value && node.value <= high) {
            result.add(node.value);
        }
        if (node.value < high) {
            range(node.right, low, high, result);
        }
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(AuditNode node, long low, long high) {
        if (node == null) {
            return true;
        }
        if (node.value <= low || node.value >= high) {
            return false;
        }
        return isValid(node.left, low, node.value) && isValid(node.right, node.value, high);
    }

    void corruptForTest() {
        if (root != null && root.left != null) {
            root.left.value = root.value + 1000;
        }
    }
}
