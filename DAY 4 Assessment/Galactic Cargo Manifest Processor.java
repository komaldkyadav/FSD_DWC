// Cargo.java
public abstract class Cargo {
    protected String containerId;
    protected double valueInCredits;
    protected boolean isHazardous;
    
    public Cargo(String containerId, double valueInCredits, boolean isHazardous) {
        this.containerId = containerId;
        this.valueInCredits = valueInCredits;
        this.isHazardous = isHazardous;
    }
    
    public String getContainerId() {
        return containerId;
    }
    
    public double getValueInCredits() {
        return valueInCredits;
    }
    
    public boolean isHazardous() {
        return isHazardous;
    }
}

// StandardCargo.java
public class StandardCargo extends Cargo {
    public StandardCargo(String containerId, double valueInCredits, boolean isHazardous) {
        super(containerId, valueInCredits, isHazardous);
    }
}

// BiologicalCargo.java
public class BiologicalCargo extends Cargo {
    private boolean isShielded;
    
    public BiologicalCargo(String containerId, double valueInCredits, boolean isHazardous, boolean isShielded) {
        super(containerId, valueInCredits, isHazardous);
        this.isShielded = isShielded;
    }
    
    public boolean isShielded() {
        return isShielded;
    }
}

// CargoInspector.java
@FunctionalInterface
public interface CargoInspector {
    boolean isSafeForTransport(Cargo cargo);
}

// CargoCompressor.java
@FunctionalInterface
public interface CargoCompressor {
    String compress(Cargo cargo);
}

// ManifestProcessor.java
import java.util.List;
import java.util.stream.Collectors;

public class ManifestProcessor {
    public List<String> processManifest(List<Cargo> manifest, 
                                       CargoInspector inspector, 
                                       CargoCompressor compressor) {
        return manifest.stream()
                .filter(inspector::isSafeForTransport)
                .filter(cargo -> cargo.getValueInCredits() >= 1000.0)
                .map(compressor::compress)
                .collect(Collectors.toList());
    }
}

// GalacticFreightSystem.java
import java.util.Arrays;
import java.util.List;

public class GalacticFreightSystem {
    public static void main(String[] args) {
        ManifestProcessor processor = new ManifestProcessor();
        
        List<Cargo> manifest = Arrays.asList(
            new StandardCargo("ALPHA-99", 5000.50, false),
            new BiologicalCargo("BETA-12", 3000.00, true, true),
            new BiologicalCargo("GAMMA-45", 800.00, true, false),
            new StandardCargo("DELTA-78", 1500.00, false),
            new BiologicalCargo("EPSILON-34", 2000.00, true, false),
            new StandardCargo("ZETA-56", 4500.00, true),
            new BiologicalCargo("ETA-89", 6000.00, true, true)
        );
        
        CargoInspector inspector = (cargo) -> {
            if (cargo.isHazardous() && cargo instanceof BiologicalCargo) {
                BiologicalCargo bio = (BiologicalCargo) cargo;
                return bio.isShielded();
            }
            return !cargo.isHazardous();
        };
        
        CargoCompressor compressor = (cargo) -> {
            String id = cargo.getContainerId().length() >= 4 
                       ? cargo.getContainerId().substring(0, 4) 
                       : cargo.getContainerId();
            int value = (int) cargo.getValueInCredits();
            return id + "-" + value;
        };
        
        System.out.println("=== Galactic Cargo Manifest Processor ===\n");
        System.out.println("Processing " + manifest.size() + " cargo containers...");
        
        List<String> processedManifest = processor.processManifest(manifest, inspector, compressor);
        
        System.out.println("\nProcessed " + processedManifest.size() + " safe cargo containers:");
        processedManifest.forEach(System.out::println);
        
        System.out.println("\n=== Stream Pipeline Analysis ===");
        System.out.println("1. Filter: CargoInspector - removes unsafe cargo");
        System.out.println("2. Filter: valueInCredits >= 1000.0");
        System.out.println("3. Map: CargoCompressor - transforms to telemetry format");
        System.out.println("4. Collect: Returns List<String>");
        
        System.out.println("\n=== Business Logic ===");
        System.out.println("Hazardous BiologicalCargo without shielding is rejected");
        System.out.println("Cargo valued under 1000 credits is rejected");
        System.out.println("Compressed format: [First 4 chars of ID]-[Integer value]");
    }
}