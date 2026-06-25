import java.util.concurrent.atomic.AtomicInteger;
public class Q5_DroneHive {
    static AtomicInteger returned = new AtomicInteger(0);
    static volatile boolean abort = false;
    static class Drone implements Runnable {
        int id;
        Drone(int id) {
            this.id = id;
        }
        @Override
        public void run() {
            if (abort) {
                System.out.println("Drone " + id + " aborted");
                return;
            }
            int count = returned.incrementAndGet();
            System.out.println("Drone " + id + " landed. Count = " + count);
        }
    }
    static class Radar implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(5);
                System.out.println("Storm Detected");
                abort = true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        int n = 20;
        Thread radar = new Thread(new Radar());
        radar.start();
        Thread[] drones = new Thread[n];
        for (int i = 0; i < n; i++) {
            drones[i] = new Thread(new Drone(i + 1));
            drones[i].start();
        }
        for (int i = 0; i < n; i++) {
            drones[i].join();
        }
        radar.join();
        System.out.println();
        System.out.println("Total Returned = " + returned.get());
        System.out.println("Abort Status = " + abort);
    }
}