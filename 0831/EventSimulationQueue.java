import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class EventSimulationQueue {
    public static final class Event {
        private final String id;
        private final long time;
        private final String type;
        private final long sequence;

        private Event(String id, long time, String type, long sequence) {
            this.id = id;
            this.time = time;
            this.type = type;
            this.sequence = sequence;
        }

        public String getId() {
            return id;
        }

        public long getTime() {
            return time;
        }

        public String getType() {
            return type;
        }

        public long getSequence() {
            return sequence;
        }

        @Override
        public String toString() {
            return time + "|" + type + "|" + sequence + "|" + id;
        }
    }

    private final PriorityQueue<Event> events = new PriorityQueue<>(
            Comparator.comparingLong(Event::getTime)
                    .thenComparingLong(Event::getSequence)
                    .thenComparing(Event::getId));
    private final Map<String, Event> eventsById = new HashMap<>();
    private long nextSequence = 1;

    public Event schedule(String id, long time, String type) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be blank");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("type cannot be blank");
        }
        if (eventsById.containsKey(id)) {
            throw new IllegalArgumentException("duplicate active event id: " + id);
        }

        Event event = new Event(id, time, type, nextSequence++);
        events.offer(event);
        eventsById.put(id, event);
        return event;
    }

    public boolean cancel(String eventId) {
        Event event = eventsById.remove(eventId);
        return event != null && events.remove(event);
    }

    public Event executeNext() {
        Event event = events.poll();
        if (event != null) {
            eventsById.remove(event.getId());
        }
        return event;
    }

    public List<Event> runAll() {
        List<Event> executionLog = new ArrayList<>();
        Event event;
        while ((event = executeNext()) != null) {
            executionLog.add(event);
        }
        return executionLog;
    }

    public int size() {
        return events.size();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    public static void main(String[] args) {
        EventSimulationQueue simulation = new EventSimulationQueue();
        simulation.schedule("E1", 30, "REPORT");
        simulation.schedule("E2", 10, "LOGIN");
        simulation.schedule("E3", 10, "PAYMENT");
        simulation.schedule("E4", 20, "TIMEOUT");
        simulation.schedule("E5", 10, "LOGOUT");

        System.out.println("CANCEL|E4|" + simulation.cancel("E4"));
        System.out.println("CANCEL|UNKNOWN|" + simulation.cancel("UNKNOWN"));
        System.out.println("EXECUTION_LOG");
        for (Event event : simulation.runAll()) {
            System.out.println(event);
        }
        System.out.println("EMPTY|" + simulation.isEmpty());
    }
}
