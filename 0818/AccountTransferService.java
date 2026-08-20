class Account {
    private final String accountId;
    private int balance;

    Account(String accountId, int openingBalance) {
        this.accountId = accountId == null || accountId.trim().isEmpty()
                ? "UNKNOWN" : accountId.trim();
        this.balance = Math.max(0, openingBalance);
    }

    int getBalance() {
        return balance;
    }

    boolean withdraw(int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    void deposit(int amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    @Override
    public String toString() {
        return accountId + " balance=" + balance;
    }
}

class TransferService {
    static boolean transfer(Account source, Account target, int amount) {
        if (source == null || target == null || source == target) {
            return false;
        }
        if (amount <= 0 || source.getBalance() < amount) {
            return false;
        }

        // 所有條件已先驗證，因此提款成功後才會存入目標帳戶。
        if (!source.withdraw(amount)) {
            return false;
        }
        target.deposit(amount);
        return true;
    }
}

public class AccountTransferService {
    private static void showResult(String testName, boolean result,
                                   Account source, Account target) {
        System.out.println(testName + "：" + result);
        System.out.println("  source = " + source);
        System.out.println("  target = " + target);
    }

    public static void main(String[] args) {
        Account source = new Account("A", 1000);
        Account target = new Account("B", 200);

        boolean success = TransferService.transfer(source, target, 300);
        showResult("成功轉帳", success, source, target);

        boolean insufficient = TransferService.transfer(source, target, 900);
        showResult("餘額不足", insufficient, source, target);

        boolean sameAccount = TransferService.transfer(source, source, 100);
        showResult("同帳戶轉帳", sameAccount, source, source);

        boolean nullTarget = TransferService.transfer(source, null, 100);
        System.out.println("null 目標：" + nullTarget);
        System.out.println("  source = " + source);
        System.out.println("  target = null");
    }
}
