import java.util.ArrayList;
import java.util.List;

public class Q02_MinHeapInsert {
    private final ArrayList<Integer> heap = new ArrayList<>();

    public void add(int value) {
        heap.add(value);

        int index = heap.size() - 1;

        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            if (heap.get(parentIndex) <= heap.get(index)) {
                break;
            }

            int temp = heap.get(parentIndex);
            heap.set(parentIndex, heap.get(index));
            heap.set(index, temp);

            index = parentIndex;
        }
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

    public boolean isValidMinHeap() {
        for (int parent = 0; parent < heap.size(); parent++) {
            int leftChild = parent * 2 + 1;
            int rightChild = parent * 2 + 2;

            if (leftChild < heap.size()
                    && heap.get(parent) > heap.get(leftChild)) {
                return false;
            }

            if (rightChild < heap.size()
                    && heap.get(parent) > heap.get(rightChild)) {
                return false;
            }
        }

        return true;
    }
}