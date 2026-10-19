public class ScooterFactory extends TransportFactory{
    @Override
    Transport createTransport() {
        return new Scooter();
    }
}
