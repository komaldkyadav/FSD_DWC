abstract class SpaceVessel {
    short shipId;
    byte bayNo;
    boolean miningStatus;
    char shipClass;

    SpaceVessel(short shipId, byte bayNo, boolean miningStatus, char shipClass) {
        this.shipId = shipId;
        this.bayNo = bayNo;
        this.miningStatus = miningStatus;
        this.shipClass = shipClass;
    }

    abstract void displayInfo();
}

class MiningShip extends SpaceVessel {

    float[][] cargo;

    MiningShip(short shipId, byte bayNo, boolean miningStatus,
               char shipClass, float[][] cargo) {

        super(shipId, bayNo, miningStatus, shipClass);
        this.cargo = cargo;
    }

    float getTotalOre() {
        float total = 0;

        for (int i = 0; i < cargo.length; i++) {
            for (int j = 0; j < cargo[i].length; j++) {
                total += cargo[i][j];
            }
        }

        return total;
    }

    float getMaxOre() {
        float max = cargo[0][0];

        for (int i = 0; i < cargo.length; i++) {
            for (int j = 0; j < cargo[i].length; j++) {
                if (cargo[i][j] > max) {
                    max = cargo[i][j];
                }
            }
        }

        return max;
    }

    @Override
    void displayInfo() {
        System.out.println("Ship ID : " + shipId);
        System.out.println("Bay No : " + bayNo);
        System.out.println("Class : " + shipClass);
        System.out.println("Mining Status : " + miningStatus);
        System.out.println("Total Ore : " + getTotalOre() + " kg");
        System.out.println("Heaviest Container : " + getMaxOre() + " kg");
    }
}

public class Q1_MiningFleet {

    public static void main(String[] args) {

        float[][] cargo1 = {
                {1200.5f, 3400.0f},
                {500.0f, 800.75f},
                {4999.9f, 2100.0f}
        };

        float[][] cargo2 = {
                {300.0f, 450.5f, 220.0f},
                {900.0f, 1100.0f, 670.25f}
        };

        MiningShip s1 = new MiningShip((short) 1001, (byte) 3, true, 'A', cargo1);
        MiningShip s2 = new MiningShip((short) 1002, (byte) 2, false, 'B', cargo2);

        SpaceVessel[] fleet = {s1, s2};

        long totalFleetOre = 0;

        for (int i = 0; i < fleet.length; i++) {

            fleet[i].displayInfo();
            System.out.println();

            totalFleetOre += (long) ((MiningShip) fleet[i]).getTotalOre();
        }

        System.out.println("Total Fleet Ore Value = " + totalFleetOre + " credits");
    }
}