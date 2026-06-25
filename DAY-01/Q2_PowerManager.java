public class Q2_PowerManager {

    byte sectorState = 0;

    void turnOn(int sector) {
        sectorState = (byte) (sectorState | (1 << sector));
    }

    void turnOff(int sector) {
        sectorState = (byte) (sectorState & ~(1 << sector));
    }

    boolean checkSector(int sector) {
        return (sectorState & (1 << sector)) != 0;
    }

    void displaySectors() {
        for (int i = 7; i >= 0; i--) {
            if (checkSector(i)) {
                System.out.print("S" + i + ":ON ");
            } else {
                System.out.print("S" + i + ":OFF ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {

        Q2_PowerManager p = new Q2_PowerManager();

        System.out.println("Initial State");
        p.displaySectors();

        p.turnOn(0);
        p.turnOn(3);
        p.turnOn(7);

        System.out.println("After Turning ON Sectors 0, 3 and 7");
        p.displaySectors();

        p.turnOff(3);

        System.out.println("After Turning OFF Sector 3");
        p.displaySectors();

        System.out.println("Sector 0 : " + p.checkSector(0));
        System.out.println("Sector 3 : " + p.checkSector(3));
        System.out.println("Sector 7 : " + p.checkSector(7));
    }
}