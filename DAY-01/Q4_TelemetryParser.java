class HardwareLockException extends Exception {
    HardwareLockException(String msg) {
        super(msg);
    }
}
class SensorCorruptionException extends RuntimeException {
    SensorCorruptionException(String msg) {
        super(msg);
    }
}
class TelemetryStream implements AutoCloseable {
    TelemetryStream() {
        System.out.println("Stream Opened");
    }
    String readData(int line) throws HardwareLockException {
        if (line == 2) {
            throw new HardwareLockException("File locked at line " + line);
        }
        if (line == 3) {
            throw new SensorCorruptionException("Invalid sensor value at line " + line);
        }
        return "Telemetry Data Line " + line;
    }
    @Override
    public void close() {
        System.out.println("Stream Closed");
    }
}
public class Q4_TelemetryParser {
    static void parseData() throws HardwareLockException {
        try (TelemetryStream t = new TelemetryStream()) {
            for (int i = 1; i <= 4; i++) {
                try {
                    String data = t.readData(i);
                    System.out.println(data);
                } catch (SensorCorruptionException e) {
                    System.out.println("Warning: " + e.getMessage());
                }
            }
        }
    }
    public static void main(String[] args) {
        try {
            parseData();
        } catch (HardwareLockException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}