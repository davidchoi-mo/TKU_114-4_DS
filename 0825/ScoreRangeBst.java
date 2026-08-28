import java.util.ArrayList;
import java.util.List;

public class ScoreRangeBst {
    static final class StudentScore {
        final int studentId;
        final String name;
        final int score;

        StudentScore(int studentId, String name, int score) {
            if (score < 0 || score > 100) {
                throw new IllegalArgumentException("score 必須介於 0 到 100");
            }
            this.studentId = studentId;
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return "score=" + score + ", studentId=" + studentId + ", name=" + name;
        }
    }

    private static final class Node {
        StudentScore data;
        Node left;
        Node right;

        Node(StudentScore data) {
            this.data = data;
        }
    }

    private Node root;
    private int size;

    public boolean add(StudentScore studentScore) {
        if (studentScore == null) {
            throw new IllegalArgumentException("studentScore 不得為 null");
        }
        if (root == null) {
            root = new Node(studentScore);
            size = 1;
            return true;
        }

        Node current = root;
        while (true) {
            int comparison = compare(studentScore, current.data);
            if (comparison == 0) {
                return false;
            }
            if (comparison < 0) {
                if (current.left == null) {
                    current.left = new Node(studentScore);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(studentScore);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    public StudentScore find(int score, int studentId) {
        Node current = root;
        StudentScore key = new StudentScore(studentId, "", score);
        while (current != null) {
            int comparison = compare(key, current.data);
            if (comparison == 0) {
                return current.data;
            }
            current = comparison < 0 ? current.left : current.right;
        }
        return null;
    }

    private static int compare(StudentScore first, StudentScore second) {
        int scoreComparison = Integer.compare(first.score, second.score);
        if (scoreComparison != 0) {
            return scoreComparison;
        }
        return Integer.compare(first.studentId, second.studentId);
    }

    public List<StudentScore> range(int low, int high) {
        List<StudentScore> result = new ArrayList<>();
        if (low <= high) {
            range(root, low, high, result);
        }
        return result;
    }

    private void range(Node node, int low, int high, List<StudentScore> result) {
        if (node == null) {
            return;
        }

        if (node.data.score >= low) {
            range(node.left, low, high, result);
        }
        if (node.data.score >= low && node.data.score <= high) {
            result.add(node.data);
        }
        if (node.data.score <= high) {
            range(node.right, low, high, result);
        }
    }

    public void printRange(int low, int high) {
        System.out.println("分數範圍 [" + low + ", " + high + "]:");
        if (low > high) {
            System.out.println("  無效範圍：low 不得大於 high");
            return;
        }
        List<StudentScore> result = range(low, high);
        if (result.isEmpty()) {
            System.out.println("  無資料");
            return;
        }
        for (StudentScore studentScore : result) {
            System.out.println("  " + studentScore);
        }
    }

    public int size() {
        return size;
    }

    public boolean isValid() {
        return isValid(root, null, null);
    }

    private boolean isValid(Node node, StudentScore minExclusive, StudentScore maxExclusive) {
        if (node == null) {
            return true;
        }
        if ((minExclusive != null && compare(node.data, minExclusive) <= 0)
                || (maxExclusive != null && compare(node.data, maxExclusive) >= 0)) {
            return false;
        }
        return isValid(node.left, minExclusive, node.data)
                && isValid(node.right, node.data, maxExclusive);
    }

    public static void main(String[] args) {
        ScoreRangeBst scores = new ScoreRangeBst();
        System.out.println("新增 Alice 88: "
                + scores.add(new StudentScore(1001, "Alice", 88)));
        System.out.println("新增 Bob 75: "
                + scores.add(new StudentScore(1002, "Bob", 75)));
        System.out.println("新增 Carol 92: "
                + scores.add(new StudentScore(1003, "Carol", 92)));
        System.out.println("新增 David 88（同分）: "
                + scores.add(new StudentScore(1004, "David", 88)));
        System.out.println("新增 Eve 60: "
                + scores.add(new StudentScore(1005, "Eve", 60)));
        System.out.println("重複 key (88, 1001): "
                + scores.add(new StudentScore(1001, "Duplicate", 88)));

        scores.printRange(75, 90);
        scores.printRange(95, 80);
        System.out.println("查詢 (88, 1004): " + scores.find(88, 1004));
        System.out.println("size=" + scores.size() + ", valid=" + scores.isValid());
    }
}
