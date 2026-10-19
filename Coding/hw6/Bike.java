public class Bike implements Transport {
    @Override
    public void startTrip() {
        System.out.println("Bike trip started.");
    }

    // $1 base fare + $0.5 per kilometer
    @Override
    public double getFare(int distance) {
        return 1 + 0.5 * distance;
    }
}
