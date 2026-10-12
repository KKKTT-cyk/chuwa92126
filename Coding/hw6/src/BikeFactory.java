public class BikeFactory extends TransportFactory{
    @Override
    Transport createTransport() {
        return new Bike();
    }
}
