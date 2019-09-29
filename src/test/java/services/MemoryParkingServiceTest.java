package services;

import helpers.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MemoryParkingServiceTest {
    private static final int DEFAULT_NO_SLOTS = 5;
    private MemoryParkingService memoryParkingService;

    @Before
    public void setUp() {
        MemoryParkingService.destroy();
        memoryParkingService = MemoryParkingService.getInstance(DEFAULT_NO_SLOTS);
        vacateSlots();
    }

    @After
    public void tearDown() {
        vacateSlots();
    }

    @Test
    public void getRegistrationsByColor_givenColor_thenReturnRegistrations() {
        givenParkedCars();
        String actualWhiteRegistrations = this.memoryParkingService.getRegistrationsByColor("WHITE");
        String expectedWhiteRegistrations = "RJ-01-BC-2345, RJ-01-ZA-4321";
        assertThat(actualWhiteRegistrations, is(expectedWhiteRegistrations));

        String actualBlueRegistrations = this.memoryParkingService.getRegistrationsByColor("BLUE");
        String expectedBlueRegistrations = "RJ-01-VX-1345, RJ-01-ZA-4567";
        assertThat(actualBlueRegistrations, is(expectedBlueRegistrations));
    }

    @Test
    public void getRegistrationsByColor_givenColorAndCarCountChanges_thenReturnCorrectRegistrations() {
        givenParkedCars();
        String actualSlotFreeMessage = this.memoryParkingService.leave(1);
        assertThat(actualSlotFreeMessage, is(String.format(Message.FREE_SLOT, 1)));

        String actualWhiteRegistrations = this.memoryParkingService.getRegistrationsByColor("WHITE");
        String expectedWhiteRegistrations = "RJ-01-ZA-4321";
        assertThat(actualWhiteRegistrations, is(expectedWhiteRegistrations));

        String actualBlueRegistrations = this.memoryParkingService.getRegistrationsByColor("BLUE");
        String expectedBlueRegistrations = "RJ-01-VX-1345, RJ-01-ZA-4567";
        assertThat(actualBlueRegistrations, is(expectedBlueRegistrations));
    }

    @Test
    public void getRegistrationsByColor_givenNoCarExist_thenReturnNotFound() {
        String actualMessage = this.memoryParkingService.getRegistrationsByColor("WHITE");
        assertThat(actualMessage, is(Message.NOT_FOUND));
    }

    @Test
    public void getRegistrationsByColor_givenUnknownColor_thenReturnNotFound() {
        givenParkedCars();
        String actualMessage = this.memoryParkingService.getRegistrationsByColor("RED");
        assertThat(actualMessage, is(Message.NOT_FOUND));
    }

    @Test
    public void getSlotsByColor_givenColor_thenReturnSlots() {
        givenParkedCars();
        String actualMessage = this.memoryParkingService.getSlotsByColor("WHITE");
        String expectedMessage = "1, 4";
        assertThat(actualMessage, is(expectedMessage));
        actualMessage = this.memoryParkingService.getSlotsByColor("BLUE");
        expectedMessage = "2, 3";
        assertThat(actualMessage, is(expectedMessage));
    }

    @Test
    public void getSlotsByColor_givenColorAndCarChanges_thenReturnSlots() {
        givenParkedCars();

        String actualSlotFreeMessage = this.memoryParkingService.leave(4);
        assertThat(actualSlotFreeMessage, is(String.format(Message.FREE_SLOT, 4)));

        String actualSlotsWithWhiteCars = this.memoryParkingService.getSlotsByColor("WHITE");
        String expectedSlotsWithWhiteCars = "1";
        assertThat(actualSlotsWithWhiteCars, is(expectedSlotsWithWhiteCars));
    }

    @Test
    public void getSlotsByColor_givenNoCarExist_thenReturnSlots() {
        String actualMessage = this.memoryParkingService.getSlotsByColor("RED");
        assertThat(actualMessage, is(Message.NOT_FOUND));
    }

    @Test
    public void getSlotsByColor_givenUnknownColor_thenReturnNotFound() {
        givenParkedCars();
        String actualMessage = this.memoryParkingService.getSlotsByColor("RED");
        assertThat(actualMessage, is(Message.NOT_FOUND));
    }

    @Test
    public void getSlot_givenRegistration_thenReturnSlotNumber() {
        givenParkedCars();
        String actualSlot = this.memoryParkingService.getSlot("RJ-01-VX-1345");
        assertThat(actualSlot, is("2"));
    }

    @Test
    public void getSlot_givenUnknownRegistration_thenReturnNotFound() {
        givenParkedCars();
        String actualSlot = this.memoryParkingService.getSlot("RJ-02-VX-1345");
        assertThat(actualSlot, is(Message.NOT_FOUND));
    }

    @Test
    public void getSlot_givenNoCarExist_thenReturnNotFound() {
        String actualSlot = this.memoryParkingService.getSlot("RJ-02-VX-1345");
        assertThat(actualSlot, is(Message.NOT_FOUND));
    }

    @Test
    public void park_givenParkedCarsAndNewCarPark_thenParkAtNextFreeSlot() {
        givenParkedCars();
        Car car5 = Car.newBuilder().withRegistration("RJ-03-ZA-1221").withColor("RED").build();
        String actualParkingMessage = this.memoryParkingService.park(car5);
        assertThat(actualParkingMessage, is(String.format(Message.SLOT_ALLOCATION, 5)));
    }

    @Test
    public void park_givenParkedCarsAndSomeCarLeftAndNewCarPark_thenParkAtNextClosestSlot() {
        givenParkedCars();
        Car car5 = Car.newBuilder().withRegistration("RJ-03-ZA-1221").withColor("RED").build();

        this.memoryParkingService.leave(2);

        String actualSlotAllocationMessage = this.memoryParkingService.park(car5);
        assertThat(actualSlotAllocationMessage, is(String.format(Message.SLOT_ALLOCATION, 2)));
    }

    @Test
    public void park_givenParkedCarsAndNewCarNoSlotsLeft_thenReturnParkingFull() {
        givenParkedCars();

        Car lastCar = Car.newBuilder().withRegistration("RJ-03-ZA-1221").withColor("RED").build();
        this.memoryParkingService.park(lastCar);
        Car newCar = Car.newBuilder().withRegistration("RJ-04-QA-1331").withColor("RED").build();
        String actualParkingMessage = this.memoryParkingService.park(newCar);

        assertThat(actualParkingMessage, is(String.format(Message.PARKING_FULL)));
    }

    @Test
    public void leave_givenCarsExistAndLeave_thenReturnFreeSlotMessageAnd() {
        givenParkedCars();
        String actualSlotFreeMessage = this.memoryParkingService.leave(3);
        assertThat(actualSlotFreeMessage, is(String.format(Message.FREE_SLOT, 3)));
    }

    @Test
    public void leave_givenNoCarExistAndLeave_thenReturnNotFoundMessage() {
        String actualSlotFreeMessage = this.memoryParkingService.leave(3);
        assertThat(actualSlotFreeMessage, is(Message.NOT_FOUND));
    }

    @Test
    public void leave_givenCarsExistAndLeaveAndCalledLeaveAgain_thenReturnNotFoundMessage() {
        givenParkedCars();
        String actualSlotFreeMessage = this.memoryParkingService.leave(3);
        assertThat(actualSlotFreeMessage, is(String.format(Message.FREE_SLOT, 3)));

        actualSlotFreeMessage = this.memoryParkingService.leave(3);
        assertThat(actualSlotFreeMessage, is(Message.NOT_FOUND));
    }

    @Test
    public void getStatus_givenCarsExist_thenReturnTheirStatus() {
        givenParkedCars();
        List<String> actualCarsStatus = this.memoryParkingService.getStatus();
        assertThat(actualCarsStatus, is(expectedCarsStatus()));
    }

    @Test
    public void getStatus_givenCars_thenReturnTheirStatus() {
        givenParkedCars();
        List<String> actualCarsStatus = this.memoryParkingService.getStatus();
        assertThat(actualCarsStatus, is(expectedCarsStatus()));
    }

    @Test
    public void getStatus_givenCarsDoesNotExist_thenReturnTheirStatus() {
        List<String> actualCarsStatus = this.memoryParkingService.getStatus();
        List<String> expectedCarStatus = new LinkedList<>();
        expectedCarStatus.add(Message.PACKED_CAR_STATUS_HEADER);
        assertThat(actualCarsStatus, is(expectedCarStatus));
    }

    private void givenParkedCars() {
        Car car1 = Car.newBuilder().withRegistration("RJ-01-BC-2345").withColor("WHITE").build();
        Car car2 = Car.newBuilder().withRegistration("RJ-01-VX-1345").withColor("BLUE").build();
        Car car3 = Car.newBuilder().withRegistration("RJ-01-ZA-4567").withColor("BLUE").build();
        Car car4 = Car.newBuilder().withRegistration("RJ-01-ZA-4321").withColor("WHITE").build();
        this.memoryParkingService.park(car1);
        this.memoryParkingService.park(car2);
        this.memoryParkingService.park(car3);
        this.memoryParkingService.park(car4);
    }

    private void vacateSlots() {
        for (int i = 1; i <= DEFAULT_NO_SLOTS; i++) {
            memoryParkingService.leave(i);
        }
    }

    private List<String> expectedCarsStatus() {
        List<String> carsAndStatus = new LinkedList<>();
        carsAndStatus.add(Message.PACKED_CAR_STATUS_HEADER);
        carsAndStatus.add(String.format(Message.PARKED_CAR_STATUS, 1,"RJ-01-BC-2345", "WHITE"));
        carsAndStatus.add(String.format(Message.PARKED_CAR_STATUS, 2,"RJ-01-VX-1345", "BLUE"));
        carsAndStatus.add(String.format(Message.PARKED_CAR_STATUS, 3,"RJ-01-ZA-4567", "BLUE"));
        carsAndStatus.add(String.format(Message.PARKED_CAR_STATUS, 4,"RJ-01-ZA-4321", "WHITE"));
        return carsAndStatus;
    }

}