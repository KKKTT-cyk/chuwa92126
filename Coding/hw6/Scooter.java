public class Scooter implements Transport {
    @Override
    public void startTrip() {
        System.out.println("Scooter trip started.");
    }

    // $2 base fare + $1 per kilometer
    @Override
    public double getFare(int distance) {
        return 2 + 1 * distance;
    }
}
