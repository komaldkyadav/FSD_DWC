// EngineLog.java
public abstract class EngineLog {
    protected String timestamp;
    protected double coreTemperature;
    protected boolean isAnomaly;
    
    public EngineLog(String timestamp, double coreTemperature, boolean isAnomaly) {
        this.timestamp = timestamp;
        this.coreTemperature = coreTemperature;
        this.isAnomaly = isAnomaly;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public double getCoreTemperature() {
        return coreTemperature;
    }
    
    public boolean isAnomaly() {
        return isAnomaly;
    }
}

// NominalLog.java
public class NominalLog extends EngineLog {
    public NominalLog(String timestamp, double coreTemperature, boolean isAnomaly) {
        super(timestamp, coreTemperature, isAnomaly);
    }
}

// CriticalLog.java
public class CriticalLog extends EngineLog {
    private String errorCode;
    
    public CriticalLog(String timestamp, double coreTemperature, boolean isAnomaly, String errorCode) {
        super(timestamp, coreTemperature, isAnomaly);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}

// LogAuditor.java
@FunctionalInterface
public interface LogAuditor {
    boolean isCritical(EngineLog log);
}

// HeatExtractor.java
@FunctionalInterface
public interface HeatExtractor {
    double extractTemperature(EngineLog log);
}

// TelemetryProcessor.java
import java.util.List;
import java.util.OptionalDouble;

public class TelemetryProcessor {
    public double getPeakCriticalTemp(List<EngineLog> logs,
                                     LogAuditor auditor,
                                     HeatExtractor extractor) {
        return logs.stream()
                .filter(auditor::isCritical)
                .mapToDouble(extractor::extractTemperature)
                .max()
                .orElse(0.0);
    }
}

// EngineTelemetrySystem.java
import java.util.Arrays;
import java.util.List;

public class EngineTelemetrySystem {
    public static void main(String[] args) {
        TelemetryProcessor processor = new TelemetryProcessor();
        
        List<EngineLog> logs = Arrays.asList(
            new NominalLog("2024-01-01 10:00:00", 75.5, false),
            new CriticalLog("2024-01-01 10:01:00", 120.0, true, "OVERHEAT"),
            new NominalLog("2024-01-01 10:02:00", 80.0, false),
            new CriticalLog("2024-01-01 10:03:00", 95.0, false, "LOW_PRESSURE"),
            new NominalLog("2024-01-01 10:04:00", 70.0, false),
            new CriticalLog("2024-01-01 10:05:00", 150.0, true, "OVERHEAT"),
            new CriticalLog("2024-01-01 10:06:00", 85.0, false, "OVERHEAT")
        );
        
        LogAuditor auditor = (log) -> {
            if (log.isAnomaly()) {
                return true;
            }
            if (log instanceof CriticalLog) {
                CriticalLog critical = (CriticalLog) log;
                return "OVERHEAT".equals(critical.getErrorCode());
            }
            return false;
        };
        
        HeatExtractor extractor = EngineLog::getCoreTemperature;
        
        System.out.println("=== Engine Telemetry Processor ===\n");
        System.out.println("Processing " + logs.size() + " engine logs...");
        
        double peakTemp = processor.getPeakCriticalTemp(logs, auditor, extractor);
        
        System.out.println("\nPeak critical temperature: " + peakTemp + "°C");
        
        System.out.println("\n=== Stream Pipeline Analysis ===");
        System.out.println("1. Filter: LogAuditor - keeps critical logs");
        System.out.println("2. Map: mapToDouble - converts to DoubleStream");
        System.out.println("3. Terminal: max() - finds highest temperature");
        System.out.println("4. OptionalDouble.orElse(0.0) - safe unpacking");
        
        System.out.println("\n=== Performance Optimization ===");
        System.out.println("mapToDouble() prevents auto-boxing/unboxing");
        System.out.println("Primitive streams are more memory efficient");
    }
}