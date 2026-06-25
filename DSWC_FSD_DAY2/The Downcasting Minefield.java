// Vehicle.java
public abstract class Vehicle {
    protected String model;
    
    public Vehicle(String model) {
        this.model = model;
    }
    
    public abstract void drive();
    
    public String getModel() {
        return model;
    }
}

// GasCar.java
public class GasCar extends Vehicle {
    private double fuelLevel;
    
    public GasCar(String model, double fuelLevel) {
        super(model);
        this.fuelLevel = fuelLevel;
    }
    
    @Override
    public void drive() {
        System.out.println("GasCar " + model + " driving with fuel level: " + fuelLevel + "%");
    }
    
    public void refuel() {
        System.out.println("Refueling GasCar " + model);
        fuelLevel = 100;
    }
}

// ElectricCar.java
public class ElectricCar extends Vehicle {
    private int batteryPercentage;
    
    public ElectricCar(String model, int batteryPercentage) {
        super(model);
        this.batteryPercentage = batteryPercentage;
    }
    
    @Override
    public void drive() {
        System.out.println("ElectricCar " + model + " driving with battery: " + batteryPercentage + "%");
    }
    
    public void updateFirmware() {
        System.out.println("Updating firmware for ElectricCar " + model);
        System.out.println("Firmware update complete. Battery optimized.");
    }
    
    public void charge() {
        System.out.println("Charging ElectricCar " + model);
        batteryPercentage = 100;
    }
}

// FleetManager.java
public class FleetManager {
    public static void main(String[] args) {
        System.out.println("=== The Downcasting Minefield ===\n");
        
        Vehicle[] fleet = {
            new GasCar("Toyota Camry", 65),
            new ElectricCar("Tesla Model S", 45),
            new GasCar("Honda Accord", 30),
            new ElectricCar("Nissan Leaf", 78),
            new GasCar("Ford Mustang", 55)
        };
        
        System.out.println("Processing fleet with safe downcasting...\n");
        
        for (Vehicle vehicle : fleet) {
            System.out.println("Vehicle: " + vehicle.getModel());
            vehicle.drive();
            
            // Safe downcasting with instanceof verification
            if (vehicle instanceof ElectricCar) {
                ElectricCar electricCar = (ElectricCar) vehicle;
                System.out.println("-> Electric vehicle detected. Updating firmware...");
                electricCar.updateFirmware();
            } else if (vehicle instanceof GasCar) {
                GasCar gasCar = (GasCar) vehicle;
                System.out.println("-> Gas vehicle detected. Checking fuel level...");
                if (gasCar.getModel().equals("Ford Mustang")) {
                    gasCar.refuel();
                }
            }
            System.out.println();
        }
        
        System.out.println("=== Memory Analysis ===");
        System.out.println("v.drive() works because compiler verifies drive() exists in Vehicle reference class");
        System.out.println("v.updateFirmware() would cause Compile-Time Error without instanceof");
        System.out.println("Blind downcasting ((ElectricCar) v) overrides compiler check");
        System.out.println("JVM examines Heap at runtime and throws ClassCastException if object doesn't match");
        System.out.println("instanceof acts as memory inspector - checks if Heap object structurally matches class blueprint");
        System.out.println("If match found, safely repoints ElectricCar Stack reference to that Heap object");
        
        System.out.println("\n=== Dangerous Pattern Demonstration (Commented Out) ===");
        System.out.println("// For demonstration of what NOT to do:");
        System.out.println("// for (Vehicle v : fleet) {");
        System.out.println("//     ((ElectricCar) v).updateFirmware(); // This would crash on GasCar!");
        System.out.println("// }");
        
        System.out.println("\n=== Safe Pattern Summary ===");
        System.out.println("1. Always use instanceof before downcasting");
        System.out.println("2. Handle different types appropriately");
        System.out.println("3. Provide fallback logic for unexpected types");
        System.out.println("4. Never assume all objects in a polymorphic array are of the same subtype");
    }
}

// BatchProcessor.java (Additional demonstration)
public class BatchProcessor {
    public static void processFleetForUpdates(Vehicle[] vehicles) {
        int electricCount = 0;
        int gasCount = 0;
        int updatedCount = 0;
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle instanceof ElectricCar) {
                electricCount++;
                ElectricCar ev = (ElectricCar) vehicle;
                ev.updateFirmware();
                updatedCount++;
            } else if (vehicle instanceof GasCar) {
                gasCount++;
                GasCar gc = (GasCar) vehicle;
                System.out.println("GasCar " + gc.getModel() + " doesn't need firmware updates");
            }
        }
        
        System.out.println("\n=== Batch Processing Summary ===");
        System.out.println("Total vehicles processed: " + vehicles.length);
        System.out.println("Electric vehicles: " + electricCount);
        System.out.println("Gas vehicles: " + gasCount);
        System.out.println("Vehicles updated: " + updatedCount);
    }
    
    public static void main(String[] args) {
        Vehicle[] testFleet = {
            new ElectricCar("Tesla Model 3", 60),
            new ElectricCar("Chevy Bolt", 40),
            new GasCar("Toyota Corolla", 80)
        };
        
        System.out.println("=== Batch Processing Example ===\n");
        processFleetForUpdates(testFleet);
    }
}