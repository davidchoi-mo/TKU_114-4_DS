import java.util.ArrayList;
import java.util.List;

public class Q03_MinHeapRemove {
    private final List<Integer> heap;

    public Q03_MinHeapRemove(List<Integer> values) {
        heap = new ArrayList<>();

        if (values != null) {
            for (Integer value : values) {
                if (value != null) {
                    heap.add(value);
                }
            }
        }

        for (int i = heap.size() / 2 - 1; i >= 0; i--) {
            bubbleDown(i);
        }
    }

    public Integer removeMin() {
        if (heap.isEmpty()) {
            return null;
        }

        Integer minimum = heap.get(0);
        Integer last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            bubbleDown(0);
        }

        return minimum;
    }

    public Integer peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    public int size() {
        return heap.size();
    }

    public List<Integer> snapshot() {
        return new ArrayList<>(heap);
    }

    private void bubbleDown(int parent) {
        int size = heap.size();

        while (true) {
            int left = parent * 2 + 1;
            if (left >= size) {
                return;
            }

            int right = left + 1;
            int smallerChild = left;

            if (right < size && heap.get(right) < heap.get(left)) {
                smallerChild = right;
            }

            if (heap.get(parent) <= heap.get(smallerChild)) {
                return;
            }

            Integer temporary = heap.get(parent);
            heap.set(parent, heap.get(smallerChild));
            heap.set(smallerChild, temporary);
            parent = smallerChild;
        }
    }
}
