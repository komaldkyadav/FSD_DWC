// DatabaseConfig.java
public abstract class DatabaseConfig {
    public DatabaseConfig() {
        System.out.println("DatabaseConfig constructor starting...");
        setup();
        System.out.println("DatabaseConfig constructor completed");
    }
    
    public void setup() {
        System.out.println("DatabaseConfig setup() - Parent implementation");
    }
}

// SecureConfig.java
public class SecureConfig extends DatabaseConfig {
    private String authToken;
    
    public SecureConfig() {
        super();
        System.out.println("SecureConfig constructor starting...");
        this.authToken = "Bearer123";
        System.out.println("SecureConfig constructor completed");
    }
    
    @Override
    public void setup() {
        System.out.println("SecureConfig setup() - authToken value: " + authToken);
        if (authToken == null) {
            System.out.println("ERROR: authToken is null - premature dispatch!");
        }
    }
    
    public String getAuthToken() {
        return authToken;
    }
}

// ConfigurationService.java
public class ConfigurationService {
    public static void main(String[] args) {
        System.out.println("=== Premature Dispatch Trap ===");
        System.out.println("Creating SecureConfig instance...");
        SecureConfig config = new SecureConfig();
        
        System.out.println("\n=== Memory Initialization Order Analysis ===");
        System.out.println("1. Heap memory allocated for SecureConfig");
        System.out.println("2. All reference variables set to default values (null)");
        System.out.println("3. Constructor chaining executes: DatabaseConfig() called first");
        System.out.println("4. Inside DatabaseConfig(), setup() is called");
        System.out.println("5. Due to Dynamic Method Dispatch, SecureConfig.setup() executes");
        System.out.println("6. At this point, SecureConfig constructor hasn't run yet!");
        System.out.println("7. authToken is still null");
        System.out.println("8. After setup(), SecureConfig constructor executes");
        System.out.println("9. authToken is assigned \"Bearer123\"");
        
        System.out.println("\n=== Verification ===");
        System.out.println("Final authToken value: " + config.getAuthToken());
    }
}