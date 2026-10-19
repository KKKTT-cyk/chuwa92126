public class BikeFactory extends TransportFactory {

    @Override
    public Transport createTransport() {
        return new Bike();
    }
}
