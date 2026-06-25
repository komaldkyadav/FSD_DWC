// DeliveryDrone.java
public abstract class DeliveryDrone {
    protected String droneId;
    
    public DeliveryDrone(String droneId) {
        this.droneId = droneId;
    }
    
    public abstract void deliverPackage();
    
    public String getDroneId() {
        return droneId;
    }
}

// Airborne.java
public interface Airborne {
    void flyToDestination();
    
    default void requestAirTrafficClearance() {
        System.out.println("Air traffic clearance requested for drone");
    }
}

// GroundBased.java
public interface GroundBased {
    void navigateSidewalks();
}

// Quadcopter.java
public class Quadcopter extends DeliveryDrone implements Airborne {
    public Quadcopter(String droneId) {
        super(droneId);
    }
    
    @Override
    public void deliverPackage() {
        System.out.println("Quadcopter " + droneId + " delivering package via air");
    }
    
    @Override
    public void flyToDestination() {
        System.out.println("Quadcopter " + droneId + " flying to destination");
    }
}

// CityRover.java
public class CityRover extends DeliveryDrone implements GroundBased {
    public CityRover(String droneId) {
        super(droneId);
    }
    
    @Override
    public void deliverPackage() {
        System.out.println("CityRover " + droneId + " delivering package via ground");
    }
    
    @Override
    public void navigateSidewalks() {
        System.out.println("CityRover " + droneId + " navigating sidewalks");
    }
}

// HybridVTOL.java
public class HybridVTOL extends DeliveryDrone implements Airborne, GroundBased {
    public HybridVTOL(String droneId) {
        super(droneId);
    }
    
    @Override
    public void deliverPackage() {
        System.out.println("HybridVTOL " + droneId + " delivering package using both air and ground");
    }
    
    @Override
    public void flyToDestination() {
        System.out.println("HybridVTOL " + droneId + " flying to destination");
    }
    
    @Override
    public void navigateSidewalks() {
        System.out.println("HybridVTOL " + droneId + " navigating sidewalks");
    }
    
    @Override
    public void requestAirTrafficClearance() {
        System.out.println("HybridVTOL " + droneId + " requesting air traffic clearance");
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        DeliveryDrone[] drones = {
            new Quadcopter("Q001"),
            new CityRover("R001"),
            new HybridVTOL("H001")
        };
        
        for (DeliveryDrone drone : drones) {
            drone.deliverPackage();
            
            if (drone instanceof Airborne) {
                ((Airborne) drone).flyToDestination();
                ((Airborne) drone).requestAirTrafficClearance();
            }
            
            if (drone instanceof GroundBased) {
                ((GroundBased) drone).navigateSidewalks();
            }
            
            System.out.println();
        }
    }
}