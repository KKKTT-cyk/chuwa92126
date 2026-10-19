public class ScooterFactory extends TransportFactory {
    @Override
    protected Transport createTransport() {
        return new Scooter();
    }
}
