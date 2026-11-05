package org.tiff.homework5.factory;

import org.tiff.homework5.Car;
import org.tiff.homework5.Transport;

public class CarFactory extends TransportFactory{
    @Override
    public Transport createTransport() {
        return new Car();
    }
}
