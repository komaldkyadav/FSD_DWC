// DNASample.java
public abstract class DNASample {
    protected String sampleId;
    protected double purityPercentage;
    
    public DNASample(String sampleId, double purityPercentage) {
        this.sampleId = sampleId;
        this.purityPercentage = purityPercentage;
    }
    
    public String getSampleId() {
        return sampleId;
    }
    
    public double getPurityPercentage() {
        return purityPercentage;
    }
}

// HumanSample.java
public class HumanSample extends DNASample {
    private String bloodType;
    
    public HumanSample(String sampleId, double purityPercentage, String bloodType) {
        super(sampleId, purityPercentage);
        this.bloodType = bloodType;
    }
    
    public String getBloodType() {
        return bloodType;
    }
}

// AlienSample.java
public class AlienSample extends DNASample {
    private boolean isSiliconBased;
    
    public AlienSample(String sampleId, double purityPercentage, boolean isSiliconBased) {
        super(sampleId, purityPercentage);
        this.isSiliconBased = isSiliconBased;
    }
    
    public boolean isSiliconBased() {
        return isSiliconBased;
    }
}

// ViabilityChecker.java
@FunctionalInterface
public interface ViabilityChecker {
    boolean isViable(DNASample sample);
}

// GenomeMapper.java
@FunctionalInterface
public interface GenomeMapper {
    String mapGenome(DNASample sample);
}

// Sequencer.java
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sequencer {
    public Map<String, List<String>> classifyGenomes(List<DNASample> samples,
                                                     ViabilityChecker checker,
                                                     GenomeMapper mapper) {
        return samples.stream()
                .filter(checker::isViable)
                .collect(Collectors.groupingBy(
                    sample -> sample.getClass().getSimpleName(),
                    Collectors.mapping(mapper::mapGenome, Collectors.toList())
                ));
    }
}

// GeneWeaverSystem.java
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GeneWeaverSystem {
    public static void main(String[] args) {
        Sequencer sequencer = new Sequencer();
        
        List<DNASample> samples = Arrays.asList(
            new HumanSample("H001", 95.0, "A+"),
            new AlienSample("A001", 85.0, false),
            new HumanSample("H002", 75.0, "O-"),
            new AlienSample("A002", 90.0, true),
            new HumanSample("H003", 88.0, "B+"),
            new AlienSample("A003", 60.0, false),
            new HumanSample("H004", 92.0, "AB-"),
            new AlienSample("A004", 82.0, true)
        );
        
        ViabilityChecker checker = (sample) -> 
            sample.getPurityPercentage() >= 80.0;
        
        GenomeMapper mapper = (sample) -> {
            if (sample instanceof HumanSample) {
                HumanSample human = (HumanSample) sample;
                return "ID: " + sample.getSampleId() + " (Type: " + human.getBloodType() + ")";
            } else if (sample instanceof AlienSample) {
                AlienSample alien = (AlienSample) sample;
                return "ID: " + sample.getSampleId() + " (Silicon: " + alien.isSiliconBased() + ")";
            }
            return "ID: " + sample.getSampleId();
        };
        
        System.out.println("=== GeneWeaver DNA Genome Classifier ===\n");
        System.out.println("Processing " + samples.size() + " DNA samples...");
        
        Map<String, List<String>> classified = sequencer.classifyGenomes(samples, checker, mapper);
        
        System.out.println("\nClassified " + 
            classified.values().stream().mapToInt(List::size).sum() + 
            " viable samples:");
        
        classified.forEach((className, genomes) -> {
            System.out.println("\n" + className + ":");
            genomes.forEach(genome -> System.out.println("  - " + genome));
        });
        
        System.out.println("\n=== Stream Pipeline Analysis ===");
        System.out.println("1. Filter: ViabilityChecker - removes unviable samples");
        System.out.println("2. Collect: groupingBy() - groups by class name");
        System.out.println("3. Downstream: mapping() - applies GenomeMapper");
        System.out.println("4. Collect: Returns Map<String, List<String>>");
        
        System.out.println("\n=== Advanced Collectors Logic ===");
        System.out.println("groupingBy with downstream mapping() transforms after grouping");
        System.out.println("Separates HumanSample and AlienSample classifications");
    }
}