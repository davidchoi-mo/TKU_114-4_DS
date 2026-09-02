import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginActivityReport {
    public record LoginRecord(String account, String ip) {
        public LoginRecord {
            account = normalize(account, "account").toLowerCase();
            ip = normalize(ip, "ip");
        }
    }

    public record AccountIpKey(String account, String ip) {}

    private final Map<String, Integer> accountCounts = new HashMap<>();
    private final Set<String> distinctIps = new HashSet<>();
    private final Map<AccountIpKey, Integer> accountIpCounts = new HashMap<>();

    public void add(LoginRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("record 不可為 null");
        }
        accountCounts.merge(record.account(), 1, Integer::sum);
        distinctIps.add(record.ip());
        accountIpCounts.merge(
                new AccountIpKey(record.account(), record.ip()),
                1,
                Integer::sum);
    }

    public void addAll(List<LoginRecord> records) {
        if (records == null) {
            return;
        }
        for (LoginRecord record : records) {
            add(record);
        }
    }

    public Map<String, Integer> accountLoginCounts() {
        Map<String, Integer> sorted = new LinkedHashMap<>();
        accountCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sorted.put(entry.getKey(), entry.getValue()));
        return sorted;
    }

    public int distinctIpCount() {
        return distinctIps.size();
    }

    public List<String> abnormalRepeatedLogins(int threshold) {
        if (threshold < 2) {
            throw new IllegalArgumentException("threshold 必須至少為 2");
        }
        List<Map.Entry<AccountIpKey, Integer>> entries = new ArrayList<>();
        for (Map.Entry<AccountIpKey, Integer> entry : accountIpCounts.entrySet()) {
            if (entry.getValue() >= threshold) {
                entries.add(entry);
            }
        }
        entries.sort(Comparator
                .<Map.Entry<AccountIpKey, Integer>>comparingInt(Map.Entry::getValue)
                .reversed()
                .thenComparing(entry -> entry.getKey().account())
                .thenComparing(entry -> entry.getKey().ip()));

        List<String> report = new ArrayList<>();
        for (Map.Entry<AccountIpKey, Integer> entry : entries) {
            AccountIpKey key = entry.getKey();
            report.add(key.account() + " @ " + key.ip() + " -> " + entry.getValue() + " 次");
        }
        return report;
    }

    private static String normalize(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " 不可為空白");
        }
        return value.trim();
    }

    public static void main(String[] args) {
        LoginActivityReport report = new LoginActivityReport();
        report.addAll(List.of(
                new LoginRecord("Amy", "10.0.0.1"),
                new LoginRecord("amy", "10.0.0.1"),
                new LoginRecord("AMY", "10.0.0.1"),
                new LoginRecord("Ben", "10.0.0.2"),
                new LoginRecord("Ben", "10.0.0.3"),
                new LoginRecord("Cara", "10.0.0.2")));

        System.out.println("各帳號登入次數=" + report.accountLoginCounts());
        System.out.println("不同 IP 數量=" + report.distinctIpCount());
        System.out.println("異常重複登入（至少 2 次）：");
        report.abnormalRepeatedLogins(2).forEach(System.out::println);
    }
}
