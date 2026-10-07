package org.tiff.homework5.factory;

import org.tiff.homework5.Bike;
import org.tiff.homework5.Transport;

public class BikeFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Bike();
    }
}
