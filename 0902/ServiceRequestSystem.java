import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ServiceRequestSystem {
    public static final class ServiceRequest
            implements Comparable<ServiceRequest> {
        private final String id;
        private final String description;
        private final int priority;
        private final long sequence;

        private ServiceRequest(String id, String description,
                               int priority, long sequence) {
            this.id = id;
            this.description = description;
            this.priority = priority;
            this.sequence = sequence;
        }

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(ServiceRequest other) {
            int byPriority = Integer.compare(other.priority, priority);
            if (byPriority != 0) {
                return byPriority;
            }
            return Long.compare(sequence, other.sequence);
        }

        @Override
        public String toString() {
            return String.format("%s(priority=%d, description=%s)",
                    id, priority, description);
        }
    }

    private final Map<String, ServiceRequest> requestsById = new HashMap<>();
    private final PriorityQueue<ServiceRequest> requestsByPriority
            = new PriorityQueue<>();
    private long nextSequence = 0;

    public boolean submitRequest(String id, String description, int priority) {
        if (id == null || id.isBlank() || description == null
                || requestsById.containsKey(id)) {
            return false;
        }

        ServiceRequest request = new ServiceRequest(
                id, description, priority, nextSequence++);
        requestsById.put(id, request);
        requestsByPriority.offer(request);
        return true;
    }

    public ServiceRequest findById(String id) {
        return id == null ? null : requestsById.get(id);
    }

    public ServiceRequest peekNextRequest() {
        return requestsByPriority.peek();
    }

    public ServiceRequest processNextRequest() {
        ServiceRequest request = requestsByPriority.poll();
        if (request != null) {
            requestsById.remove(request.id);
        }
        return request;
    }

    public boolean cancelRequest(String id) {
        if (id == null) {
            return false;
        }

        ServiceRequest request = requestsById.remove(id);
        if (request == null) {
            return false;
        }

        boolean removedFromQueue = requestsByPriority.remove(request);
        if (!removedFromQueue) {
            requestsById.put(id, request);
            return false;
        }
        return true;
    }

    public int pendingCount() {
        return requestsById.size();
    }

    public boolean isConsistent() {
        if (requestsById.size() != requestsByPriority.size()) {
            return false;
        }
        return requestsById.values().containsAll(requestsByPriority)
                && requestsByPriority.containsAll(requestsById.values());
    }

    public List<ServiceRequest> prioritySnapshot() {
        PriorityQueue<ServiceRequest> copy
                = new PriorityQueue<>(requestsByPriority);
        List<ServiceRequest> result = new ArrayList<>();
        while (!copy.isEmpty()) {
            result.add(copy.poll());
        }
        return result;
    }

    public static void main(String[] args) {
        ServiceRequestSystem system = new ServiceRequestSystem();

        System.out.println("=== Service requests ===");
        System.out.println("submit R101 = "
                + system.submitRequest("R101", "Reset password", 2));
        System.out.println("submit R102 = "
                + system.submitRequest("R102", "Server unavailable", 5));
        System.out.println("submit R103 = "
                + system.submitRequest("R103", "Install software", 2));
        System.out.println("duplicate R101 = "
                + system.submitRequest("R101", "Duplicate", 9));

        System.out.println("priority order = " + system.prioritySnapshot());
        System.out.println("find R103 = " + system.findById("R103"));
        System.out.println("cancel R102 = " + system.cancelRequest("R102"));
        System.out.println("cancel missing = " + system.cancelRequest("R999"));
        System.out.println("consistent after cancel = " + system.isConsistent());

        while (system.pendingCount() > 0) {
            System.out.println("process = " + system.processNextRequest());
        }
        System.out.println("process empty = " + system.processNextRequest());
        System.out.println("consistent when empty = " + system.isConsistent());
    }
}
