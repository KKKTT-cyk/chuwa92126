public class CarFactory extends TransportFactory {
    @Override
    protected Transport createTransport() {
        return new Car();
    }
}
