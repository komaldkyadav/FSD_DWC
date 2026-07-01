// RegistrationManager.java
public interface RegistrationManager {
    void enrollAtRiskStudents();
}

// DatabaseConnectionProvider.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConnectionProvider {
    protected static final String URL = "jdbc:postgresql://localhost:5432/edixo";
    protected static final String USER = "edixo_admin";
    protected static final String PASSWORD = "edixo_pass";
    
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// EdixoRegistrationRepository.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EdixoRegistrationRepository extends DatabaseConnectionProvider 
        implements RegistrationManager {
    
    private static final String FIND_AT_RISK = 
        "SELECT s.student_id, s.full_name " +
        "FROM students s " +
        "LEFT JOIN course_registrations cr ON s.student_id = cr.student_id " +
        "WHERE cr.student_id IS NULL";
    
    private static final String INSERT_REGISTRATION = 
        "INSERT INTO course_registrations (student_id, course_code, semester) " +
        "VALUES (?, 'ORIENTATION_101', 'FALL_2024')";
    
    @Override
    public void enrollAtRiskStudents() {
        List<Long> atRiskStudents = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement findStmt = conn.prepareStatement(FIND_AT_RISK);
             ResultSet rs = findStmt.executeQuery()) {
            
            System.out.println("=== At-Risk Students Report ===");
            while (rs.next()) {
                long studentId = rs.getLong("student_id");
                String fullName = rs.getString("full_name");
                atRiskStudents.add(studentId);
                System.out.println("Student ID: " + studentId + ", Name: " + fullName);
            }
            
            if (!atRiskStudents.isEmpty()) {
                enrollStudents(conn, atRiskStudents);
            }
            
        } catch (SQLException e) {
            System.err.println("Error processing at-risk students: " + e.getMessage());
        }
    }
    
    private void enrollStudents(Connection conn, List<Long> studentIds) 
            throws SQLException {
        
        try (PreparedStatement pstmt = conn.prepareStatement(INSERT_REGISTRATION)) {
            
            int batchSize = 1000;
            int count = 0;
            
            for (Long studentId : studentIds) {
                pstmt.setLong(1, studentId);
                pstmt.addBatch();
                count++;
                
                if (count % batchSize == 0) {
                    pstmt.executeBatch();
                    System.out.println("Enrolled " + count + " students in batch");
                }
            }
            
            if (count % batchSize != 0) {
                pstmt.executeBatch();
            }
            
            System.out.println("Total " + count + " students enrolled in Orientation 101");
        }
    }
}

// EdixoEnrollmentSystem.java
public class EdixoEnrollmentSystem {
    public static void main(String[] args) {
        EdixoRegistrationRepository repo = new EdixoRegistrationRepository();
        repo.enrollAtRiskStudents();
    }
}