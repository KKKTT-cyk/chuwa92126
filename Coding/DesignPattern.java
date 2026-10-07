package Coding;

public class DesignPattern {

    public static void main(String[] args) {

        TransportFactory carFactory = new CarFactory();
        TransportFactory bikeFactory = new BikeFactory();
        TransportFactory scooterFactory = new ScooterFactory();

        carFactory.bookTrip(10);
        bikeFactory.bookTrip(8);
        scooterFactory.bookTrip(6);
    }
}

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
        return 5 + distance * 2;    }

}

class Bike implements Transport {

    @Override
    public void startTrip() {

    System.out.println("Bike trip started.");    }

    @Override
    public double getFare(int distance) {
        return 1 + distance * 0.5;
    }

}
class Scooter implements Transport {

    @Override
    public void startTrip() {
        System.out.println("Scooter trip started.");
    }

    @Override
    public double getFare(int distance) {
      return 2 + distance;
    
}
}

abstract class TransportFactory {
    public abstract Transport createTransport();
    
    public void bookTrip(int distance) {
        Transport obj = createTransport();
        obj.startTrip();
        double price = obj.getFare(distance);
        System.out.println("Distance: " + distance + " km");
        System.out.println("Fare: $" + price);


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
