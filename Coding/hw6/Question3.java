//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
interface Transport {
  void startTrip();

  double getFare(int distance);
}

class Car implements Transport {
  private static final double BASE_FARE = 5.0;
  private static final double RATE_PER_KM = 2.0;

  @Override
  public void startTrip() {
    System.out.println("Car trip started.");
  }

  @Override
  public double getFare(int distance) {
    return BASE_FARE + RATE_PER_KM * distance;
  }
}

class Bike implements Transport {
  private static final double BASE_FARE = 1.0;
  private static final double RATE_PER_KM = 0.5;

  @Override
  public void startTrip() {
    System.out.println("Bike trip started.");
  }

  @Override
  public double getFare(int distance) {
    return BASE_FARE + RATE_PER_KM * distance;
  }
}

class Scooter implements Transport {
  private static final double BASE_FARE = 2.0;
  private static final double RATE_PER_KM = 1.0;

  @Override
  public void startTrip() {
    System.out.println("Scooter trip started.");
  }

  @Override
  public double getFare(int distance) {
    return BASE_FARE + RATE_PER_KM * distance;
  }
}

// Adding a new class Bus
class Bus implements Transport {
  private static final double BASE_FARE = 2.0;
  private static final double RATE_PER_KM = 0.0;

  @Override
  public void startTrip() {
    System.out.println("Bus trip started.");
  }

  @Override
  public double getFare(int distance) {
    return BASE_FARE + RATE_PER_KM * distance;
  }
}

abstract class TransportFactory {
  protected abstract Transport createTransport();

  public final void bookTrip(int distance) {
    Transport transport = createTransport();
    transport.startTrip();
    double fare = transport.getFare(distance);

    System.out.println("Distance: " + distance + " km");
    System.out.println("Fare: $" + fare);
    System.out.println();
  }
}

class CarFactory extends TransportFactory {

  @Override
  protected Transport createTransport() {
    return new Car();
  }
}

class BikeFactory extends TransportFactory {

  @Override
  protected Transport createTransport() {
    return new Bike();
  }
}

class ScooterFactory extends TransportFactory {

  @Override
  protected Transport createTransport() {
    return new Scooter();
  }
}

// Add BusFactory
class BusFactory extends TransportFactory {

  @Override
  protected Transport createTransport() {
    return new Bus();
  }
}

public class Question3 {
  public static void main(String[] args) {
    TransportFactory carFactory = new CarFactory();
    TransportFactory bikeFactory = new BikeFactory();
    TransportFactory scooterFactory = new ScooterFactory();

    carFactory.bookTrip(10);
    bikeFactory.bookTrip(8);
    scooterFactory.bookTrip(6);

    // Adding a new class Bus
    TransportFactory busFactory = new BusFactory();
    busFactory.bookTrip(10);
  }
}
