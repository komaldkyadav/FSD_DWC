// UserSearchEngine.java
import java.sql.*;

public class UserSearchEngine {
    private static final String URL = "jdbc:postgresql://localhost:5432/bank_db";
    private static final String USER = "bank_admin";
    private static final String PASSWORD = "bank_pass";
    
    public void searchUsersUnsafe(String emailPattern) {
        System.out.println("\n=== UNSAFE SEARCH ===");
        System.out.println("Search pattern: " + emailPattern);
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // VULNERABLE: String concatenation in SQL
            String query = "SELECT * FROM users WHERE email LIKE '%" + emailPattern + "%'";
            System.out.println("Executing SQL: " + query);
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println("Found: " + rs.getString("email"));
                }
                System.out.println("Total results: " + count);
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public void searchUsersSafe(String emailPattern) {
        System.out.println("\n=== SAFE SEARCH ===");
        System.out.println("Search pattern: " + emailPattern);
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // SAFE: Using PreparedStatement with bound parameters
            String query = "SELECT * FROM users WHERE email LIKE ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                // Bind wildcards safely in the parameter
                pstmt.setString(1, "%" + emailPattern + "%");
                
                System.out.println("Executing safe SQL with bound parameter");
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        System.out.println("Found: " + rs.getString("email"));
                    }
                    System.out.println("Total results: " + count);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

// SecurityTestSystem.java
public class SecurityTestSystem {
    public static void main(String[] args) {
        UserSearchEngine engine = new UserSearchEngine();
        
        System.out.println("=== Malicious Wildcard Injection Demo ===\n");
        
        // Malicious input attempting SQL injection
        String maliciousInput = "' OR '1'='1";
        
        System.out.println("Attack Vector: " + maliciousInput);
        
        // UNSAFE - Will dump entire table
        engine.searchUsersUnsafe(maliciousInput);
        
        // SAFE - Will find nothing
        engine.searchUsersSafe(maliciousInput);
        
        System.out.println("\n=== Security Analysis ===");
        System.out.println("UNSAFE: String concatenation defeats PreparedStatement");
        System.out.println("SAFE: Bound parameters properly sanitize wildcards");
        System.out.println("SQL injection prevented by treating input as literal string");
    }
}