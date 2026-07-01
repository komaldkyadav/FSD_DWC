// FraudAudit.java
import java.sql.*;

public class FraudAudit {
    private static final String URL = "jdbc:postgresql://localhost:5432/payroll_db";
    private static final String USER = "payroll_admin";
    private static final String PASSWORD = "payroll_pass";
    
    private static final String QUERY = 
        "SELECT e.emp_name as employee_name, " +
        "       m.emp_name as manager_name, " +
        "       e.dept_id, " +
        "       d.dept_name " +
        "FROM employees e " +
        "INNER JOIN employees m ON e.manager_id = m.emp_id " +
        "LEFT JOIN departments d ON e.dept_id = d.dept_id " +
        "WHERE e.dept_id IS NULL " +
        "ORDER BY m.emp_name";
    
    public void detectGhostEmployees() {
        System.out.println("=== Forensic Audit: Ghost Employee Detection ===\n");
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(QUERY);
             ResultSet rs = pstmt.executeQuery()) {
            
            boolean found = false;
            
            System.out.println("Ghost Employees Detected:");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-20s | %