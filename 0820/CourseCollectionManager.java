import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CourseCollectionManager {
    public static void main(String[] args) {
        CourseManager manager = new CourseManager();

        CourseEnrollment amy = new CourseEnrollment("S101", "Amy", 95);
        amy.addTag("Java");
        amy.addTag("   "); 

        CourseEnrollment ben = new CourseEnrollment("S102", "Ben", 82);
        ben.addTag("Data");

        CourseEnrollment cara = new CourseEnrollment("S103", "Cara", 82);
        cara.addTag("JAVA");
        cara.addTag("Data");

        CourseEnrollment david = new CourseEnrollment("S104", "David", 74);
        david.addTag("Web");

        CourseEnrollment eva = new CourseEnrollment("S105", "Eva", 63);
        eva.addTag("Java");

        CourseEnrollment frank = new CourseEnrollment("S106", "Frank", 58);
        frank.addTag("Database");

        System.out.println("新增 S101 = " + manager.enroll(amy));
        System.out.println("新增 S102 = " + manager.enroll(ben));
        System.out.println("新增 S103 = " + manager.enroll(cara));
        System.out.println("新增 S104 = " + manager.enroll(david));
        System.out.println("新增 S105 = " + manager.enroll(eva));
        System.out.println("新增 S106 = " + manager.enroll(frank));
        System.out.println("重複新增 S101 = " + manager.enroll(
                new CourseEnrollment("S101", "Duplicate Amy", 100)));

        System.out.println("\n更新 S105 成績 = "
                + manager.updateScore("S105", 88));
        System.out.println("更新不存在學生 = "
                + manager.updateScore("S999", 90));
        System.out.println("Java 標籤 = " + manager.findByTag("java"));
        System.out.println("成績分布 = " + manager.scoreDistribution());
        System.out.println("前三名 = " + manager.top(3));
        System.out.println("前 99 名 = " + manager.top(99));

        manager.removeBelow(60);
        System.out.println("\n移除 60 分以下後 = " + manager.top(99));
        System.out.println("List、Set、Map 保持一致 = "
                + manager.isConsistent());
        System.out.println("移除後成績分布 = "
                + manager.scoreDistribution());
    }
}

class CourseEnrollment {
    private final String studentId;
    private final String name;
    private int score;
    private final Set<String> tags = new LinkedHashSet<>();

    CourseEnrollment(String studentId, String name, int score) {
        this.studentId = studentId;
        this.name = name;
        setScore(score);
    }

    String getStudentId() {
        return studentId;
    }

    int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = Math.max(0, Math.min(100, score));
    }

    void addTag(String tag) {
        if (tag != null && !tag.isBlank()) {
            tags.add(tag.trim().toLowerCase(Locale.ROOT));
        }
    }

    boolean hasTag(String tag) {
        return tag != null
                && tags.contains(tag.trim().toLowerCase(Locale.ROOT));
    }

    @Override
    public String toString() {
        return studentId + " " + name + " score=" + score + " tags=" + tags;
    }
}

class CourseManager {
    private final List<CourseEnrollment> order = new ArrayList<>();
    private final Set<String> registeredIds = new HashSet<>();
    private final Map<String, CourseEnrollment> byId = new HashMap<>();

    boolean enroll(CourseEnrollment enrollment) {
        if (enrollment == null
                || !registeredIds.add(enrollment.getStudentId())) {
            return false;
        }
        order.add(enrollment);
        byId.put(enrollment.getStudentId(), enrollment);
        return true;
    }

    boolean updateScore(String studentId, int score) {
        CourseEnrollment enrollment = byId.get(studentId);
        if (enrollment == null) {
            return false;
        }
        enrollment.setScore(score);
        return true;
    }

    List<CourseEnrollment> findByTag(String tag) {
        List<CourseEnrollment> result = new ArrayList<>();
        if (tag == null || tag.isBlank()) {
            return result;
        }
        for (CourseEnrollment enrollment : order) {
            if (enrollment.hasTag(tag)) {
                result.add(enrollment);
            }
        }
        return result;
    }

    Map<String, Integer> scoreDistribution() {
        Map<String, Integer> distribution = new LinkedHashMap<>();
        distribution.put("A", 0);
        distribution.put("B", 0);
        distribution.put("C", 0);
        distribution.put("D", 0);
        distribution.put("F", 0);

        for (CourseEnrollment enrollment : order) {
            String grade = gradeOf(enrollment.getScore());
            distribution.put(grade, distribution.get(grade) + 1);
        }
        return distribution;
    }

    private String gradeOf(int score) {
        if (score >= 90) {
            return "A";
        }
        if (score >= 80) {
            return "B";
        }
        if (score >= 70) {
            return "C";
        }
        if (score >= 60) {
            return "D";
        }
        return "F";
    }

    List<CourseEnrollment> top(int count) {
        List<CourseEnrollment> ranking = new ArrayList<>(order);
        ranking.sort(Comparator.comparingInt(CourseEnrollment::getScore)
                .reversed()
                .thenComparing(CourseEnrollment::getStudentId));

        if (count <= 0) {
            return new ArrayList<>();
        }
        int endIndex = Math.min(count, ranking.size());
        return new ArrayList<>(ranking.subList(0, endIndex));
    }

    void removeBelow(int minimum) {
        Iterator<CourseEnrollment> iterator = order.iterator();
        while (iterator.hasNext()) {
            CourseEnrollment enrollment = iterator.next();
            if (enrollment.getScore() < minimum) {
                iterator.remove();
                registeredIds.remove(enrollment.getStudentId());
                byId.remove(enrollment.getStudentId());
            }
        }
    }

    boolean isConsistent() {
        if (order.size() != registeredIds.size()
                || order.size() != byId.size()) {
            return false;
        }
        for (CourseEnrollment enrollment : order) {
            if (!registeredIds.contains(enrollment.getStudentId())
                    || byId.get(enrollment.getStudentId()) != enrollment) {
                return false;
            }
        }
        return true;
    }
}
