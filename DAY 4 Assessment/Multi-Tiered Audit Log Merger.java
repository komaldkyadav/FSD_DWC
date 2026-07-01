// LoginLog.java
public class LoginLog {
    private String userId;
    private String ipAddress;
    private long timestamp;
    
    public LoginLog(String userId, String ipAddress) {
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}

// AuditLogProcessor.java
import java.util.*;
import java.util.stream.Collectors;

public class AuditLogProcessor {
    public Map<String, Set<String>> aggregateIPAddresses(List<LoginLog> logs) {
        return logs.stream()
                .collect(Collectors.toUnmodifiableMap(
                    LoginLog::getUserId,
                    log -> new HashSet<>(Collections.singletonList(log.getIpAddress())),
                    (existingSet, newSet) -> {
                        existingSet.addAll(newSet);
                        return existingSet;
                    }
                ));
    }
    
    public void printAuditReport(Map<String, Set<String>> userIPMap) {
        System.out.println("=== User IP Address Audit Report ===");
        userIPMap.forEach((userId, ipSet) -> {
            System.out.println("User: " + userId);
            System.out.println("  IP Addresses: " + String.join(", ", ipSet));
            System.out.println("  Total IPs: " + ipSet.size());
        });
    }
}

// SecurityAuditSystem.java
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SecurityAuditSystem {
    public static void main(String[] args) {
        AuditLogProcessor processor = new AuditLogProcessor();
        
        List<LoginLog> logs = Arrays.asList(
            new LoginLog("U-101", "192.168.1.1"),
            new LoginLog("U-102", "10.0.0.1"),
            new LoginLog("U-101", "192.168.1.2"),
            new LoginLog("U-103", "172.16.0.1"),
            new LoginLog("U-101", "192.168.1.1"),
            new LoginLog("U-102", "10.0.0.2"),
            new LoginLog("U-104", "192.168.1.5"),
            new LoginLog("U-103", "172.16.0.2")
        );
        
        System.out.println("=== Multi-Tiered Audit Log Merger ===\n");
        System.out.println("Processing " + logs.size() + " login logs...");
        
        Map<String, Set<String>> userIPMap = processor.aggregateIPAddresses(logs);
        
        processor.printAuditReport(userIPMap);
        
        System.out.println("\n=== Collectors Analysis ===");
        System.out.println("toUnmodifiableMap: Creates immutable, thread-safe map");
        System.out.println("Key: User ID (String)");
        System.out.println("Value: Set of unique IP addresses");
        System.out.println("Merge function: Combines IP sets on duplicate keys");
        System.out.println("Duplicate keys resolved without IllegalStateException");
    }
}