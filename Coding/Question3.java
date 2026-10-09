public class Question3 {
    public static void main(String[] args) {
        TransportFactory carFactory = new CarFactory();
        TransportFactory bikeFactory = new BikeFactory();
        TransportFactory scooterFactory = new ScooterFactory();

        carFactory.bookTrip(10);
        bikeFactory.bookTrip(8);
        scooterFactory.bookTrip(6);
    }
}

/*Output:
Car trip started.
Distance: 10 km
Fare: $25.0
Bike trip started.
Distance: 8 km
Fare: $5.0
Scooter trip started.
Distance: 6 km
Fare: $8.0
*/