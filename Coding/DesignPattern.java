package com.lant.hw5;

interface Transport{
    public void startTrip();
    public double getFare(int distance);
}
class Car implements Transport{
    @Override
    public void startTrip() {
        System.out.println("Car trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 5 + distance*2;
    }
}
class Bike implements Transport{

    @Override
    public void startTrip() {
        System.out.println("Bike trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 1 + distance * 0.5;
    }
}
class Scooter implements Transport{
    @Override
    public void startTrip() {
        System.out.println("Scooter trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 2 + distance * 1;
    }
}
abstract class TransportFactory{
    abstract Transport createTransport();
    void bookTrip(int distance){
        Transport transport = createTransport();
        transport.startTrip();
        System.out.println("Distance: " + distance + " km");
        System.out.println("Fare: $" + transport.getFare(distance));
    }
}

class CarFactory extends TransportFactory{
    @Override
    Transport createTransport() {
        return new Car();
    }
}
class BikeFactory extends TransportFactory{
    @Override
    Transport createTransport() {
        return new Bike();
    }
}
class ScooterFactory extends TransportFactory{
    @Override
    Transport createTransport() {
        return new Scooter();
    }
}
public class DesignPattern {
    public static void main(String[] args) {
        CarFactory carFactory = new CarFactory();
        BikeFactory bikeFactory = new BikeFactory();
        ScooterFactory scooterFactory = new ScooterFactory();
        carFactory.bookTrip(10);
        System.out.println();
        bikeFactory.bookTrip(8);
        System.out.println();
        scooterFactory.bookTrip(6);


    }

}
