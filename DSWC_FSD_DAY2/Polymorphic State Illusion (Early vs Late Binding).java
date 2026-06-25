// BaseAccount.java
public class BaseAccount {
    public double interestRate = 4.5;
    
    public double getInterestRate() {
        return interestRate;
    }
}

// PremiumAccount.java
public class PremiumAccount extends BaseAccount {
    public double interestRate = 7.5;
    
    @Override
    public double getInterestRate() {
        return interestRate;
    }
}

// AccountingService.java
public class AccountingService {
    public static void main(String[] args) {
        PremiumAccount premium = new PremiumAccount();
        BaseAccount account = premium;
        
        System.out.println("=== Polymorphic State Illusion ===");
        System.out.println("Direct variable access (Early Binding): " + account.interestRate);
        System.out.println("Method call (Late Binding): " + account.getInterestRate());
        
        System.out.println("\n=== Memory Analysis ===");
        System.out.println("Stack Reference Type: BaseAccount");
        System.out.println("Heap Object Type: PremiumAccount");
        System.out.println("Variables are resolved at compile-time based on reference type");
        System.out.println("Methods are resolved at runtime based on actual object type");
        
        System.out.println("\n=== Verification ===");
        System.out.println("Direct access via PremiumAccount reference: " + premium.interestRate);
        System.out.println("Method call via PremiumAccount reference: " + premium.getInterestRate());
    }
}