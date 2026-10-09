package Coding.hw6;

interface Transport {
    void startTrip();

    double getFare(int distance);
}


class Car implements Transport {
    @Override
    public void startTrip() {
        System.out.println("Car trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 5 + 2 * distance;
    }
}

class Bike implements Transport {
    @Override
    public void startTrip() {
        System.out.println("Bike trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 1 + 0.5 * distance;
    }
}

class Scooter implements Transport {
    @Override
    public void startTrip() {
        System.out.println("Scooter trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 2 + 1 * distance;
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

class CarFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Car();
    }
}

class BikeFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Bike();
    }
}

class ScooterFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Scooter();
    }
}

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