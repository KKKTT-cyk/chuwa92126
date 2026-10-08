public class CarFactory extends TransportFactory{
    @Override
    Transport createTransport() {
        return new Car();
    }
}
