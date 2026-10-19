public abstract class TransportFactory {
    // Factory Method - subclasses decide WHAT to create
    protected abstract Transport createTransport();

    // Template code that USES the created object
    public void bookTrip(int distance) {
        Transport transport = createTransport();
        transport.startTrip();
        double fare = transport.getFare(distance);
        System.out.println("Distance: " + distance + " km");
        System.out.println("Fare: $" + fare);
    }
}
