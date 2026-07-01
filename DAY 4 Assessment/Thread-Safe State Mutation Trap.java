import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryCounter {
    public void demonstrateRaceCondition() {
        System.out.println("=== Thread-Safe State Mutation Trap ===\n");
        
        List<Integer> inventory = Arrays.asList(5, 8, 3, 10, 6, 4, 9, 7, 2, 1);
        
        System.out.println("Inventory Items: " + inventory);
        
        // Broken parallel stream with external counter
        Counter badCounter = new Counter();
        System.out.println("\n1. BROKEN - Parallel stream with external mutable state:");
        
        for (int i = 0; i < 5; i++) {
            badCounter.reset();
            inventory.parallelStream().forEach(item -> badCounter.add(item));
            System.out.println("  Attempt " + (i+1) + ": Total = " + badCounter.getTotal());
        }
        System.out.println("  Expected: 55 (sum of all items)");
        System.out.println("  Race condition causes inconsistent results!");
        
        // Correct solution using reduce
        System.out.println("\n2. CORRECT - Parallel stream with reduce:");
        
        for (int i = 0; i < 5; i++) {
            int total = inventory.parallelStream()
                    .reduce(0, (acc, curr) -> acc + curr);
            System.out.println("  Attempt " + (i+1) + ": Total = " + total);
        }
        System.out.println("  Expected: 55 (consistent every time!)");
        
        System.out.println("\n=== Memory Analysis ===");
        System.out.println("parallelStream().forEach(): Multiple threads mutate shared state");
        System.out.println("badCounter.add() is not synchronized - race condition");
        System.out.println("reduce(): Pure, side-effect-free aggregation");
        System.out.println("JVM safely orchestrates parallel chunks and combines results");
        System.out.println("No external state mutation - thread-safe by design");
    }
}

class Counter {
    private int total = 0;
    
    public void add(int value) {
        total += value;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void reset() {
        total = 0;
    }
}

// MainSystem.java
public class MainSystem {
    public static void main(String[] args) {
        InventoryCounter counter = new InventoryCounter();
        counter.demonstrateRaceCondition();
        
        System.out.println("\n=== Key Takeaways ===");
        System.out.println("1. Avoid mutable external state in parallel streams");
        System.out.println("2. Use reduce() for safe parallel aggregation");
        System.out.println("3. reduce() operations must be associative and stateless");
        System.out.println("4. Parallel streams are only beneficial for large datasets");
    }
}