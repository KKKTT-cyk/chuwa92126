
interface Transport {
    void startTrip();
    double getFare(int distance);
}

// Concrete Product: Car
class Car implements Transport {
    @Override
    public void startTrip() {
        System.out.println("Car trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 5.0 + 2.0 * distance;
    }
}

// Concrete Product: Bike
class Bike implements Transport {
    @Override
    public void startTrip() {
        System.out.println("Bike trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 1.0 + 0.5 * distance;
    }
}

// Concrete Product: Scooter
class Scooter implements Transport {
    @Override
    public void startTrip() {
        System.out.println("Scooter trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 2.0 + 1.0 * distance;
    }
}

// Abstract Creator
abstract class TransportFactory {
    public abstract Transport createTransport();

    public void bookTrip(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException(
                    "Distance cannot be negative."
            );
        }

        Transport transport = createTransport();

        transport.startTrip();

        double fare = transport.getFare(distance);

        System.out.println(
                "Distance: " + distance + " km"
        );
        System.out.println("Fare: $" + fare);
        System.out.println();
    }
}

// Concrete Creator: CarFactory
class CarFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Car();
    }
}

// Concrete Creator: BikeFactory
class BikeFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Bike();
    }
}

// Concrete Creator: ScooterFactory
class ScooterFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Scooter();
    }
}

// Main Application
public class TransportationBookingSystem {
    public static void main(String[] args) {

        TransportFactory carFactory =
                new CarFactory();

        TransportFactory bikeFactory =
                new BikeFactory();

        TransportFactory scooterFactory =
                new ScooterFactory();

        carFactory.bookTrip(10);

        bikeFactory.bookTrip(8);

        scooterFactory.bookTrip(6);
    }
}
