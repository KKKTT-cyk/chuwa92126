package org.tiff.homework5;

public class Scooter implements Transport{
    @Override
    public void startTrip() {
        System.out.println("Scooter trip started.");
    }

    @Override
    public double getFare(int distance) {
        return 2 + distance;
    }
}
