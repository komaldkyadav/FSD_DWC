// PricingStrategy.java
public interface PricingStrategy {
    double calculate(double total);
}

// BlackFridayStrategy.java
public class BlackFridayStrategy implements PricingStrategy {
    @Override
    public double calculate(double total) {
        double discount = total * 0.20;
        double finalPrice = total - discount;
        System.out.println("BlackFriday 20% off: -$" + String.format("%.2f", discount));
        return finalPrice;
    }
}

// VIPStrategy.java
public class VIPStrategy implements PricingStrategy {
    @Override
    public double calculate(double total) {
        double discount = 50.00;
        double finalPrice = total - discount;
        System.out.println("VIP $50 flat off: -$" + String.format("%.2f", discount));
        return finalPrice;
    }
}

// SeasonalStrategy.java
public class SeasonalStrategy implements PricingStrategy {
    @Override
    public double calculate(double total) {
        double discount = total * 0.10;
        double finalPrice = total - discount;
        System.out.println("Seasonal 10% off: -$" + String.format("%.2f", discount));
        return finalPrice;
    }
}

// CheckoutEngine.java
public class CheckoutEngine {
    public static void main(String[] args) {
        double baseTotal = 500.00;
        System.out.println("=== Dynamic Strategy Orchestrator ===\n");
        System.out.println("Base Total: $" + String.format("%.2f", baseTotal));
        
        PricingStrategy[] activeDiscounts = {
            new BlackFridayStrategy(),
            new VIPStrategy(),
            new SeasonalStrategy()
        };
        
        System.out.println("\nApplying discounts sequentially...");
        double currentTotal = baseTotal;
        
        for (PricingStrategy strategy : activeDiscounts) {
            System.out.print("Before: $" + String.format("%.2f", currentTotal) + " -> ");
            currentTotal = strategy.calculate(currentTotal);
            System.out.println("After: $" + String.format("%.2f", currentTotal));
        }
        
        System.out.println("\n=== Final Results ===");
        System.out.println("Original total: $" + String.format("%.2f", baseTotal));
        System.out.println("Final total after all discounts: $" + String.format("%.2f", currentTotal));
        System.out.println("Total savings: $" + String.format("%.2f", (baseTotal - currentTotal)));
        
        System.out.println("\n=== Memory Analysis ===");
        System.out.println("PricingStrategy[] activeDiscounts: Array storing memory addresses (references) in Heap");
        System.out.println("The JVM doesn't care what actual objects are, as long as they implement PricingStrategy");
        System.out.println("strategy.calculate() relies on Dynamic Method Dispatch");
        System.out.println("Checkout code is closed for modification but open for extension (Open/Closed Principle)");
        System.out.println("You can add 50 new discount classes without changing the for loop logic");
        
        System.out.println("\n=== Dynamic Strategy Addition Demonstration ===");
        System.out.println("Adding additional discount strategies dynamically...");
        
        PricingStrategy[] extendedDiscounts = new PricingStrategy[activeDiscounts.length + 1];
        System.arraycopy(activeDiscounts, 0, extendedDiscounts, 0, activeDiscounts.length);
        extendedDiscounts[extendedDiscounts.length - 1] = new HolidayBonusStrategy();
        
        double newTotal = baseTotal;
        for (PricingStrategy strategy : extendedDiscounts) {
            newTotal = strategy.calculate(newTotal);
        }
        System.out.println("Final total with Holiday Bonus: $" + String.format("%.2f", newTotal));
    }
}

// HolidayBonusStrategy.java (Additional strategy for demonstration)
class HolidayBonusStrategy implements PricingStrategy {
    @Override
    public double calculate(double total) {
        double bonus = 25.00;
        double finalPrice = total - bonus;
        System.out.println("Holiday Bonus $25 off: -$" + String.format("%.2f", bonus));
        return finalPrice;
    }
}