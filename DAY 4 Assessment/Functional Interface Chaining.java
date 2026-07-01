// BankUser.java
public class BankUser {
    private String name;
    private int age;
    private boolean active;
    private String email;
    
    public BankUser(String name, int age, boolean active, String email) {
        this.name = name;
        this.age = age;
        this.active = active;
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public String getEmail() {
        return email;
    }
}

// UserValidator.java
import java.util.function.Predicate;

public class UserValidator {
    public static void main(String[] args) {
        System.out.println("=== Functional Interface Chaining ===\n");
        
        Predicate<BankUser> isAdult = user -> user.getAge() >= 18;
        Predicate<BankUser> isActive = BankUser::isActive;
        Predicate<BankUser> isCorporate = user -> 
            user.getEmail() != null && user.getEmail().endsWith("@enterprise.com");
        
        Predicate<BankUser> compositeRule = isAdult
                .and(isActive)
                .and(isCorporate);
        
        BankUser validUser = new BankUser("John Doe", 25, true, "john.doe@enterprise.com");
        BankUser underageUser = new BankUser("Jane Smith", 17, true, "jane.smith@enterprise.com");
        BankUser inactiveUser = new BankUser("Bob Johnson", 30, false, "bob.johnson@enterprise.com");
        BankUser nonCorporateUser = new BankUser("Alice Brown", 28, true, "alice.brown@gmail.com");
        
        System.out.println("Composite Rule: Adult AND Active AND Corporate Email");
        System.out.println("\nTest Results:");
        System.out.println("  Valid User: " + compositeRule.test(validUser));
        System.out.println("  Underage: " + compositeRule.test(underageUser));
        System.out.println("  Inactive: " + compositeRule.test(inactiveUser));
        System.out.println("  Non-Corporate: " + compositeRule.test(nonCorporateUser));
        
        System.out.println("\n=== Predicate Chaining Analysis ===");
        System.out.println("isAdult.and(isActive).and(isCorporate)");
        System.out.println("Each Predicate is independently testable");
        System.out.println("Short-circuits on first false condition");
        System.out.println("Modular business logic without nested if-else");
    }
}