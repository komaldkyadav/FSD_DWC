// AuditTracker.java
public interface AuditTracker {
    default void log() {
        System.out.println("AuditTracker: Logging audit information");
    }
}

// PerformanceTracker.java
public interface PerformanceTracker {
    default void log() {
        System.out.println("PerformanceTracker: Logging performance metrics");
    }
}

// MasterService.java
public class MasterService implements AuditTracker, PerformanceTracker {
    @Override
    public void log() {
        System.out.println("MasterService: Resolving diamond collision using super keyword");
        System.out.println("Calling AuditTracker's log method:");
        AuditTracker.super.log();
        System.out.println("Calling PerformanceTracker's log method:");
        PerformanceTracker.super.log();
        System.out.println("MasterService: Combined logging complete");
    }
    
    public void customLog() {
        System.out.println("MasterService: Custom logging with both implementations");
        AuditTracker.super.log();
        PerformanceTracker.super.log();
    }
}

// DiamondCollisionDemo.java
public class DiamondCollisionDemo {
    public static void main(String[] args) {
        System.out.println("=== Default Method Diamond Collision Resolution ===\n");
        
        MasterService service = new MasterService();
        
        System.out.println("1. Calling overridden log() method:");
        service.log();
        
        System.out.println("\n2. Calling customLog() method:");
        service.customLog();
        
        System.out.println("\n=== Memory Analysis ===");
        System.out.println("The JVM detects the collision at compile-time");
        System.out.println("The implementing class MUST override the conflicting method");
        System.out.println("Using InterfaceName.super.methodName() allows precise routing");
        System.out.println("This enables unambiguous Late Binding in memory");
        
        System.out.println("\n=== Compilation Behavior ===");
        System.out.println("If MasterService didn't override log(): Compilation Error");
        System.out.println("Resolution: Override and use Interface.super for specific implementations");
    }
}