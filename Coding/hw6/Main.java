public class Main {
    public static void main(String[] args) {
        TransportFactory carFactory = new CarFactory();
        TransportFactory bikeFactory = new BikeFactory();
        TransportFactory scooterFactory = new ScooterFactory();

        carFactory.bookTrip(10);
        bikeFactory.bookTrip(8);
        scooterFactory.bookTrip(6);
    }
}
