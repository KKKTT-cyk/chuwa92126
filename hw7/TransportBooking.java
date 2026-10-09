// Single-file version: only one public class per file, so the others are package-private

// Product interface: every transport type must implement these two methods
interface Transport {
    void startTrip();
    double getFare(int distance);
}

// Concrete product: $5 base fare + $2 per km
class Car implements Transport {
    public void startTrip() { System.out.println("Car trip started."); }
    public double getFare(int distance) { return 5.0 + 2.0 * distance; }
}

// Concrete product: $1 base fare + $0.5 per km
class Bike implements Transport {
    public void startTrip() { System.out.println("Bike trip started."); }
    public double getFare(int distance) { return 1.0 + 0.5 * distance; }
}

// Concrete product: $2 base fare + $1 per km
class Scooter implements Transport {
    public void startTrip() { System.out.println("Scooter trip started."); }
    public double getFare(int distance) { return 2.0 + 1.0 * distance; }
}

// Creator: defines the booking flow, but lets subclasses decide which transport to create
abstract class TransportFactory {

    // Factory method: each subclass returns its own transport type
    public abstract Transport createTransport();

    // Shared booking flow, written once for all transport types
    public void bookTrip(int distance) {
        Transport transport = createTransport();   // get a transport from the subclass
        transport.startTrip();                     // start the trip
        double fare = transport.getFare(distance); // calculate the fare
        System.out.println("Distance: " + distance + " km");
        System.out.println("Fare: $" + fare);
    }
}

// Concrete creators: each one creates one transport type
class CarFactory extends TransportFactory {
    public Transport createTransport() { return new Car(); }
}

class BikeFactory extends TransportFactory {
    public Transport createTransport() { return new Bike(); }
}

class ScooterFactory extends TransportFactory {
    public Transport createTransport() { return new Scooter(); }
}

// Entry point: only uses factories, never creates Car, Bike, or Scooter directly
public class TransportBooking {
    public static void main(String[] args) {
        TransportFactory carFactory = new CarFactory();
        TransportFactory bikeFactory = new BikeFactory();
        TransportFactory scooterFactory = new ScooterFactory();

        carFactory.bookTrip(10);
        bikeFactory.bookTrip(8);
        scooterFactory.bookTrip(6);
    }
}
