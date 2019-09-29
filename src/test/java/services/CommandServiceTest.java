package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CommandServiceTest {

    @Mock
    private MemoryParkingService parkingService;

    @InjectMocks CommandService commandService;

    @Test
    public void activateCommandSlotNumberForRegistrationNumber_givenValidInput_thenInvokeParkingServiceMethod() {
      String commandText = "slot_number_for_registration_number KA-01-HH-3141";
      this.commandService.activateCommandSlotNumberForRegistrationNumber(commandText);
      verify(parkingService, times(1)).getSlot("KA-01-HH-3141");
    }

    @Test
    public void activateCommandSlotNumbersForCarsWithColour_givenValidInput_thenInvokeParkingServiceMethod() {
        String commandText = "slot_numbers_for_cars_with_colour White";
        this.commandService.activateCommandSlotNumbersForCarsWithColour(commandText);
        verify(parkingService, times(1)).getSlotsByColor("White");
    }

    @Test
    public void activateCommandRegistrationNumbersForCarsWithColour_givenValidInput_thenInvokeParkingServiceMethod() {
        String commandText = "registration_numbers_for_cars_with_colour White";
        this.commandService.activateCommandRegistrationNumbersForCarsWithColour(commandText);
        verify(parkingService, times(1)).getRegistrationsByColor("White");
    }

    @Test
    public void activateCommandStatus_givenValidInput_thenInvokeParkingServiceMethod() {
        String commandText = "status";
        this.commandService.activateCommandStatus(commandText);
        verify(parkingService, times(1)).getStatus();
    }

    @Test
    public void activateCommandLeave_givenValidInput_thenInvokeParkingServiceMethod() {
        String commandText = "leave 4";
        this.commandService.activateCommandLeave(commandText);
        verify(parkingService, times(1)).leave(4);
    }

    @Test
    public void activateCommandPark_givenValidInput_thenInvokeParkingServiceMethod() {
        ArgumentCaptor<Car> argument = ArgumentCaptor.forClass(Car.class);
        String commandText = "park KA-01-HH-3141 Black";
        this.commandService.activateCommandPark(commandText);
        verify(parkingService, times(1)).park(argument.capture());
        assertThat(argument.getValue(), is(Car.newBuilder()
                .withRegistration("KA-01-HH-3141")
                .withColor("Black")
                .build()));
    }

    @Test
    public void activateCommandCreateParkingLot_givenValidInput_thenInvokeParkingServiceMethod() {
        String commandText = "create_parking_lot 6";
        commandService.activateCommandCreateParkingLot(commandText);
    }
}