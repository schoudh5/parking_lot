package services;

import helpers.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class MemoryParkingService implements ParkingService {
    private final Map<String, List<ParkedCarAttributes>> colorByParkedCarAttributes;
    private final Map<String, Integer> registrationBySlotNumber;
    private final Map<Integer, Car> slotNumberByRegistration;
    private final PriorityQueue<Integer> availableParkingSlots;
    private static MemoryParkingService instance;
    private final int maxSlots;

    private MemoryParkingService(int maxSlots) {
        colorByParkedCarAttributes = new HashMap<>();
        registrationBySlotNumber = new HashMap<>();
        availableParkingSlots = new PriorityQueue<>();
        slotNumberByRegistration = new HashMap<>();
        this.maxSlots = maxSlots;
        populateSlots();
    }

    public static MemoryParkingService getInstance(int noOfSlots) {
        if (instance == null) {
            synchronized (MemoryParkingService.class) {
                if (instance == null) {
                    instance = new MemoryParkingService(noOfSlots);
                }
            }
        }
        return instance;
    }

    public static void destroy() {
        instance =null;
    }

    public String getRegistrationsByColor(String color) {
        List<ParkedCarAttributes> parkedCarAttributes = colorByParkedCarAttributes.get(color);
        if (Objects.nonNull(parkedCarAttributes)) {
            return String
                    .join(", ", parkedCarAttributes.stream()
                            .map(p -> p.getRegistrationNumber()).collect(Collectors.toList()));
        }
        return Message.NOT_FOUND;
    }

    public String getSlotsByColor(String color) {
        List<ParkedCarAttributes> parkedCarAttributes = colorByParkedCarAttributes.get(color);
        if (Objects.nonNull(parkedCarAttributes)) {
            return String
                    .join(", ", parkedCarAttributes.stream()
                            .map(p -> String.valueOf(p.getSlot())).collect(Collectors.toList()));
        }
        return Message.NOT_FOUND;
    }

    public String getSlot(String registration) {
        Integer slot = registrationBySlotNumber.get(registration);
        if (Objects.nonNull(slot)) {
            return String.valueOf(slot);
        }
        return Message.NOT_FOUND;
    }

    public List<String> getStatus() {
        List<String> parkedCarStatus = new LinkedList<>();
        parkedCarStatus.add(Message.PACKED_CAR_STATUS_HEADER);
        for (int num = 1; num <= maxSlots; num++) {
            if (slotNumberByRegistration.containsKey(num)) {
                Car car = slotNumberByRegistration.get(num);
                parkedCarStatus.add(String.format(Message.PARKED_CAR_STATUS, num, car.getRegistration(), car.getColor()));
            }
        }
        return parkedCarStatus;
    }

    @Override
    public String park(Car car) {
        if (availableParkingSlots.isEmpty()) {
            return Message.PARKING_FULL;
        }
        int closestSlot = availableParkingSlots.poll();
        List<ParkedCarAttributes> parkedCarAttributes = colorByParkedCarAttributes.get(car.getColor());
        if (Objects.isNull(parkedCarAttributes)) {
            parkedCarAttributes = new ArrayList<>();
        }
        parkedCarAttributes.add(ParkedCarAttributes
                .newBuilder()
                .withRegistrationNumber(car.getRegistration())
                .withSlot(closestSlot)
                .withColor(car.getColor())
                .build());
        colorByParkedCarAttributes.put(car.getColor(), parkedCarAttributes);
        registrationBySlotNumber.put(car.getRegistration(), closestSlot);
        slotNumberByRegistration.put(closestSlot, car);
        return String.format(Message.SLOT_ALLOCATION, closestSlot);
    }

    @Override
    public String leave(int slot) {
        Car car = slotNumberByRegistration.get(slot);
        if (car == null) {
            return String.format(Message.NOT_FOUND);
        }
        slotNumberByRegistration.remove(slot);
        registrationBySlotNumber.remove(car.getRegistration());
        colorByParkedCarAttributes.get(car.getColor())
                .remove(ParkedCarAttributes
                        .newBuilder()
                        .withRegistrationNumber(car.getRegistration())
                        .withSlot(slot)
                        .withColor(car.getColor())
                        .build());
        if (colorByParkedCarAttributes.get(car.getColor()).isEmpty()) {
            colorByParkedCarAttributes.remove(car.getColor());
        }
        availableParkingSlots.offer(slot);
        return String.format(Message.FREE_SLOT, slot);
    }

    private void populateSlots() {
        for (int num = 1; num <= maxSlots; num++) {
            availableParkingSlots.offer(num);
        }
    }
}
