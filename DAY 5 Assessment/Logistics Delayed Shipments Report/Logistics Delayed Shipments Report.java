// ReportGenerator.java
public interface ReportGenerator {
    void printDelayedReport();
}

// DatabaseRepository.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseRepository {
    protected static final String URL = "jdbc:postgresql://localhost:5432/logistics";
    protected static final String USER = "admin";
    protected static final String PASSWORD = "secure_password";
    
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// LogisticsRepository.java
import java.sql.*;

public class LogisticsRepository extends DatabaseRepository implements ReportGenerator {
    private static final String QUERY = 
        "SELECT s.shipment_id, c.company_name, s.dispatch_date " +
        "FROM shipments s " +
        "INNER JOIN couriers c ON s.courier_id = c.courier_id " +
        "WHERE s.status = ? " +
        "ORDER BY s.dispatch_date DESC";
    
    @Override
    public void printDelayedReport() {
        String status = "DELAYED";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(QUERY)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("=== Delayed Shipments Report ===");
                System.out.println("Shipment ID | Courier Company | Dispatch Date");
                System.out.println("---------------------------------------------");
                
                while (rs.next()) {
                    String shipmentId = rs.getString("shipment_id");
                    String companyName = rs.getString("company_name");
                    Timestamp dispatchDate = rs.getTimestamp("dispatch_date");
                    
                    System.out.printf("%-12s | %-16s | %s%n", 
                        shipmentId, companyName, dispatchDate);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }
}

// LogisticsDashboard.java
public class LogisticsDashboard {
    public static void main(String[] args) {
        LogisticsRepository repo = new LogisticsRepository();
        repo.printDelayedReport();
    }
}