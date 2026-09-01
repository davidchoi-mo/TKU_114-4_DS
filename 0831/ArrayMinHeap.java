import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class ArrayMinHeap {
    private static final int DEFAULT_CAPACITY = 8;

    private int[] data;
    private int size;

    public ArrayMinHeap() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayMinHeap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be positive");
        }
        data = new int[initialCapacity];
    }

    public void add(int value) {
        ensureCapacity();
        data[size] = value;
        bubbleUp(size);
        size++;
    }

    public int remove() {
        ensureNotEmpty();
        int result = data[0];
        size--;
        if (size > 0) {
            data[0] = data[size];
            bubbleDown(0);
        }
        return result;
    }

    public int removeMin() {
        return remove();
    }

    public int peek() {
        ensureNotEmpty();
        return data[0];
    }

    public int[] snapshot() {
        return Arrays.copyOf(data, size);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (size < data.length) {
            return;
        }
        if (data.length > Integer.MAX_VALUE / 2) {
            throw new OutOfMemoryError("heap capacity is too large");
        }
        data = Arrays.copyOf(data, data.length * 2);
    }

    private void ensureNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("heap is empty");
        }
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data[parent] <= data[index]) {
                return;
            }
            swap(parent, index);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            if (left >= size) {
                return;
            }

            int right = left + 1;
            int smallerChild = left;
            if (right < size && data[right] < data[left]) {
                smallerChild = right;
            }
            if (data[index] <= data[smallerChild]) {
                return;
            }
            swap(index, smallerChild);
            index = smallerChild;
        }
    }

    private void swap(int first, int second) {
        int temp = data[first];
        data[first] = data[second];
        data[second] = temp;
    }

    public static void main(String[] args) {
        int[] testData = {
                42, 17, 8, 99, 23, 5, 61, 17, 3, 77,
                54, 1, 36, 88, 12, 45, 29, 6, 70, 2,
                31, 50, 4, 93, 15
        };

        ArrayMinHeap heap = new ArrayMinHeap(2);
        for (int value : testData) {
            heap.add(value);
        }
        System.out.println("SIZE|" + heap.size());
        System.out.println("PEEK|" + heap.peek());
        System.out.println("HEAP|" + Arrays.toString(heap.snapshot()));

        List<Integer> removalOrder = new ArrayList<>();
        while (!heap.isEmpty()) {
            removalOrder.add(heap.remove());
        }
        System.out.println("REMOVE_ORDER|" + removalOrder);
    }
}
