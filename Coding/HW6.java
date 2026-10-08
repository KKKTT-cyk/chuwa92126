public class HW6 {
    static interface Transport {
        void startTrip();
        double getFare(int distance);
    }

    static class Car implements Transport {
        @Override
        public void startTrip() {
            System.out.println("Car trip started.");
        }

        @Override
        public double getFare(int distance) {
            return distance * 2 + 5;
        }
    }

    static class Bike implements Transport {
        @Override
        public void startTrip() {
            System.out.println("Bike trip started.");
        }

        @Override
        public double getFare(int distance) {
            return distance * 0.5 + 1;
        }
    }

    static class Scooter implements Transport {
        @Override
        public void startTrip() {
            System.out.println("Scooter trip started.");
        }

        @Override
        public double getFare(int distance) {
            return distance + 2;
        }
    }

    static abstract class TransportFactory {
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

            System.out.println("Distance: " + distance + " km");
            System.out.println("Fare: $" + fare);

        }
    }

    static class CarFactory extends TransportFactory {
        @Override
        public Transport createTransport() {
            return new Car();
        }
    }

    static class BikeFactory extends TransportFactory {
        @Override
        public Transport createTransport() {
            return new Bike();
        }
    }

    static class ScooterFactory extends TransportFactory {
        @Override
        public Transport createTransport() {
            return new Scooter();
        }
    }

    public static void main(String[] args) {

        TransportFactory carFactory = new CarFactory();
        TransportFactory bikeFactory = new BikeFactory();
        TransportFactory scooterFactory = new ScooterFactory();

        carFactory.bookTrip(1);
        bikeFactory.bookTrip(5);
        scooterFactory.bookTrip(10);
    }
}