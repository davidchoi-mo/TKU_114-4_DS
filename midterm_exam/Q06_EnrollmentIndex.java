import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Q06_EnrollmentIndex {

    private final Map<String, Set<String>> enrollment = new TreeMap<>();

    public boolean enroll(String courseCode, String studentId) {
        if (isBlank(courseCode) || isBlank(studentId)) {
            return false;
        }

        Set<String> students =
                enrollment.computeIfAbsent(courseCode, key -> new TreeSet<>());

        return students.add(studentId);
    }

    public boolean drop(String courseCode, String studentId) {
        if (isBlank(courseCode) || isBlank(studentId)) {
            return false;
        }

        Set<String> students = enrollment.get(courseCode);

        if (students == null || !students.remove(studentId)) {
            return false;
        }

        if (students.isEmpty()) {
            enrollment.remove(courseCode);
        }

        return true;
    }

    public int courseSize(String courseCode) {
        if (isBlank(courseCode)) {
            return 0;
        }

        Set<String> students = enrollment.get(courseCode);
        return students == null ? 0 : students.size();
    }

    public List<String> studentsOf(String courseCode) {
        if (isBlank(courseCode)) {
            return new ArrayList<>();
        }

        Set<String> students = enrollment.get(courseCode);

        if (students == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(students);
    }

    public List<String> coursesOf(String studentId) {
        List<String> courses = new ArrayList<>();

        if (isBlank(studentId)) {
            return courses;
        }

        for (Map.Entry<String, Set<String>> entry : enrollment.entrySet()) {
            if (entry.getValue().contains(studentId)) {
                courses.add(entry.getKey());
            }
        }

        return courses;
    }

    public Map<String, Integer> summary() {
        Map<String, Integer> result = new TreeMap<>();

        for (Map.Entry<String, Set<String>> entry : enrollment.entrySet()) {
            result.put(entry.getKey(), entry.getValue().size());
        }

        return result;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}