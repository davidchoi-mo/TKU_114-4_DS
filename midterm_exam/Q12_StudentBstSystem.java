import java.util.ArrayList;
import java.util.List;

public class Q12_StudentBstSystem {
    public static class Student {
        private final int id;
        private final String name;
        private int score;

        public Student(int id, String name, int score) {
            if (id <= 0) {
                throw new IllegalArgumentException("id must be greater than 0");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("name must not be null or blank");
            }

            this.id = id;
            this.name = name;
            this.score = clampScore(score);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        private void setScore(int score) {
            this.score = clampScore(score);
        }

        private static int clampScore(int score) {
            return Math.max(0, Math.min(100, score));
        }

        @Override
        public String toString() {
            return id + "|" + name + "|" + score;
        }
    }

    private static class Node {
        private Student student;
        private Node left;
        private Node right;

        private Node(Student student) {
            this.student = student;
        }
    }

    private Node root;

    public boolean add(Student student) {
        if (student == null) {
            return false;
        }

        if (root == null) {
            root = new Node(student);
            return true;
        }

        Node current = root;
        while (true) {
            if (student.getId() == current.student.getId()) {
                return false;
            }

            if (student.getId() < current.student.getId()) {
                if (current.left == null) {
                    current.left = new Node(student);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(student);
                    return true;
                }
                current = current.right;
            }
        }
    }

    public Student find(int id) {
        Node current = root;

        while (current != null) {
            if (id == current.student.getId()) {
                return current.student;
            }
            current = id < current.student.getId() ? current.left : current.right;
        }

        return null;
    }

    public boolean updateScore(int id, int score) {
        Student student = find(id);
        if (student == null) {
            return false;
        }

        student.setScore(score);
        return true;
    }

    public boolean remove(int id) {
        Node parent = null;
        Node current = root;

        while (current != null && current.student.getId() != id) {
            parent = current;
            current = id < current.student.getId() ? current.left : current.right;
        }

        if (current == null) {
            return false;
        }

        if (current.left != null && current.right != null) {
            Node successorParent = current;
            Node successor = current.right;

            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            current.student = successor.student;
            parent = successorParent;
            current = successor;
        }

        Node replacement = current.left != null ? current.left : current.right;

        if (parent == null) {
            root = replacement;
        } else if (parent.left == current) {
            parent.left = replacement;
        } else {
            parent.right = replacement;
        }

        return true;
    }

    public List<Student> studentsBetween(int lowId, int highId) {
        List<Student> result = new ArrayList<>();
        if (lowId > highId) {
            return result;
        }

        collectBetween(root, lowId, highId, result);
        return result;
    }

    private void collectBetween(Node node, int lowId, int highId, List<Student> result) {
        if (node == null) {
            return;
        }

        int id = node.student.getId();
        if (lowId < id) {
            collectBetween(node.left, lowId, highId, result);
        }
        if (lowId <= id && id <= highId) {
            result.add(node.student);
        }
        if (id < highId) {
            collectBetween(node.right, lowId, highId, result);
        }
    }

    public List<Student> inorder() {
        List<Student> result = new ArrayList<>();
        collectInorder(root, result);
        return result;
    }

    private void collectInorder(Node node, List<Student> result) {
        if (node == null) {
            return;
        }

        collectInorder(node.left, result);
        result.add(node.student);
        collectInorder(node.right, result);
    }
}
