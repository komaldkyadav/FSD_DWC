// SmartDevice.java
public abstract class SmartDevice {
    protected String deviceId;
    protected String deviceName;
    
    public SmartDevice(String deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }
    
    public abstract void runDiagnostic();
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public String getDeviceName() {
        return deviceName;
    }
}

// BatteryOperated.java
public interface BatteryOperated {
    int getBatteryLevel();
    void triggerRechargeAlert();
}

// SmartLight.java
public class SmartLight extends SmartDevice {
    public SmartLight(String deviceId, String deviceName) {
        super(deviceId, deviceName);
    }
    
    @Override
    public void runDiagnostic() {
        System.out.println("Diagnostic: SmartLight " + deviceName + " is functioning properly");
    }
}

// SmartCamera.java
public class SmartCamera extends SmartDevice implements BatteryOperated {
    private int batteryLevel;
    
    public SmartCamera(String deviceId, String deviceName, int batteryLevel) {
        super(deviceId, deviceName);
        this.batteryLevel = batteryLevel;
    }
    
    @Override
    public void runDiagnostic() {
        System.out.println("Diagnostic: SmartCamera " + deviceName + " is functioning properly");
    }
    
    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }
    
    @Override
    public void triggerRechargeAlert() {
        System.out.println("ALERT: SmartCamera " + deviceName + " needs recharge! Battery level: " + batteryLevel + "%");
    }
}

// SmartLock.java
public class SmartLock extends SmartDevice implements BatteryOperated {
    private int batteryLevel;
    
    public SmartLock(String deviceId, String deviceName, int batteryLevel) {
        super(deviceId, deviceName);
        this.batteryLevel = batteryLevel;
    }
    
    @Override
    public void runDiagnostic() {
        System.out.println("Diagnostic: SmartLock " + deviceName + " is functioning properly");
    }
    
    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }
    
    @Override
    public void triggerRechargeAlert() {
        System.out.println("ALERT: SmartLock " + deviceName + " needs recharge! Battery level: " + batteryLevel + "%");
    }
}

// HomeHub.java
public class HomeHub {
    public void executeNightlyRoutine(SmartDevice[] devices) {
        for (SmartDevice device : devices) {
            device.runDiagnostic();
            
            if (device instanceof BatteryOperated) {
                BatteryOperated batteryDevice = (BatteryOperated) device;
                if (batteryDevice.getBatteryLevel() < 20) {
                    batteryDevice.triggerRechargeAlert();
                }
            }
        }
    }
    
    public static void main(String[] args) {
        HomeHub hub = new HomeHub();
        
        SmartDevice[] devices = {
            new SmartLight("L001", "Living Room Light"),
            new SmartCamera("C001", "Front Door Camera", 15),
            new SmartLock("K001", "Main Door Lock", 45),
            new SmartCamera("C002", "Backyard Camera", 85),
            new SmartLock("K002", "Garage Lock", 10)
        };
        
        System.out.println("=== Executing Nightly Routine ===");
        hub.executeNightlyRoutine(devices);
    }
}