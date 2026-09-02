import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebsiteLinkGraph {
    private final Map<String, Set<String>> outgoing = new LinkedHashMap<>();

    public boolean addPage(String page) {
        String normalized = requirePage(page);
        return outgoing.putIfAbsent(normalized, new LinkedHashSet<>()) == null;
    }

    public boolean addLink(String from, String to) {
        String source = requireExistingPage(from);
        String target = requireExistingPage(to);
        return outgoing.get(source).add(target);
    }

    public boolean removeLink(String from, String to) {
        String source = requireExistingPage(from);
        String target = requireExistingPage(to);
        return outgoing.get(source).remove(target);
    }

    public List<String> outgoingLinks(String page) {
        String source = requireExistingPage(page);
        return outgoing.get(source).stream().sorted().toList();
    }

    public List<String> incomingLinks(String page) {
        String target = requireExistingPage(page);
        List<String> sources = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : outgoing.entrySet()) {
            if (entry.getValue().contains(target)) {
                sources.add(entry.getKey());
            }
        }
        return sources.stream().sorted().toList();
    }

    public int incomingCount(String page) {
        return incomingLinks(page).size();
    }

    public List<String> pagesWithNoIncoming() {
        Set<String> hasIncoming = new LinkedHashSet<>();
        for (Set<String> links : outgoing.values()) {
            hasIncoming.addAll(links);
        }
        return outgoing.keySet().stream()
                .filter(page -> !hasIncoming.contains(page))
                .sorted()
                .toList();
    }

    public List<String> pagesWithNoOutgoing() {
        return outgoing.entrySet().stream()
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
    }

    private String requireExistingPage(String page) {
        String normalized = requirePage(page);
        if (!outgoing.containsKey(normalized)) {
            throw new IllegalArgumentException("不存在的頁面：" + normalized);
        }
        return normalized;
    }

    private static String requirePage(String page) {
        if (page == null || page.isBlank()) {
            throw new IllegalArgumentException("page 不可為空白");
        }
        return page.trim();
    }

    public static void main(String[] args) {
        WebsiteLinkGraph web = new WebsiteLinkGraph();
        for (String page : List.of("home", "products", "about", "contact", "orphan")) {
            web.addPage(page);
        }
        web.addLink("home", "products");
        web.addLink("home", "about");
        web.addLink("products", "contact");
        web.addLink("about", "contact");
        web.addLink("home", "products");

        for (String page : web.outgoing.keySet()) {
            System.out.printf("%s: outgoing=%s, incoming=%d%n",
                    page, web.outgoingLinks(page), web.incomingCount(page));
        }
        System.out.println("無 incoming=" + web.pagesWithNoIncoming());
        System.out.println("無 outgoing=" + web.pagesWithNoOutgoing());
    }
}
