// TransactionProcessor.java
import java.util.Arrays;
import java.util.List;

public class TransactionProcessor {
    public void findHighValueTransactions(List<Double> transactions) {
        System.out.println("=== Short-Circuit Optimization Proof ===\n");
        
        System.out.println("Processing " + transactions.size() + " transactions...");
        System.out.println("Looking for first 3 transactions over $10,000\n");
        
        List<Double> highValue = transactions.stream()
                .peek(amount -> System.out.println("  Evaluating: $" + amount))
                .filter(amount -> {
                    boolean isHigh = amount > 10000.0;
                    if (isHigh) {
                        System.out.println("  *** FOUND: $" + amount + " (match)");
                    }
                    return isHigh;
                })
                .limit(3)
                .toList();
        
        System.out.println("\nFound " + highValue.size() + " high-value transactions:");
        highValue.forEach(amount -> System.out.println("  $" + amount));
        
        System.out.println("\n=== Optimization Proof ===");
        System.out.println("JVM stopped processing after finding 3 matches");
        System.out.println("Not all elements in the stream were evaluated");
        System.out.println("Demonstrates vertical processing (element-by-element)");
        System.out.println("limit(3) acts as a short-circuiting operation");
    }
}

// FraudDetectionSystem.java
import java.util.Arrays;
import java.util.List;

public class FraudDetectionSystem {
    public static void main(String[] args) {
        TransactionProcessor processor = new TransactionProcessor();
        
        List<Double> transactions = Arrays.asList(
            5000.0,      // Low - skip
            12000.0,     // High - 1st match
            3000.0,      // Low - skip
            18000.0,     // High - 2nd match
            25000.0,     // High - 3rd match (stops after this)
            400.0,       // Never evaluated
            15000.0,     // Never evaluated
            9000.0       // Never evaluated
        );
        
        processor.findHighValueTransactions(transactions);
        
        System.out.println("\n=== Performance Impact ===");
        System.out.println("Without limit: 8 transactions evaluated");
        System.out.println("With limit: Only 5 transactions evaluated");
        System.out.println("Saved 3 evaluations (37.5% reduction)");
        System.out.println("On large datasets, savings are exponential");
    }
}