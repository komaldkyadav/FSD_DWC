// TemporalEntity.java
public abstract class TemporalEntity {
    protected String entityName;
    protected int originYear;
    
    public TemporalEntity(String entityName, int originYear) {
        this.entityName = entityName;
        this.originYear = originYear;
    }
    
    public String getEntityName() {
        return entityName;
    }
    
    public int getOriginYear() {
        return originYear;
    }
}

// HumanEntity.java
public class HumanEntity extends TemporalEntity {
    public HumanEntity(String entityName, int originYear) {
        super(entityName, originYear);
    }
}

// ArtifactEntity.java
public class ArtifactEntity extends TemporalEntity {
    private boolean isRadioactive;
    
    public ArtifactEntity(String entityName, int originYear, boolean isRadioactive) {
        super(entityName, originYear);
        this.isRadioactive = isRadioactive;
    }
    
    public boolean isRadioactive() {
        return isRadioactive;
    }
}

// HistoricalEvent.java
import java.util.List;

public class HistoricalEvent {
    private List<TemporalEntity> entities;
    private int eventYear;
    
    public HistoricalEvent(List<TemporalEntity> entities, int eventYear) {
        this.entities = entities;
        this.eventYear = eventYear;
    }
    
    public List<TemporalEntity> getEntities() {
        return entities;
    }
    
    public int getEventYear() {
        return eventYear;
    }
}

// ParadoxChecker.java
@FunctionalInterface
public interface ParadoxChecker {
    boolean isParadox(TemporalEntity entity, int eventYear);
}

// ThreatMapper.java
@FunctionalInterface
public interface ThreatMapper {
    String mapThreat(TemporalEntity entity);
}

// ParadoxAnalyzer.java
import java.util.List;
import java.util.stream.Collectors;

public class ParadoxAnalyzer {
    public List<String> detectParadoxes(List<HistoricalEvent> timeline,
                                       ParadoxChecker checker,
                                       ThreatMapper mapper) {
        return timeline.stream()
                .flatMap(event -> event.getEntities().stream()
                        .filter(entity -> checker.isParadox(entity, event.getEventYear()))
                        .map(mapper::mapThreat))
                .collect(Collectors.toList());
    }
}

// TemporalParadoxSystem.java
import java.util.Arrays;
import java.util.List;

public class TemporalParadoxSystem {
    public static void main(String[] args) {
        ParadoxAnalyzer analyzer = new ParadoxAnalyzer();
        
        List<HistoricalEvent> timeline = Arrays.asList(
            new HistoricalEvent(
                Arrays.asList(
                    new HumanEntity("Albert Einstein", 1879),
                    new ArtifactEntity("Time Machine Blueprint", 1950, false)
                ),
                1905
            ),
            new HistoricalEvent(
                Arrays.asList(
                    new ArtifactEntity("Ancient Computer", 3000, true),
                    new HumanEntity("Future Woman", 2500)
                ),
                2024
            ),
            new HistoricalEvent(
                Arrays.asList(
                    new HumanEntity("Leonardo da Vinci", 1452),
                    new ArtifactEntity("Alien Artifact", 3000, true)
                ),
                1500
            )
        );
        
        ParadoxChecker checker = (entity, eventYear) -> 
            entity.getOriginYear() > eventYear;
        
        ThreatMapper mapper = (entity) -> 
            entity.getEntityName() + " detected out of time!";
        
        System.out.println("=== Temporal Paradox Analyzer ===\n");
        System.out.println("Analyzing " + timeline.size() + " historical events...");
        
        List<String> paradoxes = analyzer.detectParadoxes(timeline, checker, mapper);
        
        System.out.println("\nDetected " + paradoxes.size() + " temporal paradoxes:");
        paradoxes.forEach(System.out::println);
        
        System.out.println("\n=== Stream Pipeline Analysis ===");
        System.out.println("1. flatMap: Flattens List<HistoricalEvent> to TemporalEntity stream");
        System.out.println("2. filter: ParadoxChecker - maintains eventYear context");
        System.out.println("3. map: ThreatMapper - formats paradox messages");
        System.out.println("4. collect: Returns List<String>");
        
        System.out.println("\n=== Advanced flatMap Usage ===");
        System.out.println("Maintains parent context (eventYear) while processing children");
        System.out.println("Filters paradoxes before mapping to avoid unnecessary transformations");
    }
}