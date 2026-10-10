public class Car implements Transport {
    @Override
    public void startTrip() {
        System.out.println("Car trip started.");
    }

    // $5 base fare + $2 per kilometer
    @Override
    public double getFare(int distance) {
        return 5 + 2 * distance;
    }
}
