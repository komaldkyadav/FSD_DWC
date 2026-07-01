// RecordQueryOptimizer.java
import java.sql.*;

public class RecordQueryOptimizer {
    private static final String URL = "jdbc:postgresql://localhost:5432/finance_db";
    private static final String USER = "finance_admin";
    private static final String PASSWORD = "finance_pass";
    
    public void queryRecordsUnsafe(int year) {
        System.out.println("\n=== UNSAFE QUERY (Full Table Scan) ===");
        
        String query = "SELECT * FROM records WHERE YEAR(created_at) = ?";
        
        long startTime = System.currentTimeMillis();
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, year);
            
            System.out.println("Query: " + query);
            System.out.println("Year: " + year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                System.out.println("Records found: " + count);
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + "ms");
            System.out.println("B-Tree Index BYPASSED! (Full table scan)");
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public void queryRecordsSafe(int year) {
        System.out.println("\n=== SAFE QUERY (Index Range Scan) ===");
        
        // SARGable query with range operators
        String query = "SELECT * FROM records WHERE created_at >= ? AND created_at < ?";
        
        long startTime = System.currentTimeMillis();
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            Timestamp startDate = Timestamp.valueOf(year + "-01-01 00:00:00");
            Timestamp endDate = Timestamp.valueOf((year + 1) + "-01-01 00:00:00");
            
            pstmt.setTimestamp(1, startDate);
            pstmt.setTimestamp(2, endDate);
            
            System.out.println("Query: " + query);
            System.out.println("Year: " + year);
            System.out.println("Range: " + startDate + " to " + endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                System.out.println("Records found: " + count);
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + "ms");
            System.out.println("B-Tree Index UTILIZED! (O(log N) scan)");
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

// FinancialAuditSystem.java
public class FinancialAuditSystem {
    public static void main(String[] args) {
        RecordQueryOptimizer optimizer = new RecordQueryOptimizer();
        
        System.out.println("=== B-Tree Bypass Defect Analysis ===\n");
        
        // Unsafe query (bypasses index)
        optimizer.queryRecordsUnsafe(2024);
        
        // Safe query (uses index)
        optimizer.queryRecordsSafe(2024);
        
        System.out.println("\n=== Query Optimization Explanation ===");
        System.out.println("UNSAFE: YEAR(created_at) function applied to indexed column");
        System.out.println("  → Database cannot use B-Tree index");
        System.out.println("  → Full table scan (O(n))");
        System.out.println("\nSAFE: Range operators (>= AND <) on indexed column");
        System.out.println("  → Database can use B-Tree index");
        System.out.println("  → Index range scan (O(log n))");
        System.out.println("  → Query is SARGable (Search Argument Able)");
    }
}