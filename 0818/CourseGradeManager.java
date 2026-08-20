class CourseGrade {
    private final String studentId;
    private final String name;
    private final int regularScore;
    private final int midtermScore;
    private final int finalExamScore;
    private final int attendanceScore;

    CourseGrade(String studentId, String name, int regularScore,
                int midtermScore, int finalExamScore, int attendanceScore) {
        this.studentId = normalize(studentId, "UNKNOWN");
        this.name = normalize(name, "Unknown");
        this.regularScore = limitScore(regularScore);
        this.midtermScore = limitScore(midtermScore);
        this.finalExamScore = limitScore(finalExamScore);
        this.attendanceScore = limitScore(attendanceScore);
    }

    private static String normalize(String value, String defaultValue) {
        return value == null || value.trim().isEmpty()
                ? defaultValue : value.trim();
    }

    private static int limitScore(int score) {
        return Math.max(0, Math.min(100, score));
    }

    double calculateFinalScore() {
        return regularScore * 0.50
                + midtermScore * 0.20
                + finalExamScore * 0.20
                + attendanceScore * 0.10;
    }

    String getLevel() {
        double score = calculateFinalScore();
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

    @Override
    public String toString() {
        return String.format(
                "%s %s｜平時=%d 期中=%d 期末=%d 出席=%d｜總分=%.1f 等級=%s",
                studentId, name, regularScore, midtermScore,
                finalExamScore, attendanceScore, calculateFinalScore(), getLevel());
    }
}

public class CourseGradeManager {
    public static void main(String[] args) {
        CourseGrade[] grades = {
            new CourseGrade("S101", "Amy", 92, 88, 90, 100),
            new CourseGrade("S102", "Ben", 70, 65, 68, 90),
            new CourseGrade("S103", "Cara", 85, 91, 88, 95),
            new CourseGrade("S104", "David", 45, 55, 50, 70),
            new CourseGrade("S105", "Eva", 78, 75, 82, 100)
        };

        double total = 0;
        CourseGrade highest = grades[0];

        System.out.println("所有成績：");
        for (CourseGrade grade : grades) {
            System.out.println(grade);
            total += grade.calculateFinalScore();
            if (grade.calculateFinalScore() > highest.calculateFinalScore()) {
                highest = grade;
            }
        }

        System.out.printf("%n全班平均：%.1f%n", total / grades.length);
        System.out.println("最高分：" + highest);
        System.out.println("不及格名單：");
        for (CourseGrade grade : grades) {
            if (grade.calculateFinalScore() < 60) {
                System.out.println(grade);
            }
        }
    }
}
