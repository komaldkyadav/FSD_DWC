// Passenger.java
import java.util.Objects;

public class Passenger {
    private String passportNumber;
    private String fullName;
    private String nationality;
    
    public Passenger(String passportNumber, String fullName, String nationality) {
        this.passportNumber = passportNumber;
        this.fullName = fullName;
        this.nationality = nationality;
    }
    
    public String getPassportNumber() {
        return passportNumber;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Passenger passenger = (Passenger) obj;
        return Objects.equals(passportNumber, passenger.passportNumber) &&
               Objects.equals(nationality, passenger.nationality);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(passportNumber, nationality);
    }
    
    @Override
    public String toString() {
        return "Passenger{" +
               "passportNumber='" + passportNumber + '\'' +
               ", fullName='" + fullName + '\'' +
               ", nationality='" + nationality + '\'' +
               '}';
    }
}

// ManifestManager.java
import java.util.*;

public class ManifestManager {
    private Set<Passenger> globalNoFlyList;
    private Map<String, List<Passenger>> flightRosters;
    private Map<Passenger, String> globalPassengerDirectory;
    
    public ManifestManager() {
        globalNoFlyList = new HashSet<>();
        flightRosters = new HashMap<>();
        globalPassengerDirectory = new HashMap<>();
    }
    
    public void addToNoFlyList(Passenger p) {
        globalNoFlyList.add(p);
    }
    
    public boolean processCheckIn(String flightNumber, Passenger p) {
        if (globalNoFlyList.contains(p)) {
            System.out.println("Passenger " + p.getFullName() + " is on No-Fly List. Check-in rejected.");
            return false;
        }
        
        flightRosters.computeIfAbsent(flightNumber, k -> new ArrayList<>());
        flightRosters.get(flightNumber).add(p);
        globalPassengerDirectory.put(p, flightNumber);
        
        System.out.println("Passenger " + p.getFullName() + " checked in to flight " + flightNumber);
        return true;
    }
    
    public String locatePassengerFlight(Passenger p) {
        return globalPassengerDirectory.get(p);
    }
    
    public Set<Passenger> getGlobalNoFlyList() {
        return globalNoFlyList;
    }
    
    public Map<String, List<Passenger>> getFlightRosters() {
        return flightRosters;
    }
    
    public Map<Passenger, String> getGlobalPassengerDirectory() {
        return globalPassengerDirectory;
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        ManifestManager manager = new ManifestManager();
        
        Passenger alice = new Passenger("P1001", "Alice Johnson", "USA");
        Passenger bob = new Passenger("P1002", "Bob Smith", "UK");
        Passenger carol = new Passenger("P1003", "Carol Davis", "Canada");
        Passenger dave = new Passenger("P1004", "Dave Wilson", "USA");
        Passenger eve = new Passenger("P1005", "Eve Brown", "Australia");
        
        manager.addToNoFlyList(eve);
        
        manager.processCheckIn("AA101", alice);
        manager.processCheckIn("AA102", bob);
        manager.processCheckIn("AA101", carol);
        boolean eveCheckin = manager.processCheckIn("AA103", eve);
        
        System.out.println("\n=== Flight Rosters ===");
        manager.getFlightRosters().forEach((flight, passengers) -> {
            System.out.println("Flight " + flight + ":");
            passengers.forEach(p -> System.out.println("  - " + p.getFullName() + " (" + p.getPassportNumber() + ")"));
        });
        
        System.out.println("\n=== Passenger Location Lookup ===");
        System.out.println("Alice is on flight: " + manager.locatePassengerFlight(alice));
        System.out.println("Bob is on flight: " + manager.locatePassengerFlight(bob));
        System.out.println("Carol is on flight: " + manager.locatePassengerFlight(carol));
        System.out.println("Dave is not checked in: " + manager.locatePassengerFlight(dave));
        System.out.println("Eve check-in status: " + (eveCheckin ? "Success" : "Rejected"));
        
        System.out.println("\n=== Testing equals() and hashCode() Contract ===");
        Passenger aliceCopy = new Passenger("P1001", "Alice Johnson (married name)", "USA");
        System.out.println("Looking up Alice by copy (same passport & nationality): " + 
                          manager.locatePassengerFlight(aliceCopy));
        System.out.println("alice.equals(aliceCopy): " + alice.equals(aliceCopy));
        System.out.println("alice.hashCode() == aliceCopy.hashCode(): " + 
                          (alice.hashCode() == aliceCopy.hashCode()));
        
        System.out.println("\n=== Memory Analysis ===");
        System.out.println("Set for No-Fly List: O(1) lookup");
        System.out.println("List for Flight Rosters: Preserves check-in order");
        System.out.println("Map for Global Directory: O(1) passenger lookup");
        System.out.println("equals() and hashCode() exclude fullName for correct identity");
    }
}