public class Scooter implements Transport {
    @Override
    public void startTrip() {
        System.out.println("Scooter trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 2.0 + 1.0 * distance;
    }
}
