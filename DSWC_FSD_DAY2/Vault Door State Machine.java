// IllegalStateTransitionException.java
public class IllegalStateTransitionException extends RuntimeException {
    public IllegalStateTransitionException(String message) {
        super(message);
    }
}

// VaultDoor.java
public class VaultDoor {
    private DoorState state;
    
    public VaultDoor() {
        this.state = DoorState.OPEN;
    }
    
    public void closeDoor() {
        if (state == DoorState.OPEN) {
            state = DoorState.CLOSED;
            System.out.println("Door closed");
        } else if (state == DoorState.LOCKED) {
            System.out.println("Door is already locked, unlock first");
        } else {
            System.out.println("Door is already closed");
        }
    }
    
    public void lockDoor() {
        if (state == DoorState.CLOSED) {
            state = DoorState.LOCKED;
            System.out.println("Door locked");
        } else if (state == DoorState.OPEN) {
            throw new IllegalStateTransitionException("Cannot lock an open door. Please close the door first.");
        } else {
            System.out.println("Door is already locked");
        }
    }
    
    public void unlockDoor() {
        if (state == DoorState.LOCKED) {
            state = DoorState.CLOSED;
            System.out.println("Door unlocked");
        } else {
            System.out.println("Door is not locked");
        }
    }
    
    public DoorState getState() {
        return state;
    }
}

// DoorState.java
public enum DoorState {
    OPEN,
    CLOSED,
    LOCKED
}

// VaultDoorTest.java
public class VaultDoorTest {
    public static void main(String[] args) {
        VaultDoor vault = new VaultDoor();
        
        System.out.println("Initial state: " + vault.getState());
        
        vault.closeDoor();
        System.out.println("State: " + vault.getState());
        
        vault.lockDoor();
        System.out.println("State: " + vault.getState());
        
        vault.unlockDoor();
        System.out.println("State: " + vault.getState());
        
        System.out.println("\nTesting illegal transition:");
        vault.unlockDoor();
        
        vault.closeDoor();
        System.out.println("State: " + vault.getState());
        
        try {
            vault.lockDoor();
        } catch (IllegalStateTransitionException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}