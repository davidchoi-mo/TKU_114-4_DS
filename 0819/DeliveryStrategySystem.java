interface DeliveryMethod {
    int calculateFee(int orderAmount, int distanceKm);

    String estimate(String destination);

    String getName();
}

class HomeDelivery implements DeliveryMethod {
    @Override
    public int calculateFee(int orderAmount, int distanceKm) {
        int safeAmount = Math.max(0, orderAmount);
        int safeDistance = Math.max(0, distanceKm);
        if (safeAmount >= 1500) {
            return 0;
        }
        return 80 + safeDistance * 5;
    }

    @Override
    public String estimate(String destination) {
        return "宅配至「" + normalizeDestination(destination) + "」，預計 1～2 天";
    }

    @Override
    public String getName() {
        return "宅配";
    }

    private String normalizeDestination(String destination) {
        return destination == null || destination.trim().isEmpty()
                ? "未指定地址" : destination.trim();
    }
}

class ConvenienceStorePickup implements DeliveryMethod {
    @Override
    public int calculateFee(int orderAmount, int distanceKm) {
        return Math.max(0, orderAmount) >= 1000 ? 0 : 60;
    }

    @Override
    public String estimate(String destination) {
        String store = destination == null || destination.trim().isEmpty()
                ? "未指定門市" : destination.trim();
        return "配送至「" + store + "」，預計 2～3 天可取貨";
    }

    @Override
    public String getName() {
        return "超商取貨";
    }
}

class SelfPickup implements DeliveryMethod {
    @Override
    public int calculateFee(int orderAmount, int distanceKm) {
        return 0;
    }

    @Override
    public String estimate(String destination) {
        return "付款完成後可至服務台自取";
    }

    @Override
    public String getName() {
        return "自取";
    }
}

class OrderService {
    private final String orderId;
    private final int orderAmount;
    private final int distanceKm;
    private final String destination;
    private final DeliveryMethod deliveryMethod;

    OrderService(String orderId, int orderAmount, int distanceKm,
                 String destination, DeliveryMethod deliveryMethod) {
        this.orderId = orderId == null || orderId.trim().isEmpty()
                ? "UNKNOWN" : orderId.trim();
        this.orderAmount = Math.max(0, orderAmount);
        this.distanceKm = Math.max(0, distanceKm);
        this.destination = destination;
        this.deliveryMethod = deliveryMethod == null
                ? new SelfPickup() : deliveryMethod;
    }

    int calculateDeliveryFee() {
        return deliveryMethod.calculateFee(orderAmount, distanceKm);
    }

    String summary() {
        return orderId + "｜商品金額=$" + orderAmount
                + "｜方式=" + deliveryMethod.getName()
                + "｜運費=$" + calculateDeliveryFee()
                + "｜" + deliveryMethod.estimate(destination);
    }
}

public class DeliveryStrategySystem {
    public static void main(String[] args) {
        OrderService[] orders = {
            new OrderService("O101", 800, 10, "台北市中正區",
                    new HomeDelivery()),
            new OrderService("O102", 1800, 25, "台中公益門市",
                    new ConvenienceStorePickup()),
            new OrderService("O103", 500, 0, "校內服務台",
                    new SelfPickup()),
            new OrderService("O104", 2000, 30, "高雄市三民區",
                    new HomeDelivery())
        };

        for (OrderService order : orders) {
            System.out.println(order.summary());
        }
    }
}
