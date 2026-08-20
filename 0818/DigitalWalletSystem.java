class DigitalWallet {
    private final String walletId;
    private final String owner;
    private int balance;
    private int transactionCount;

    DigitalWallet(String walletId, String owner) {
        this.walletId = isBlank(walletId) ? "UNKNOWN" : walletId.trim();
        this.owner = isBlank(owner) ? "Unknown" : owner.trim();
        this.balance = 0;
        this.transactionCount = 0;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    boolean deposit(int amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        transactionCount++;
        return true;
    }

    boolean pay(int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        transactionCount++;
        return true;
    }

    boolean refund(int amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        transactionCount++;
        return true;
    }

    int getBalance() {
        return balance;
    }

    int getTransactionCount() {
        return transactionCount;
    }

    @Override
    public String toString() {
        return walletId + " owner=" + owner + " balance=" + balance
                + " successfulTransactions=" + transactionCount;
    }
}

public class DigitalWalletSystem {
    public static void main(String[] args) {
        DigitalWallet wallet = new DigitalWallet("W001", "Amy");

        System.out.println("正常儲值 1000：" + wallet.deposit(1000));
        System.out.println("正常付款 250：" + wallet.pay(250));
        System.out.println("餘額不足付款 900：" + wallet.pay(900));
        System.out.println("負數儲值 -100：" + wallet.deposit(-100));
        System.out.println("退款 50：" + wallet.refund(50));
        System.out.println(wallet);
    }
}
