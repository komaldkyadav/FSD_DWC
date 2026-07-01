// MemoryEngram.java
public abstract class MemoryEngram {
    protected String engramId;
    protected double clarityScore;
    protected boolean isCorrupted;
    
    public MemoryEngram(String engramId, double clarityScore, boolean isCorrupted) {
        this.engramId = engramId;
        this.clarityScore = clarityScore;
        this.isCorrupted = isCorrupted;
    }
    
    public String getEngramId() {
        return engramId;
    }
    
    public double getClarityScore() {
        return clarityScore;
    }
    
    public boolean isCorrupted() {
        return isCorrupted;
    }
}

// StandardEngram.java
public class StandardEngram extends MemoryEngram {
    public StandardEngram(String engramId, double clarityScore, boolean isCorrupted) {
        super(engramId, clarityScore, isCorrupted);
    }
}

// ClassifiedEngram.java
public class ClassifiedEngram extends MemoryEngram {
    private int securityClearanceLevel;
    
    public ClassifiedEngram(String engramId, double clarityScore, boolean isCorrupted, int securityClearanceLevel) {
        super(engramId, clarityScore, isCorrupted);
        this.securityClearanceLevel = securityClearanceLevel;
    }
    
    public int getSecurityClearanceLevel() {
        return securityClearanceLevel;
    }
}

// EngramValidator.java
@FunctionalInterface
public interface EngramValidator {
    boolean isValid(MemoryEngram engram);
}

// EngramTranslator.java
@FunctionalInterface
public interface EngramTranslator {
    String translate(MemoryEngram engram);
}

// CortexProcessor.java
import java.util.List;
import java.util.stream.Collectors;

public class CortexProcessor {
    public List<String> processMemories(List<MemoryEngram> engrams,
                                       EngramValidator validator,
                                       EngramTranslator translator) {
        return engrams.stream()
                .filter(validator::isValid)
                .filter(engram -> engram.getClarityScore() >= 50.0)
                .map(translator::translate)
                .collect(Collectors.toList());
    }
}

// NeuroLinkSystem.java
import java.util.Arrays;
import java.util.List;

public class NeuroLinkSystem {
    public static void main(String[] args) {
        CortexProcessor processor = new CortexProcessor();
        
        List<MemoryEngram> engrams = Arrays.asList(
            new StandardEngram("MEM-001", 85.5, false),
            new ClassifiedEngram("MEM-002", 92.0, false, 2),
            new StandardEngram("MEM-003", 45.0, false),
            new ClassifiedEngram("MEM-004", 78.0, true, 1),
            new StandardEngram("MEM-005", 95.0, false),
            new ClassifiedEngram("MEM-006", 65.0, false, 5),
            new StandardEngram("MEM-007", 30.0, true)
        );
        
        EngramValidator validator = (engram) -> {
            if (engram.isCorrupted()) {
                return false;
            }
            if (engram instanceof ClassifiedEngram) {
                ClassifiedEngram classified = (ClassifiedEngram) engram;
                return classified.getSecurityClearanceLevel() <= 3;
            }
            return true;
        };
        
        EngramTranslator translator = (engram) -> {
            return "ENGRAM-" + engram.getEngramId() + " | CLARITY: " + 
                   String.format("%.1f", engram.getClarityScore()) + "%";
        };
        
        System.out.println("=== NeuroLink Memory Engram Sorter ===\n");
        System.out.println("Processing " + engrams.size() + " memory engrams...");
        
        List<String> processedMemories = processor.processMemories(engrams, validator, translator);
        
        System.out.println("\nProcessed " + processedMemories.size() + " safe memories:");
        processedMemories.forEach(System.out::println);
        
        System.out.println("\n=== Stream Pipeline Analysis ===");
        System.out.println("1. Filter: EngramValidator - removes corrupted/restricted");
        System.out.println("2. Filter: clarityScore >= 50.0");
        System.out.println("3. Map: EngramTranslator - converts to readable format");
        System.out.println("4. Collect: Returns List<String>");
    }
}