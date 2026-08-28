import java.util.ArrayList;
import java.util.List;

public class Q05_BoundedBox<T extends Comparable<T>> {

    private final int capacity;
    private final List<T> values;

    public Q05_BoundedBox(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException(
                "Capacity must be greater than 0."
            );
        }

        this.capacity = capacity;
        this.values = new ArrayList<>();
    }

    public boolean add(T value) {
        if (value == null || isFull()) {
            return false;
        }

        values.add(value);
        return true;
    }

    public int size() {
        return values.size();
    }

    public boolean isFull() {
        return values.size() >= capacity;
    }

    public T minimum() {
        if (values.isEmpty()) {
            return null;
        }

        T minimum = values.get(0);

        for (int i = 1; i < values.size(); i++) {
            T current = values.get(i);

            if (current.compareTo(minimum) < 0) {
                minimum = current;
            }
        }

        return minimum;
    }

    public T maximum() {
        if (values.isEmpty()) {
            return null;
        }

        T maximum = values.get(0);

        for (int i = 1; i < values.size(); i++) {
            T current = values.get(i);

            if (current.compareTo(maximum) > 0) {
                maximum = current;
            }
        }

        return maximum;
    }

    public int countGreaterThan(T threshold) {
        if (threshold == null) {
            return 0;
        }

        int count = 0;

        for (T value : values) {
            if (value.compareTo(threshold) > 0) {
                count++;
            }
        }

        return count;
    }

    public List<T> snapshot() {
        return new ArrayList<>(values);
    }
}