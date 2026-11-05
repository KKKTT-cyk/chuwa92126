package org.tiff.homework5.factory;

import org.tiff.homework5.Transport;

public abstract class TransportFactory {
    public abstract Transport createTransport();
    public void bookTrip(int distance) {
       Transport ts = createTransport();
       ts.startTrip();
       double fare = ts.getFare(distance);
       System.out.println("Distance: " + distance + " km");
       System.out.println("Fare: $" + fare);
    }
}
