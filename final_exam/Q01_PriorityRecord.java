import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Q01_PriorityRecord {

    public record Job(String id, int priority, long sequence) {}

    public static List<String> processOrder(List<Job> jobs) {
        List<String> result = new ArrayList<>();

        if (jobs == null || jobs.isEmpty()) {
            return result;
        }

        Comparator<Job> comparator =
                Comparator.comparingInt(Job::priority)
                          .thenComparingLong(Job::sequence)
                          .thenComparing(Job::id);

        PriorityQueue<Job> queue = new PriorityQueue<>(comparator);

        for (Job job : jobs) {
            if (job != null) {
                queue.offer(job);
            }
        }

        while (!queue.isEmpty()) {
            result.add(queue.poll().id());
        }

        return result;
    }
}