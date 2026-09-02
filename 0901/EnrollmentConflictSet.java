import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnrollmentConflictSet {
    public record EnrollmentKey(String studentId, String courseId) {
        public EnrollmentKey {
            studentId = normalize(studentId, "studentId").toUpperCase();
            courseId = normalize(courseId, "courseId").toUpperCase();
        }
    }

    private final Set<EnrollmentKey> accepted = new HashSet<>();
    private final Set<EnrollmentKey> duplicates = new HashSet<>();
    private final Map<String, Set<String>> coursesByStudent = new HashMap<>();
    private final Map<String, Set<String>> studentsByCourse = new HashMap<>();

    public boolean add(String studentId, String courseId) {
        EnrollmentKey key = new EnrollmentKey(studentId, courseId);
        if (!accepted.add(key)) {
            duplicates.add(key);
            return false;
        }

        coursesByStudent
                .computeIfAbsent(key.studentId(), ignored -> new HashSet<>())
                .add(key.courseId());
        studentsByCourse
                .computeIfAbsent(key.courseId(), ignored -> new HashSet<>())
                .add(key.studentId());
        return true;
    }

    public List<EnrollmentKey> duplicateRecords() {
        return duplicates.stream()
                .sorted(Comparator.comparing(EnrollmentKey::studentId)
                        .thenComparing(EnrollmentKey::courseId))
                .toList();
    }

    public Map<String, Set<String>> coursesByStudent() {
        Map<String, Set<String>> result = new LinkedHashMap<>();
        coursesByStudent.keySet().stream().sorted().forEach(studentId -> {
            Set<String> courses = new LinkedHashSet<>(
                    coursesByStudent.get(studentId).stream().sorted().toList());
            result.put(studentId, Collections.unmodifiableSet(courses));
        });
        return Collections.unmodifiableMap(result);
    }

    public Map<String, Integer> enrollmentCountByCourse() {
        Map<String, Integer> result = new LinkedHashMap<>();
        studentsByCourse.keySet().stream().sorted()
                .forEach(courseId -> result.put(courseId, studentsByCourse.get(courseId).size()));
        return result;
    }

    private static String normalize(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " 不可為空白");
        }
        return value.trim();
    }

    public static void main(String[] args) {
        EnrollmentConflictSet checker = new EnrollmentConflictSet();
        checker.add("S001", "DS101");
        checker.add("S001", "JAVA1");
        checker.add(" s001 ", "ds101");
        checker.add("S002", "DS101");
        checker.add("S002", "WEB01");
        checker.add("S002", "WEB01");

        System.out.println("重複紀錄=" + checker.duplicateRecords());
        System.out.println("每人課程=" + checker.coursesByStudent());
        System.out.println("每門課修課人數=" + checker.enrollmentCountByCourse());
    }
}
