// TelemetryService.java
public interface TelemetryService {
    void printLatestLocations();
}

// FleetDatabaseConnection.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class FleetDatabaseConnection {
    protected static final String URL = "jdbc:postgresql://localhost:5432/fleet_db";
    protected static final String USER = "fleet_admin";
    protected static final String PASSWORD = "fleet_pass";
    
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// HimalayanFleetTracker.java
import java.sql.*;
import java.time.LocalDateTime;

public class HimalayanFleetTracker extends FleetDatabaseConnection 
        implements TelemetryService {
    
    private static final String LATEST_LOCATIONS = 
        "SELECT r.rider_id, r.rider_name, r.bike_model, " +
        "       p.latitude, p.longitude, p.recorded_at " +
        "FROM riders r " +
        "INNER JOIN ( " +
        "    SELECT rider_id, latitude, longitude, recorded_at, " +
        "           ROW_NUMBER() OVER(PARTITION BY rider_id ORDER BY recorded_at DESC) as rn " +
        "    FROM gps_pings " +
        ") p ON r.rider_id = p.rider_id " +
        "WHERE p.rn = 1";
    
    @Override
    public void printLatestLocations() {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(LATEST_LOCATIONS);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("=== Himalayan Fleet Tracker ===");
            System.out.println("Latest GPS Pings for All Riders\n");
            
            while (rs.next()) {
                long riderId = rs.getLong("rider_id");
                String riderName = rs.getString("rider_name");
                String bikeModel = rs.getString("bike_model");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                
                // Modern Java 8+ Date/Time mapping
                LocalDateTime recordedAt = rs.getObject("recorded_at", LocalDateTime.class);
                
                System.out.printf("Rider: %s (%s)%n", riderName, bikeModel);
                System.out.printf("  Location: %.6f, %.6f%n", latitude, longitude);
                System.out.printf("  Recorded: %s%n%n", recordedAt);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching telemetry data: " + e.getMessage());
        }
    }
}

// HimalayanTrackingSystem.java
public class HimalayanTrackingSystem {
    public static void main(String[] args) {
        HimalayanFleetTracker tracker = new HimalayanFleetTracker();
        tracker.printLatestLocations();
    }
}