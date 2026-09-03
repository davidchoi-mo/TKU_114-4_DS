import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Q05_StudentHashIndex {
    private final Map<String, Set<String>> studentCourses = new HashMap<>();
    private final Map<String, Set<String>> courseStudents = new HashMap<>();
    private int enrollmentCount;

    public boolean enroll(String studentId, String courseId) {
        String student = normalize(studentId);
        String course = normalize(courseId);

        if (student == null || course == null) {
            return false;
        }

        Set<String> courses = studentCourses.computeIfAbsent(
                student, key -> new HashSet<>());

        if (!courses.add(course)) {
            return false;
        }

        courseStudents.computeIfAbsent(course, key -> new HashSet<>())
                .add(student);
        enrollmentCount++;
        return true;
    }

    public boolean drop(String studentId, String courseId) {
        String student = normalize(studentId);
        String course = normalize(courseId);

        if (student == null || course == null) {
            return false;
        }

        Set<String> courses = studentCourses.get(student);
        if (courses == null || !courses.remove(course)) {
            return false;
        }

        if (courses.isEmpty()) {
            studentCourses.remove(student);
        }

        Set<String> students = courseStudents.get(course);
        if (students != null) {
            students.remove(student);
            if (students.isEmpty()) {
                courseStudents.remove(course);
            }
        }

        enrollmentCount--;
        return true;
    }

    public Set<String> coursesOf(String studentId) {
        String student = normalize(studentId);
        if (student == null) {
            return immutableCopy(null);
        }
        return immutableCopy(studentCourses.get(student));
    }

    public Set<String> studentsIn(String courseId) {
        String course = normalize(courseId);
        if (course == null) {
            return immutableCopy(null);
        }
        return immutableCopy(courseStudents.get(course));
    }

    public int enrollmentCount() {
        return enrollmentCount;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.trim().toUpperCase(Locale.ROOT);
        return normalized.isEmpty() ? null : normalized;
    }

    private Set<String> immutableCopy(Set<String> source) {
        Set<String> copy = source == null
                ? new HashSet<>()
                : new HashSet<>(source);
        return Collections.unmodifiableSet(copy);
    }
}
