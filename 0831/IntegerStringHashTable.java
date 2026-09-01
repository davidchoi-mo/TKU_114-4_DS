import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IntegerStringHashTable {
    private static final class Entry {
        private final int key;
        private String value;

        private Entry(int key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private final List<List<Entry>> buckets;
    private int size;

    public IntegerStringHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount must be positive");
        }
        buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    public void put(int key, String value) {
        List<Entry> chain = buckets.get(bucketIndex(key));
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
        Entry entry = findEntry(key);
        return entry == null ? null : entry.value;
    }

    public boolean containsKey(int key) {
        return findEntry(key) != null;
    }

    public boolean remove(int key) {
        List<Entry> chain = buckets.get(bucketIndex(key));
        Iterator<Entry> iterator = chain.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().key == key) {
                iterator.remove();
                size--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public String bucketReport() {
        StringBuilder report = new StringBuilder();
        int collisions = 0;
        int longestChain = 0;

        for (int i = 0; i < buckets.size(); i++) {
            List<Entry> chain = buckets.get(i);
            if (i > 0) {
                report.append(System.lineSeparator());
            }
            report.append(i).append(" -> ").append(chain);
            collisions += Math.max(0, chain.size() - 1);
            longestChain = Math.max(longestChain, chain.size());
        }

        report.append(System.lineSeparator())
                .append("size=").append(size)
                .append(", collisions=").append(collisions)
                .append(", longestChain=").append(longestChain);
        return report.toString();
    }

    private int bucketIndex(int key) {
        return Math.floorMod(Integer.hashCode(key), buckets.size());
    }

    private Entry findEntry(int key) {
        for (Entry entry : buckets.get(bucketIndex(key))) {
            if (entry.key == key) {
                return entry;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        IntegerStringHashTable table = new IntegerStringHashTable(5);
        table.put(12, "Alice");
        table.put(7, "Bob");
        table.put(22, "Carol");
        table.put(-3, "David");
        table.put(7, "Bob-Updated");

        System.out.println("GET_7|" + table.get(7));
        System.out.println("CONTAINS_22|" + table.containsKey(22));
        System.out.println("REMOVE_12|" + table.remove(12));
        System.out.println("REMOVE_999|" + table.remove(999));
        System.out.println(table.bucketReport());
    }
}
