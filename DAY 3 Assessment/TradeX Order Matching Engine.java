// Order.java
public class Order {
    private String orderId;
    private String type;  // "BUY" or "SELL"
    private double price;
    private int quantity;
    private long timestamp;
    
    public Order(String orderId, String type, double price, int quantity) {
        this.orderId = orderId;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public String getType() {
        return type;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "Order{" +
               "orderId='" + orderId + '\'' +
               ", type='" + type + '\'' +
               ", price=" + price +
               ", quantity=" + quantity +
               '}';
    }
}

// ExchangeManager.java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExchangeManager {
    private ConcurrentHashMap<String, CopyOnWriteArrayList<Order>> orderBook;
    
    public ExchangeManager() {
        orderBook = new ConcurrentHashMap<>();
    }
    
    public void placeOrder(String ticker, Order order) {
        orderBook.computeIfAbsent(ticker, k -> new CopyOnWriteArrayList<>());
        orderBook.get(ticker).add(order);
        System.out.println("Order placed for " + ticker + ": " + order.getOrderId() + 
                          " (" + order.getType() + " " + order.getQuantity() + " @ $" + order.getPrice() + ")");
    }
    
    public CopyOnWriteArrayList<Order> getOrders(String ticker) {
        return orderBook.get(ticker);
    }
    
    public void printOrderBook(String ticker) {
        CopyOnWriteArrayList<Order> orders = orderBook.get(ticker);
        if (orders != null) {
            System.out.println("\nOrder Book for " + ticker + ":");
            orders.forEach(order -> System.out.println("  " + order));
        } else {
            System.out.println("No orders for " + ticker);
        }
    }
    
    public int getOrderCount(String ticker) {
        CopyOnWriteArrayList<Order> orders = orderBook.get(ticker);
        return orders != null ? orders.size() : 0;
    }
}

// TradingSystem.java
public class TradingSystem {
    public static void main(String[] args) throws InterruptedException {
        ExchangeManager exchange = new ExchangeManager();
        
        System.out.println("=== Cryptocurrency Trading System ===");
        
        Order btc1 = new Order("BTC001", "BUY", 45000.00, 1);
        Order btc2 = new Order("BTC002", "SELL", 46000.00, 0.5);
        Order btc3 = new Order("BTC003", "BUY", 45500.00, 2);
        
        Order eth1 = new Order("ETH001", "BUY", 3000.00, 5);
        Order eth2 = new Order("ETH002", "SELL", 3100.00, 3);
        
        exchange.placeOrder("BTC", btc1);
        exchange.placeOrder("BTC", btc2);
        exchange.placeOrder("ETH", eth1);
        exchange.placeOrder("BTC", btc3);
        exchange.placeOrder("ETH", eth2);
        
        exchange.printOrderBook("BTC");
        exchange.printOrderBook("ETH");
        
        System.out.println("\n=== Thread Safety Demonstration ===");
        System.out.println("Simulating concurrent orders from multiple threads...");
        
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                String ticker = index % 2 == 0 ? "BTC" : "ETH";
                Order order = new Order("T" + index + "001", "BUY", 45000 + index * 100, index + 1);
                exchange.placeOrder(ticker, order);
            });
            threads[i].start();
        }
        
        for (Thread t : threads) {
            t.join();
        }
        
        System.out.println("\nFinal Order Counts:");
        System.out.println("BTC Orders: " + exchange.getOrderCount("BTC"));
        System.out.println("ETH Orders: " + exchange.getOrderCount("ETH"));
        
        System.out.println("\n=== Memory Analysis ===");
        System.out.println("ConcurrentHashMap: Thread-safe map implementation");
        System.out.println("CopyOnWriteArrayList: Thread-safe for reads, copies on writes");
        System.out.println("computeIfAbsent: Atomic operation prevents race conditions");
        System.out.println("No synchronized needed - lock-striped concurrency");
    }
}