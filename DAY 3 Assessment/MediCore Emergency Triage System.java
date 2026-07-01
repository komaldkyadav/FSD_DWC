// TriageLevel.java
public enum TriageLevel {
    CRITICAL,  // Highest priority
    URGENT,    // Medium priority
    STABLE     // Lowest priority
}

// Patient.java
public class Patient implements Comparable<Patient> {
    private String name;
    private TriageLevel severity;
    private long arrivalTime;
    
    public Patient(String name, TriageLevel severity, long arrivalTime) {
        this.name = name;
        this.severity = severity;
        this.arrivalTime = arrivalTime;
    }
    
    public String getName() {
        return name;
    }
    
    public TriageLevel getSeverity() {
        return severity;
    }
    
    public long getArrivalTime() {
        return arrivalTime;
    }
    
    @Override
    public int compareTo(Patient other) {
        // Compare by severity first (CRITICAL > URGENT > STABLE)
        int severityCompare = this.severity.compareTo(other.severity);
        if (severityCompare != 0) {
            return severityCompare;
        }
        // If same severity, compare by arrival time (earlier first)
        return Long.compare(this.arrivalTime, other.arrivalTime);
    }
    
    @Override
    public String toString() {
        return "Patient{" +
               "name='" + name + '\'' +
               ", severity=" + severity +
               ", arrivalTime=" + arrivalTime +
               '}';
    }
}

// TriageManager.java
import java.util.PriorityQueue;

public class TriageManager {
    private PriorityQueue<Patient> waitingRoom;
    
    public TriageManager() {
        waitingRoom = new PriorityQueue<>();
    }
    
    public void admitPatient(Patient p) {
        waitingRoom.offer(p);
        System.out.println("Admitted: " + p.getName() + " (" + p.getSeverity() + ")");
    }
    
    public Patient getNextPatient() {
        Patient next = waitingRoom.poll();
        if (next != null) {
            System.out.println("Next patient: " + next.getName() + " (" + next.getSeverity() + ")");
        }
        return next;
    }
    
    public boolean isEmpty() {
        return waitingRoom.isEmpty();
    }
    
    public int getWaitingCount() {
        return waitingRoom.size();
    }
}

// EmergencyRoom.java
public class EmergencyRoom {
    public static void main(String[] args) {
        TriageManager triage = new TriageManager();
        
        long currentTime = System.currentTimeMillis();
        
        Patient p1 = new Patient("John Doe", TriageLevel.URGENT, currentTime + 1000);
        Patient p2 = new Patient("Jane Smith", TriageLevel.CRITICAL, currentTime);
        Patient p3 = new Patient("Bob Johnson", TriageLevel.STABLE, currentTime + 2000);
        Patient p4 = new Patient("Alice Brown", TriageLevel.CRITICAL, currentTime + 500);
        Patient p5 = new Patient("Charlie Wilson", TriageLevel.URGENT, currentTime + 1500);
        
        System.out.println("=== Mass Casualty Event ===");
        triage.admitPatient(p1);
        triage.admitPatient(p2);
        triage.admitPatient(p3);
        triage.admitPatient(p4);
        triage.admitPatient(p5);
        
        System.out.println("\n=== Treatment Order (Priority Queue) ===");
        System.out.println("Patients waiting: " + triage.getWaitingCount());
        
        while (!triage.isEmpty()) {
            Patient next = triage.getNextPatient();
            if (next != null) {
                System.out.println("  Treating: " + next.getName() + 
                                 " (Severity: " + next.getSeverity() + 
                                 ", Arrived: " + (next.getArrivalTime() - currentTime) + "ms ago)");
            }
        }
        
        System.out.println("\n=== Time Complexity Analysis ===");
        System.out.println("Add patient: O(log n) - Binary Heap bubbling");
        System.out.println("Get next patient: O(log n) - Heapify after removal");
        System.out.println("Peek next patient: O(1) - Element at root");
        
        System.out.println("\n=== CompareTo Logic ===");
        System.out.println("Primary sort: Severity (CRITICAL > URGENT > STABLE)");
        System.out.println("Secondary sort: Arrival time (earlier first)");
        System.out.println("Both CRITICAL patients: Jane arrived first, so treated before Alice");
    }
}