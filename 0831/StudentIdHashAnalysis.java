import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class StudentIdHashAnalysis {
    public static final class Analysis {
        private final int bucketCount;
        private final int[] bucketSizes;
        private final int studentCount;
        private final int ignoredNullCount;
        private final int collisionCount;
        private final int longestChain;
        private final double averageChainLength;
        private final double averageNonEmptyChainLength;

        private Analysis(
                int bucketCount,
                int[] bucketSizes,
                int studentCount,
                int ignoredNullCount,
                int collisionCount,
                int longestChain,
                double averageChainLength,
                double averageNonEmptyChainLength) {
            this.bucketCount = bucketCount;
            this.bucketSizes = Arrays.copyOf(bucketSizes, bucketSizes.length);
            this.studentCount = studentCount;
            this.ignoredNullCount = ignoredNullCount;
            this.collisionCount = collisionCount;
            this.longestChain = longestChain;
            this.averageChainLength = averageChainLength;
            this.averageNonEmptyChainLength = averageNonEmptyChainLength;
        }

        public int getBucketCount() {
            return bucketCount;
        }

        public int[] getBucketSizes() {
            return Arrays.copyOf(bucketSizes, bucketSizes.length);
        }

        public int getStudentCount() {
            return studentCount;
        }

        public int getIgnoredNullCount() {
            return ignoredNullCount;
        }

        public int getCollisionCount() {
            return collisionCount;
        }

        public int getLongestChain() {
            return longestChain;
        }

        public double getAverageChainLength() {
            return averageChainLength;
        }

        public double getAverageNonEmptyChainLength() {
            return averageNonEmptyChainLength;
        }

        public String toReport() {
            StringBuilder report = new StringBuilder();
            report.append("bucketCount=").append(bucketCount).append(System.lineSeparator());
            for (int i = 0; i < bucketSizes.length; i++) {
                report.append("bucket[").append(i).append("]=")
                        .append(bucketSizes[i]).append(System.lineSeparator());
            }
            report.append("studentCount=").append(studentCount).append(System.lineSeparator())
                    .append("ignoredNullCount=").append(ignoredNullCount)
                    .append(System.lineSeparator())
                    .append("collisions=").append(collisionCount).append(System.lineSeparator())
                    .append("longestChain=").append(longestChain).append(System.lineSeparator())
                    .append(String.format(Locale.ROOT,
                            "averageChainLength=%.2f%n", averageChainLength))
                    .append(String.format(Locale.ROOT,
                            "averageNonEmptyChainLength=%.2f",
                            averageNonEmptyChainLength));
            return report.toString();
        }
    }

    public static Analysis analyze(List<Integer> studentIds, int bucketCount) {
        if (studentIds == null) {
            throw new IllegalArgumentException("studentIds cannot be null");
        }
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount must be positive");
        }

        int[] bucketSizes = new int[bucketCount];
        int validCount = 0;
        int ignoredNullCount = 0;
        int collisions = 0;

        for (Integer studentId : studentIds) {
            if (studentId == null) {
                ignoredNullCount++;
                continue;
            }
            int index = Math.floorMod(Integer.hashCode(studentId), bucketCount);
            if (bucketSizes[index] > 0) {
                collisions++;
            }
            bucketSizes[index]++;
            validCount++;
        }

        int longestChain = 0;
        int nonEmptyBuckets = 0;
        for (int chainLength : bucketSizes) {
            longestChain = Math.max(longestChain, chainLength);
            if (chainLength > 0) {
                nonEmptyBuckets++;
            }
        }

        double averageChainLength = (double) validCount / bucketCount;
        double averageNonEmptyChainLength = nonEmptyBuckets == 0
                ? 0.0 : (double) validCount / nonEmptyBuckets;

        return new Analysis(
                bucketCount,
                bucketSizes,
                validCount,
                ignoredNullCount,
                collisions,
                longestChain,
                averageChainLength,
                averageNonEmptyChainLength);
    }

    public static String compare(
            List<Integer> studentIds, int firstBucketCount, int secondBucketCount) {
        Analysis first = analyze(studentIds, firstBucketCount);
        Analysis second = analyze(studentIds, secondBucketCount);
        return "=== FIRST ===" + System.lineSeparator()
                + first.toReport() + System.lineSeparator()
                + "=== SECOND ===" + System.lineSeparator()
                + second.toReport() + System.lineSeparator()
                + "=== SUMMARY ===" + System.lineSeparator()
                + "fewerCollisions="
                + describeBetterCollisionCount(first, second);
    }

    private static String describeBetterCollisionCount(Analysis first, Analysis second) {
        if (first.getCollisionCount() == second.getCollisionCount()) {
            return "tie";
        }
        return first.getCollisionCount() < second.getCollisionCount()
                ? String.valueOf(first.getBucketCount())
                : String.valueOf(second.getBucketCount());
    }

    public static void main(String[] args) {
        List<Integer> studentIds = Arrays.asList(
                11001, 11006, 11011, 11016, 11021,
                12003, 12008, 12013, 13002, 13007,
                14004, 14009, -15, null);

        System.out.println(compare(studentIds, 5, 11));
    }
}
