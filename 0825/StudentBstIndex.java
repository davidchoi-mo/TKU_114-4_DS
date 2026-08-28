import java.util.ArrayList;
import java.util.List;

public class StudentBstIndex {
    static final class Student {
        final int studentId;
        final String name;
        final String department;

        Student(int studentId, String name, String department) {
            this.studentId = studentId;
            this.name = name;
            this.department = department;
        }

        @Override
        public String toString() {
            return studentId + " " + name + " (" + department + ")";
        }
    }

    private static final class Node {
        Student data;
        Node left;
        Node right;

        Node(Student data) {
            this.data = data;
        }
    }

    private Node root;
    private int size;

    public boolean insert(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("student 不得為 null");
        }

        if (root == null) {
            root = new Node(student);
            size = 1;
            return true;
        }

        Node current = root;
        while (true) {
            if (student.studentId == current.data.studentId) {
                return false;
            }
            if (student.studentId < current.data.studentId) {
                if (current.left == null) {
                    current.left = new Node(student);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(student);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    public Student search(int studentId) {
        Node current = root;
        while (current != null) {
            if (studentId == current.data.studentId) {
                return current.data;
            }
            current = studentId < current.data.studentId
                    ? current.left
                    : current.right;
        }
        return null;
    }

    public boolean delete(int studentId) {
        if (search(studentId) == null) {
            return false;
        }
        root = delete(root, studentId);
        size--;
        return true;
    }

    private Node delete(Node node, int studentId) {
        if (node == null) {
            return null;
        }
        if (studentId < node.data.studentId) {
            node.left = delete(node.left, studentId);
        } else if (studentId > node.data.studentId) {
            node.right = delete(node.right, studentId);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            Node successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = delete(node.right, successor.data.studentId);
        }
        return node;
    }

    private Node minimumNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public int size() {
        return size;
    }

    public List<Student> inorderList() {
        List<Student> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(Node node, List<Student> result) {
        if (node == null) {
            return;
        }
        inorder(node.left, result);
        result.add(node.data);
        inorder(node.right, result);
    }

    public void printInorder() {
        System.out.println("Inorder（依學號排序）:");
        for (Student student : inorderList()) {
            System.out.println("  " + student);
        }
    }

    public boolean isValid() {
        return isValid(root, null, null);
    }

    private boolean isValid(Node node, Long minExclusive, Long maxExclusive) {
        if (node == null) {
            return true;
        }
        long key = node.data.studentId;
        if ((minExclusive != null && key <= minExclusive)
                || (maxExclusive != null && key >= maxExclusive)) {
            return false;
        }
        return isValid(node.left, minExclusive, key)
                && isValid(node.right, key, maxExclusive);
    }

    public static void main(String[] args) {
        StudentBstIndex index = new StudentBstIndex();

        System.out.println("新增 11400300: "
                + index.insert(new Student(11400300, "王小明", "資訊管理學系")));
        System.out.println("新增 11400100: "
                + index.insert(new Student(11400100, "陳怡君", "資訊管理學系")));
        System.out.println("新增 11400500: "
                + index.insert(new Student(11400500, "林志豪", "資訊工程學系")));
        System.out.println("新增 11400200: "
                + index.insert(new Student(11400200, "李佳蓉", "企業管理學系")));
        System.out.println("重複新增 11400100: "
                + index.insert(new Student(11400100, "重複資料", "測試系")));

        index.printInorder();
        System.out.println("查詢 11400200: " + index.search(11400200));
        System.out.println("查詢 99999999: " + index.search(99999999));
        System.out.println("刪除 11400300: " + index.delete(11400300));
        System.out.println("再次刪除 11400300: " + index.delete(11400300));
        index.printInorder();
        System.out.println("size=" + index.size() + ", valid=" + index.isValid());
    }
}
