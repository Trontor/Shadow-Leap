package base;

/**
 * Interface that describes an object that can move with a Driver
 */
public interface PassengerSupport {
  void checkForDrivers();

  void detachDriver();

  void attachDriver(Driver driver);
}
