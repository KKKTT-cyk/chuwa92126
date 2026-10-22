package org.tiff.homework5.factory;

import org.tiff.homework5.Scooter;
import org.tiff.homework5.Transport;

public class ScooterFactory extends TransportFactory{

    @Override
    public Transport createTransport() {
        return new Scooter();
    }
}
