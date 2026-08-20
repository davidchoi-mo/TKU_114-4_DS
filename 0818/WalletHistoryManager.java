final class HistoryTransaction {
    private final int sequence;
    private final String type;
    private final int amount;
    private final int balanceAfter;

    HistoryTransaction(int sequence, String type, int amount, int balanceAfter) {
        this.sequence = sequence;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    int getSequence() {
        return sequence;
    }

    String getType() {
        return type;
    }

    int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return sequence + " " + type + " " + amount
                + " balance=" + balanceAfter;
    }
}

class HistoryWallet {
    private final String walletId;
    private final String owner;
    private int balance;
    private final HistoryTransaction[] transactions;
    private int transactionCount;

    HistoryWallet(String walletId, String owner, int historyCapacity) {
        this.walletId = normalize(walletId, "UNKNOWN");
        this.owner = normalize(owner, "Unknown");
        this.balance = 0;
        this.transactions = new HistoryTransaction[Math.max(1, historyCapacity)];
        this.transactionCount = 0;
    }

    private static String normalize(String value, String defaultValue) {
        return value == null || value.trim().isEmpty()
                ? defaultValue : value.trim();
    }

    private boolean hasHistorySpace() {
        return transactionCount < transactions.length;
    }

    boolean deposit(int amount) {
        if (amount <= 0 || !hasHistorySpace()) {
            return false;
        }
        balance += amount;
        record("DEPOSIT", amount);
        return true;
    }

    boolean pay(int amount) {
        if (amount <= 0 || amount > balance || !hasHistorySpace()) {
            return false;
        }
        balance -= amount;
        record("PAY", amount);
        return true;
    }

    boolean refund(int amount) {
        if (amount <= 0 || !hasHistorySpace()) {
            return false;
        }
        balance += amount;
        record("REFUND", amount);
        return true;
    }

    HistoryTransaction findTransaction(int sequence) {
        for (int i = 0; i < transactionCount; i++) {
            if (transactions[i].getSequence() == sequence) {
                return transactions[i];
            }
        }
        return null;
    }

    int totalByType(String type) {
        if (type == null) {
            return 0;
        }
        int total = 0;
        for (int i = 0; i < transactionCount; i++) {
            if (transactions[i].getType().equalsIgnoreCase(type.trim())) {
                total += transactions[i].getAmount();
            }
        }
        return total;
    }

    boolean transferTo(HistoryWallet target, int amount) {
        if (target == null || target == this || amount <= 0 || amount > balance) {
            return false;
        }
        // 先檢查兩邊容量，確保轉帳具備原子性，不會只修改其中一邊。
        if (!hasHistorySpace() || !target.hasHistorySpace()) {
            return false;
        }

        balance -= amount;
        target.balance += amount;
        record("TRANSFER_OUT", amount);
        target.record("TRANSFER_IN", amount);
        return true;
    }

    private void record(String type, int amount) {
        transactions[transactionCount] = new HistoryTransaction(
                transactionCount + 1, type, amount, balance);
        transactionCount++;
    }

    void printStatement() {
        System.out.println(walletId + " owner=" + owner + " balance=" + balance);
        for (int i = 0; i < transactionCount; i++) {
            System.out.println("  " + transactions[i]);
        }
    }
}

public class WalletHistoryManager {
    public static void main(String[] args) {
        HistoryWallet first = new HistoryWallet("W001", "Amy", 6);
        HistoryWallet second = new HistoryWallet("W002", "Ben", 5);

        System.out.println("first 儲值：" + first.deposit(1000));
        System.out.println("first 付款：" + first.pay(200));
        System.out.println("second 儲值：" + second.deposit(300));
        System.out.println("轉帳：" + first.transferTo(second, 250));

        System.out.println("查詢 first #2：" + first.findTransaction(2));
        System.out.println("查詢 first #99：" + first.findTransaction(99));
        System.out.println("first DEPOSIT 總額：" + first.totalByType("DEPOSIT"));
        System.out.println("second TRANSFER_IN 總額："
                + second.totalByType("TRANSFER_IN"));

        HistoryWallet fullWallet = new HistoryWallet("W003", "Cara", 1);
        fullWallet.deposit(100); // 唯一一格交易紀錄已使用
        System.out.println("轉入紀錄已滿的錢包："
                + first.transferTo(fullWallet, 50));

        System.out.println("\n第一個錢包 statement：");
        first.printStatement();
        System.out.println("\n第二個錢包 statement：");
        second.printStatement();
    }
}
