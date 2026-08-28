import java.util.ArrayList;
import java.util.List;

public class CourseBstIndex {
    public static void main(String[] args) {
        CourseBst index = new CourseBst();
        System.out.println("add=" + index.add(new Course("CS301", "Data Structures", 3)));
        System.out.println("add=" + index.add(new Course("CS101", "Intro to Programming", 3)));
        System.out.println("add=" + index.add(new Course("CS450", "Capstone Project", 6)));
        System.out.println("add=" + index.add(new Course("CS220", "Discrete Math", 4)));
        System.out.println("invalidCredit=" + index.add(new Course("CS999", "Bad Credit Course", 9)));
        System.out.println("duplicate=" + index.add(new Course("CS101", "Other Title", 2)));

        System.out.println("find=" + index.find("CS220"));
        System.out.println("updateCredit=" + index.updateCredit("CS220", 5));
        System.out.println("updateCreditInvalid=" + index.updateCredit("CS220", 10));
        System.out.println("find=" + index.find("CS220"));

        System.out.println("remove=" + index.remove("CS301"));
        System.out.println("missing=" + index.remove("CS000"));

        System.out.println("range=" + index.codeRange("CS150", "CS500"));

        System.out.println("-- sortedReport --");
        for (Course course : index.sortedReport()) {
            System.out.println(course);
        }
    }
}

class Course {
    String code;
    String title;
    int credit;

    Course(String code, String title, int credit) {
        this.code = code;
        this.title = title;
        this.credit = credit;
    }

    @Override
    public String toString() {
        return code + " " + title + " credit=" + credit;
    }
}

class CourseNode {
    Course data;
    CourseNode left;
    CourseNode right;

    CourseNode(Course data) {
        this.data = data;
    }
}

class CourseBst {
    private CourseNode root;

    private boolean validCredit(int credit) {
        return credit >= 1 && credit <= 6;
    }

    boolean add(Course course) {
        if (!validCredit(course.credit)) {
            return false;
        }
        if (root == null) {
            root = new CourseNode(course);
            return true;
        }

        CourseNode current = root;
        while (true) {
            int cmp = course.code.compareTo(current.data.code);
            if (cmp == 0) {
                return false;
            }
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new CourseNode(course);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new CourseNode(course);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Course find(String code) {
        CourseNode current = root;
        while (current != null) {
            int cmp = code.compareTo(current.data.code);
            if (cmp == 0) {
                return current.data;
            }
            current = cmp < 0 ? current.left : current.right;
        }
        return null;
    }

    boolean updateCredit(String code, int newCredit) {
        if (!validCredit(newCredit)) {
            return false;
        }
        Course course = find(code);
        if (course == null) {
            return false;
        }
        course.credit = newCredit;
        return true;
    }

    boolean remove(String code) {
        if (find(code) == null) {
            return false;
        }
        root = remove(root, code);
        return true;
    }

    private CourseNode remove(CourseNode node, String code) {
        int cmp = code.compareTo(node.data.code);
        if (cmp < 0) {
            node.left = remove(node.left, code);
        } else if (cmp > 0) {
            node.right = remove(node.right, code);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            CourseNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = remove(node.right, successor.data.code);
        }
        return node;
    }

    private CourseNode minimumNode(CourseNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    List<Course> codeRange(String low, String high) {
        List<Course> result = new ArrayList<>();
        if (low.compareTo(high) <= 0) {
            codeRange(root, low, high, result);
        }
        return result;
    }

    private void codeRange(CourseNode node, String low, String high, List<Course> result) {
        if (node == null) {
            return;
        }
        if (low.compareTo(node.data.code) < 0) {
            codeRange(node.left, low, high, result);
        }
        if (low.compareTo(node.data.code) <= 0 && node.data.code.compareTo(high) <= 0) {
            result.add(node.data);
        }
        if (node.data.code.compareTo(high) < 0) {
            codeRange(node.right, low, high, result);
        }
    }

    List<Course> sortedReport() {
        List<Course> result = new ArrayList<>();
        sortedReport(root, result);
        return result;
    }

    private void sortedReport(CourseNode node, List<Course> result) {
        if (node == null) {
            return;
        }
        sortedReport(node.left, result);
        result.add(node.data);
        sortedReport(node.right, result);
    }
}
