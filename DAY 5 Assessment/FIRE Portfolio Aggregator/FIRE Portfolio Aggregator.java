// PortfolioManager.java
public interface PortfolioManager {
    void restructurePortfolio(long investorId);
}

// FinancialDatabaseConfig.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class FinancialDatabaseConfig {
    protected static final String URL = "jdbc:postgresql://localhost:5432/fire_db";
    protected static final String USER = "fire_admin";
    protected static final String PASSWORD = "fire_pass";
    
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// FinancialPortfolioManager.java
import java.sql.*;

public class FinancialPortfolioManager extends FinancialDatabaseConfig 
        implements PortfolioManager {
    
    private static final String SELECT_PORTFOLIO = 
        "SELECT h.asset_class, SUM(h.current_value) as total_value " +
        "FROM investors i " +
        "INNER JOIN holdings h ON i.investor_id = h.investor_id " +
        "WHERE i.investor_id = ? " +
        "GROUP BY h.asset_class";
    
    private static final String UPDATE_HOLDING = 
        "UPDATE holdings SET current_value = ? " +
        "WHERE investor_id = ? AND asset_class = ?";
    
    @Override
    public void restructurePortfolio(long investorId) {
        Connection conn = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            System.out.println("=== Portfolio Restructuring ===");
            System.out.println("Investor ID: " + investorId);
            
            // Read current portfolio
            double debtValue = 0;
            double equityValue = 0;
            
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_PORTFOLIO)) {
                pstmt.setLong(1, investorId);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String assetClass = rs.getString("asset_class");
                        double totalValue = rs.getDouble("total_value");
                        
                        if ("Debt".equals(assetClass)) {
                            debtValue = totalValue;
                        } else if ("Equity".equals(assetClass)) {
                            equityValue = totalValue;
                        }
                        
                        System.out.println("Asset Class: " + assetClass + 
                                         ", Value: $" + totalValue);
                    }
                }
            }
            
            // Calculate shift amount
            double shiftAmount = 50000.0;
            
            if (debtValue >= shiftAmount) {
                // Execute updates
                try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_HOLDING)) {
                    // Reduce Debt
                    pstmt.setDouble(1, debtValue - shiftAmount);
                    pstmt.setLong(2, investorId);
                    pstmt.setString(3, "Debt");
                    pstmt.executeUpdate();
                    
                    // Increase Equity
                    pstmt.setDouble(1, equityValue + shiftAmount);
                    pstmt.setLong(2, investorId);
                    pstmt.setString(3, "Equity");
                    pstmt.executeUpdate();
                    
                    // Commit transaction
                    conn.commit();
                    
                    System.out.println("\nTransaction Committed Successfully!");
                    System.out.println("Transferred $" + shiftAmount + " from Debt to Equity");
                }
            } else {
                System.out.println("\nInsufficient Debt value to transfer $" + shiftAmount);
                conn.rollback();
            }
            
        } catch (SQLException e) {
            System.err.println("Error during portfolio restructuring: " + e.getMessage());
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

// FIREPortfolioSystem.java
public class FIREPortfolioSystem {
    public static void main(String[] args) {
        FinancialPortfolioManager manager = new FinancialPortfolioManager();
        manager.restructurePortfolio(1L);
    }
}