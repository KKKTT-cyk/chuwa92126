package org.tiff.homework5;

public class Car implements Transport{
    @Override
    public void startTrip() {
        System.out.println("Car trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 5 + 2 * distance;
    }
}
