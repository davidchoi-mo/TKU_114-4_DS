interface PricingPolicy {
    int finalPrice(int originalPrice);

    String getName();
}

class StandardPricing implements PricingPolicy {
    @Override
    public int finalPrice(int originalPrice) {
        return Math.max(0, originalPrice);
    }

    @Override
    public String getName() {
        return "原價";
    }
}

class VipPricing implements PricingPolicy {
    @Override
    public int finalPrice(int originalPrice) {
        return Math.max(0, originalPrice) * 85 / 100;
    }

    @Override
    public String getName() {
        return "VIP 八五折";
    }
}

class ThresholdPricing implements PricingPolicy {
    @Override
    public int finalPrice(int originalPrice) {
        int safePrice = Math.max(0, originalPrice);
        return safePrice >= 2000 ? safePrice - 300 : safePrice;
    }

    @Override
    public String getName() {
        return "滿 2000 折 300";
    }
}

interface NotificationChannel {
    boolean send(String receiver, String message);

    String getName();
}

class EmailChannel implements NotificationChannel {
    @Override
    public boolean send(String receiver, String message) {
        if (receiver == null || !receiver.contains("@")
                || message == null || message.trim().isEmpty()) {
            return false;
        }
        System.out.println("EMAIL " + receiver + " -> " + message);
        return true;
    }

    @Override
    public String getName() {
        return "Email";
    }
}

class SmsChannel implements NotificationChannel {
    @Override
    public boolean send(String receiver, String message) {
        if (receiver == null || !receiver.matches("[0-9]{8,15}")
                || message == null || message.trim().isEmpty()) {
            return false;
        }
        System.out.println("SMS " + receiver + " -> " + message);
        return true;
    }

    @Override
    public String getName() {
        return "SMS";
    }
}

class ConsoleChannel implements NotificationChannel {
    @Override
    public boolean send(String receiver, String message) {
        if (message == null || message.trim().isEmpty()) {
            return false;
        }
        String target = receiver == null || receiver.trim().isEmpty()
                ? "console" : receiver.trim();
        System.out.println("CONSOLE " + target + " -> " + message);
        return true;
    }

    @Override
    public String getName() {
        return "Console";
    }
}

final class CheckoutResult {
    private final String orderId;
    private final int originalPrice;
    private final int finalPrice;
    private final boolean notificationStatus;

    CheckoutResult(String orderId, int originalPrice, int finalPrice,
                   boolean notificationStatus) {
        this.orderId = orderId;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.notificationStatus = notificationStatus;
    }

    @Override
    public String toString() {
        return "CheckoutResult{orderId='" + orderId
                + "', originalPrice=" + originalPrice
                + ", finalPrice=" + finalPrice
                + ", notificationStatus=" + notificationStatus + "}";
    }
}

class CheckoutService {
    private final PricingPolicy pricing;
    private final NotificationChannel channel;

    CheckoutService(PricingPolicy pricing, NotificationChannel channel) {
        this.pricing = pricing == null ? new StandardPricing() : pricing;
        this.channel = channel == null ? new ConsoleChannel() : channel;
    }

    CheckoutResult checkout(String orderId, int originalPrice, String receiver) {
        if (orderId == null || orderId.trim().isEmpty() || originalPrice < 0) {
            String safeId = orderId == null || orderId.trim().isEmpty()
                    ? "UNKNOWN" : orderId.trim();
            return new CheckoutResult(safeId, originalPrice, 0, false);
        }

        int amount = pricing.finalPrice(originalPrice);
        String message = "order=" + orderId.trim()
                + ", pricing=" + pricing.getName()
                + ", amount=" + amount;
        boolean sent = channel.send(receiver, message);
        return new CheckoutResult(orderId.trim(), originalPrice, amount, sent);
    }

    String configuration() {
        return pricing.getName() + " + " + channel.getName();
    }
}

public class FlexibleCheckoutSystem {
    private static void runTest(String label, CheckoutService service,
                                String orderId, int price, String receiver) {
        System.out.println("\n" + label + "（" + service.configuration() + "）");
        System.out.println(service.checkout(orderId, price, receiver));
    }

    public static void main(String[] args) {
        runTest("組合 1", new CheckoutService(
                new StandardPricing(), new EmailChannel()),
                "O101", 800, "amy@example.com");

        runTest("組合 2", new CheckoutService(
                new VipPricing(), new SmsChannel()),
                "O102", 2000, "0912345678");

        runTest("組合 3", new CheckoutService(
                new ThresholdPricing(), new ConsoleChannel()),
                "O103", 2500, "counter");

        runTest("組合 4：無效 Email", new CheckoutService(
                new VipPricing(), new EmailChannel()),
                "O104", 1200, "invalid-email");

        runTest("組合 5：無效手機", new CheckoutService(
                new StandardPricing(), new SmsChannel()),
                "O105", 500, "12AB");

        runTest("組合 6：未達折扣門檻", new CheckoutService(
                new ThresholdPricing(), new ConsoleChannel()),
                "O106", 1500, "counter");
    }
}
