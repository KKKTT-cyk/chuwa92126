public class ScooterFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Scooter();
    }
}
