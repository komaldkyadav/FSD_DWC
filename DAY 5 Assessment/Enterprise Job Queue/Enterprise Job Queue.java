// QueueWorker.java
public interface QueueWorker {
    void processNextJob();
}

// EnterpriseConnectionFactory.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class EnterpriseConnectionFactory {
    protected static final String URL = "jdbc:postgresql://localhost:5432/enterprise_db";
    protected static final String USER = "queue_admin";
    protected static final String PASSWORD = "queue_pass";
    
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// JobQueueWorker.java
import java.sql.*;

public class JobQueueWorker extends EnterpriseConnectionFactory 
        implements QueueWorker {
    
    private static final String SELECT_PENDING_JOB = 
        "SELECT j.job_id, j.created_at " +
        "FROM background_jobs j " +
        "INNER JOIN departments d ON j.dept_id = d.dept_id " +
        "WHERE j.status = ? AND d.dept_name = ? " +
        "ORDER BY j.created_at ASC " +
        "LIMIT 1 " +
        "FOR UPDATE SKIP LOCKED";
    
    private static final String UPDATE_JOB_STATUS = 
        "UPDATE background_jobs SET status = ? WHERE job_id = ?";
    
    @Override
    public void processNextJob() {
        Connection conn = null;
        
        try {
            conn = getConnection();
            
            // Find pending job with concurrency safety
            Long jobId = null;
            
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_PENDING_JOB)) {
                pstmt.setString(1, "PENDING");
                pstmt.setString(2, "Engineering");
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        jobId = rs.getLong("job_id");
                        System.out.println("Found pending job: " + jobId);
                    } else {
                        System.out.println("No pending jobs available");
                        return;
                    }
                }
            }
            
            // Process the job
            if (jobId != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_JOB_STATUS)) {
                    pstmt.setString(1, "PROCESSING");
                    pstmt.setLong(2, jobId);
                    
                    int rowsUpdated = pstmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Job " + jobId + " marked as PROCESSING");
                        System.out.println("--- Processing job " + jobId + " ---");
                        // Simulate job processing
                        Thread.sleep(1000);
                        System.out.println("Job " + jobId + " completed processing");
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error processing job: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Job processing interrupted");
        }
    }
}

// EnterpriseJobQueueSystem.java
public class EnterpriseJobQueueSystem {
    public static void main(String[] args) {
        JobQueueWorker worker = new JobQueueWorker();
        worker.processNextJob();
    }
}