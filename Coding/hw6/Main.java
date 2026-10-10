public class Main {
    public static void main(String[] args) {
        TransportFactory carFactory = new CarFactory();
        TransportFactory bikeFactory = new BikeFactory();
        TransportFactory scooterFactory = new ScooterFactory();

        carFactory.bookTrip(10);
        System.out.println();
        bikeFactory.bookTrip(8);
        System.out.println();
        scooterFactory.bookTrip(6);
    }
}
