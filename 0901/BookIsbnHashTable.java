import java.util.ArrayList;
import java.util.List;

public class BookIsbnHashTable {
    public record Book(String isbn, String title, String author) {
        public Book {
            isbn = requireText(isbn, "isbn");
            title = requireText(title, "title");
            author = requireText(author, "author");
        }
    }

    private static final class Entry {
        private final String key;
        private Book value;

        private Entry(String key, Book value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value.title();
        }
    }

    private final List<List<Entry>> buckets;
    private int size;

    public BookIsbnHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount 必須大於 0");
        }
        buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    public Book put(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("book 不可為 null");
        }
        String key = normalizeIsbn(book.isbn());
        List<Entry> chain = buckets.get(index(key));
        for (Entry entry : chain) {
            if (entry.key.equals(key)) {
                Book oldValue = entry.value;
                entry.value = book;
                return oldValue;
            }
        }
        chain.add(new Entry(key, book));
        size++;
        return null;
    }

    public Book get(String isbn) {
        String key = normalizeIsbn(isbn);
        for (Entry entry : buckets.get(index(key))) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public Book remove(String isbn) {
        String key = normalizeIsbn(isbn);
        List<Entry> chain = buckets.get(index(key));
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key.equals(key)) {
                Book removed = chain.remove(i).value;
                size--;
                return removed;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public double loadFactor() {
        return (double) size / buckets.size();
    }

    public List<String> bucketReport() {
        List<String> report = new ArrayList<>();
        for (int i = 0; i < buckets.size(); i++) {
            report.add(i + " -> " + buckets.get(i));
        }
        return report;
    }

    private int index(String key) {
        return Math.floorMod(key.hashCode(), buckets.size());
    }

    private static String normalizeIsbn(String isbn) {
        return requireText(isbn, "isbn").replace("-", "").replace(" ", "");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " 不可為空白");
        }
        return value.trim();
    }

    public static void main(String[] args) {
        BookIsbnHashTable table = new BookIsbnHashTable(5);
        table.put(new Book("978-986-312-001-0", "Java 入門", "王小明"));
        table.put(new Book("978-986-312-002-7", "資料結構", "陳美玲"));
        table.put(new Book("978-986-312-003-4", "圖論基礎", "林志強"));

        table.put(new Book("9789863120010", "Java 入門（第二版）", "王小明"));

        System.out.println("搜尋=" + table.get("978-986-312-001-0"));
        System.out.println("size=" + table.size());
        System.out.printf("load factor=%.2f%n", table.loadFactor());
        table.bucketReport().forEach(System.out::println);
        System.out.println("刪除=" + table.remove("978-986-312-002-7"));
        System.out.println("刪除後 size=" + table.size());
    }
}
