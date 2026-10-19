public abstract class TransportFactory {

    public abstract Transport createTransport();

    public void bookTrip(int distance) {
        Transport transport = createTransport();

        transport.startTrip();

        double fare = transport.getFare(distance);

        System.out.println("Distance: " + distance + " km");
        System.out.println("Fare: $" + fare);
    }
}
