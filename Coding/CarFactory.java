public class CarFactory extends TransportFactory{
    @Override
    public Transport createTransport() {
        return new Car();
    }
}
