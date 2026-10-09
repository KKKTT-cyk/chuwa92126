
interface Transport {
    void startTrip();
    double getFare(int distance);
}

// Car
class Car implements Transport {
    private static final double BASE_FARE = 5.0;
    private static final double RATE_PER_KM = 2.0;

    @Override
    public void startTrip() {
        System.out.println("Car trip starts.");
    }

    @Override
    public double getFare(int distance) {
        return BASE_FARE + RATE_PER_KM * distance;
    }
}

// Bike
class Bike implements Transport {
    private static final double BASE_FARE = 1.0;
    private static final double RATE_PER_KM = 0.5;

    @Override
    public void startTrip() {
        System.out.println("Bike trip starts.");
    }

    @Override
    public double getFare(int distance) {
        return BASE_FARE + RATE_PER_KM * distance;
    }
}

// Scooter
class Scooter implements Transport {
    private static final double BASE_FARE = 2.0;
    private static final double RATE_PER_KM = 1.0;

    @Override
    public void startTrip() {
        System.out.println("Scooter trip starts.");
    }

    @Override
    public double getFare(int distance) {
        return BASE_FARE + RATE_PER_KM * distance;
    }
}

abstract class TransportFactory {
    public abstract Transport createTransport();

    public void bookTrip(int distance) {
        Transport transport = createTransport();
        transport.startTrip();

        double fare = transport.getFare(distance);

        System.out.println("Distance: " + distance + " km");
        System.out.println("Fare: $" + fare);
        System.out.println();
    }
}

// Car Factory
class CarFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Car();
    }
}

// Bike Factory
class BikeFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Bike();
    }
}

// Scooter Factory
class ScooterFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Scooter();
    }
}

public class Transportation {
    public static void main(String[] args) {
        TransportFactory carFactory = new CarFactory();
        TransportFactory bikeFactory = new BikeFactory();
        TransportFactory scooterFactory = new ScooterFactory();

        carFactory.bookTrip(10);
        bikeFactory.bookTrip(8);
        scooterFactory.bookTrip(6);
    }
}
