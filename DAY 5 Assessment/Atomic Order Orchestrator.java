// InsufficientStockException.java
public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}

// Product.java
public class Product {
    private long productId;
    private String name;
    private int quantity;
    private double price;
    
    public Product(long productId, String name, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
    
    public long getProductId() { return productId; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}

// Order.java
import java.util.Date;

public class Order {
    private long orderId;
    private long productId;
    private int quantity;
    private Date orderDate;
    private String customerName;
    
    public Order(long productId, int quantity, String customerName) {
        this.productId = productId;
        this.quantity = quantity;
        this.customerName = customerName;
        this.orderDate = new Date();
    }
}

// AtomicOrderProcessor.java
import java.sql.*;

public class AtomicOrderProcessor {
    private static final String URL = "jdbc:postgresql://localhost:5432/ecommerce";
    private static final String USER = "ecommerce_admin";
    private static final String PASSWORD = "ecommerce_pass";
    
    private static final String CHECK_STOCK = 
        "SELECT quantity FROM products WHERE product_id = ?";
    
    private static final String UPDATE_STOCK = 
        "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";
    
    private static final String CREATE_ORDER = 
        "INSERT INTO orders (product_id, quantity, customer_name, order_date) " +
        "VALUES (?, ?, ?, ?)";
    
    public void processOrder(Order order) throws InsufficientStockException {
        Connection conn = null;
        
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false);
            
            // Check stock
            int availableQuantity = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(CHECK_STOCK)) {
                pstmt.setLong(1, order.getProductId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        availableQuantity = rs.getInt("quantity");
                    }
                }
            }
            
            if (availableQuantity < order.getQuantity()) {
                throw new InsufficientStockException(
                    "Insufficient stock. Available: " + availableQuantity + 
                    ", Requested: " + order.getQuantity()
                );
            }
            
            // Update stock
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_STOCK)) {
                pstmt.setInt(1, order.getQuantity());
                pstmt.setLong(2, order.getProductId());
                pstmt.executeUpdate();
            }
            
            // Create order
            try (PreparedStatement pstmt = conn.prepareStatement(CREATE_ORDER)) {
                pstmt.setLong(1, order.getProductId());
                pstmt.setInt(2, order.getQuantity());
                pstmt.setString(3, order.getCustomerName());
                pstmt.setTimestamp(4, new Timestamp(order.getOrderDate().getTime()));
                pstmt.executeUpdate();
            }
            
            // Commit transaction
            conn.commit();
            System.out.println("Order processed successfully!");
            System.out.println("Product ID: " + order.getProductId());
            System.out.println("Quantity: " + order.getQuantity());
            System.out.println("Customer: " + order.getCustomerName());
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back due to error");
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}

// ECommerceOrderSystem.java
public class ECommerceOrderSystem {
    public static void main(String[] args) {
        AtomicOrderProcessor processor = new AtomicOrderProcessor();
        
        try {
            // Valid order
            Order order1 = new Order(1L, 2, "John Doe");
            processor.processOrder(order1);
            
            System.out.println("\n--- Testing insufficient stock ---");
            
            // Invalid order (insufficient stock)
            Order order2 = new Order(1L, 100, "Jane Smith");
            processor.processOrder(order2);
            
        } catch (InsufficientStockException e) {
            System.err.println("Order failed: " + e.getMessage());
        }
    }
}