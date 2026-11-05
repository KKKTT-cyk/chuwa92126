package org.tiff.homework5;

import org.tiff.homework5.factory.BikeFactory;
import org.tiff.homework5.factory.CarFactory;
import org.tiff.homework5.factory.ScooterFactory;
import org.tiff.homework5.factory.TransportFactory;

public class Homework5 {
    public static void main(String[] args) {
        TransportFactory carFactory = new CarFactory();
        carFactory.bookTrip(10);

        TransportFactory bikeFactory = new BikeFactory();
        bikeFactory.bookTrip(8);

        TransportFactory scooterFactory = new ScooterFactory();
        scooterFactory.bookTrip(6);
    }

}
