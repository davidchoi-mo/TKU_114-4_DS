public class Q01_InventoryItem {
    private final String id;
    private final String name;
    private int stock;

    public Q01_InventoryItem(String id, String name, int stock) {
        if (id == null || id.trim().isEmpty()
                || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "id 和 name 不可為 null 或空字串"
            );
        }

        this.id = id.trim();
        this.name = name.trim();
        this.stock = Math.max(stock, 0);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public boolean restock(int amount) {
        if (amount <= 0) {
            return false;
        }

        stock += amount;
        return true;
    }

    public boolean sell(int amount) {
        if (amount <= 0 || stock < amount) {
            return false;
        }

        stock -= amount;
        return true;
    }

    public String status() {
        return id + "|" + name + "|" + stock;
    }
}