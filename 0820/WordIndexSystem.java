import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class WordIndexSystem {
    public static void main(String[] args) {
        String[] sentences = {
                "Java is useful, and Java is popular.",
                "Collections make Java data processing useful.",
                "A Map counts data, and a Set removes repeated data."
        };

        Map<String, Integer> wordCounts = new LinkedHashMap<>();
        Set<String> uniqueWords = new LinkedHashSet<>();

        for (String sentence : sentences) {
            String normalized = sentence.toLowerCase(Locale.ROOT)
                    .replace(".", "")
                    .replace(",", "");

            for (String word : normalized.split("\\s+")) {
                if (word.isBlank()) {
                    continue;
                }
                uniqueWords.add(word);
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }
        }

        System.out.println("=== 單字次數 Map ===");
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        System.out.println("\n=== 不重複單字 Set ===");
        System.out.println(uniqueWords);

        System.out.println("\n=== 至少出現兩次的單字 ===");
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            if (entry.getValue() >= 2) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        }
    }
}
