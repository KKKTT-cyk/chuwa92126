abstract public class TransportFactory {
    abstract Transport createTransport();

    void bookTrip(int distance){
        Transport transport = createTransport();
        transport.startTrip();
        double fare = transport.getFare(distance);
        System.out.println("Distance: "+distance+" km\nFare: $"+fare);
        System.out.println();
    }


}
