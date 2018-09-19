package base;

/** Interface that describes an object that can move with a Driver */
public interface PassengerSupport {
  /** Handles logic to detect if the passenger is interacting with a Driver */
  void checkForDrivers();
  /** Handles logic to remove the passenger from the Driver */
  void detachDriver();
  /** Handles logic to add the passenger to the Driver */
  void attachDriver(Driver driver);
}
