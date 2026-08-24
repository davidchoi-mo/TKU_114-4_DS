import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EnrollmentSetSystem {
    public static void main(String[] args) {
        Set<Enrollment> enrollments = new HashSet<>();

        Enrollment amyJava = new Enrollment("S101", "JAVA", "Amy");
        Enrollment amyDatabase = new Enrollment("S101", "DB", "Amy");
        Enrollment benJava = new Enrollment("S102", "JAVA", "Ben");

        System.out.println("新增 Amy/JAVA = " + enrollments.add(amyJava));
        System.out.println("新增 Amy/DB = " + enrollments.add(amyDatabase));
        System.out.println("新增 Ben/JAVA = " + enrollments.add(benJava));

        Enrollment duplicateAmyJava =
                new Enrollment("S101", "JAVA", "Amy（重複物件）");
        System.out.println("重複新增 Amy/JAVA = "
                + enrollments.add(duplicateAmyJava));

        Enrollment sameIdentity =
                new Enrollment("S101", "JAVA", "姓名不影響身分");
        System.out.println("以新物件 contains Amy/JAVA = "
                + enrollments.contains(sameIdentity));
        System.out.println("以新物件 remove Amy/JAVA = "
                + enrollments.remove(sameIdentity));
        System.out.println("再次取消 Amy/JAVA = "
                + enrollments.remove(sameIdentity));

        System.out.println("目前報名集合：");
        for (Enrollment enrollment : enrollments) {
            System.out.println(enrollment);
        }
    }
}

class Enrollment {
    private final String studentId;
    private final String courseCode;
    private final String studentName;

    Enrollment(String studentId, String courseCode, String studentName) {
        this.studentId = Objects.requireNonNull(studentId).trim();
        this.courseCode = Objects.requireNonNull(courseCode).trim();
        this.studentName = Objects.requireNonNull(studentName).trim();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Enrollment)) {
            return false;
        }
        Enrollment other = (Enrollment) object;
        return studentId.equals(other.studentId)
                && courseCode.equals(other.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode);
    }

    @Override
    public String toString() {
        return studentId + " " + studentName + " -> " + courseCode;
    }
}
