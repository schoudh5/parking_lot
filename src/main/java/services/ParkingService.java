package services;

public interface ParkingService {
    String park(Car car);

    String leave(int slot);
}
