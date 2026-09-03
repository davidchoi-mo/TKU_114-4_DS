import java.util.ArrayList;
import java.util.List;

public class Q04_ChainedHashTable {
    private final List<List<Entry>> buckets;
    private int size;

    public Q04_ChainedHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount must be greater than 0");
        }

        buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    public void put(int key, String value) {
        List<Entry> chain = chainFor(key);

        for (Entry entry : chain) {
            if (entry.key == key) {
                entry.value = value;
                return;
            }
        }

        chain.add(new Entry(key, value));
        size++;
    }

    public String get(int key) {
        for (Entry entry : chainFor(key)) {
            if (entry.key == key) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean remove(int key) {
        List<Entry> chain = chainFor(key);

        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key == key) {
                chain.remove(i);
                size--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public int longestChain() {
        int longest = 0;
        for (List<Entry> chain : buckets) {
            longest = Math.max(longest, chain.size());
        }
        return longest;
    }

    private List<Entry> chainFor(int key) {
        int bucketIndex = Math.floorMod(key, buckets.size());
        return buckets.get(bucketIndex);
    }

    private static class Entry {
        private final int key;
        private String value;

        private Entry(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
