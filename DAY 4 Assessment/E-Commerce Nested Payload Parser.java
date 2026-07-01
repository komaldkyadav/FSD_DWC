// Order.java
import java.util.List;

public class Order {
    private List<Item> items;
    private String orderId;
    
    public Order(String orderId, List<Item> items) {
        this.orderId = orderId;
        this.items = items;
    }
    
    public List<Item> getItems() {
        return items;
    }
    
    public String getOrderId() {
        return orderId;
    }
}

// Item.java
public class Item {
    private String name;
    private String category;
    private double price;
    
    public Item(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public double getPrice() {
        return price;
    }
}

// OrderProcessor.java
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrderProcessor {
    public double calculateElectronicsRevenue(List<Order> orders) {
        return orders.stream()
                .filter(Objects::nonNull)
                .flatMap(order -> order.getItems().stream())
                .filter(Objects::nonNull)
                .filter(item -> "ELECTRONICS".equals(item.getCategory()))
                .filter(item -> item.getPrice() > 0)
                .mapToDouble(Item::getPrice)
                .sum();
    }
}

// ECommercePayloadParser.java
import java.util.Arrays;
import java.util.List;

public class ECommercePayloadParser {
    public static void main(String[] args) {
        OrderProcessor processor = new OrderProcessor();
        
        List<Order> orders = Arrays.asList(
            new Order("ORD-001", Arrays.asList(
                new Item("Laptop", "ELECTRONICS", 999.99),
                new Item("Mouse", "ELECTRONICS", 29.99),
                new Item("Book", "BOOKS", 19.99)
            )),
            new Order("ORD-002", Arrays.asList(
                new Item("Phone", "ELECTRONICS", 699.99),
                null,
                new Item("Headphones", "ELECTRONICS", 89.99)
            )),
            null,
            new Order("ORD-003", Arrays.asList(
                new Item("TV", "ELECTRONICS", 1499.00),
                new Item("Couch", "FURNITURE", 599.00)
            ))
        );
        
        System.out.println("=== E-Commerce Nested Payload Parser ===\n");
        System.out.println("Processing " + orders.size() + " orders...");
        
        double revenue = processor.calculateElectronicsRevenue(orders);
        
        System.out.println("\nElectronics Revenue: $" + String.format("%.2f", revenue));
        
        System.out.println("\n=== Stream Pipeline Analysis ===");
        System.out.println("1. filter(Objects::nonNull): Removes null orders");
        System.out.println("2. flatMap: Extracts items from all orders");
        System.out.println("3. filter(Objects::nonNull): Removes null items");
        System.out.println("4. filter: Keeps only ELECTRONICS category");
        System.out.println("5. filter: Ensures positive price");
        System.out.println("6. mapToDouble: Converts to DoubleStream");
        System.out.println("7. sum: Aggregates total revenue");
        
        System.out.println("\n=== Null Safety ===");
        System.out.println("Objects::nonNull prevents NullPointerException");
        System.out.println("\"ELECTRONICS\".equals(item.getCategory()) safe even if category is null");
    }
}