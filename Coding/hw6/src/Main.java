public class Main {
    public static void main(String[] args) {
        TransportFactory car = new CarFactory();
        TransportFactory bike = new BikeFactory();
        TransportFactory scooter = new ScooterFactory();

        car.bookTrip(10);
        bike.bookTrip(8);
        scooter.bookTrip(6);


    }
}