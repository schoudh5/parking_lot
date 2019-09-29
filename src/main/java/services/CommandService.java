package services;

import java.util.Objects;

public class CommandService {
    private MemoryParkingService parkingService;

    public void activateCommandSlotNumberForRegistrationNumber(String enteredInput) {
        String[] inputs = enteredInput.split(" ");
        if (Objects.nonNull(parkingService) && inputs.length == 2) {
            System.out.println(parkingService.getSlot(inputs[1]));
        } else{
            System.out.println("Wrong Command!!! Please review your input");
        }
    }

    public void activateCommandSlotNumbersForCarsWithColour(String enteredInput) {
        String[] inputs = enteredInput.split(" ");
        if (Objects.nonNull(parkingService) && inputs.length == 2) {
            System.out.println(parkingService.getSlotsByColor(inputs[1]));
        } else{
            System.out.println("Wrong Command!!! Please review your input");
        }
    }

    public void activateCommandRegistrationNumbersForCarsWithColour(String enteredInput) {
        String[] inputs = enteredInput.split(" ");
        if (Objects.nonNull(parkingService) && inputs.length == 2) {
            System.out.println(parkingService.getRegistrationsByColor(inputs[1]));
        } else{
            System.out.println("Wrong Command!!! Please review your input");
        }
    }

    public void activateCommandStatus(String enteredInput) {
        String[] inputs = enteredInput.split(" ");
        if (Objects.nonNull(parkingService) && inputs.length == 1) {
            parkingService.getStatus().forEach(line -> System.out.println(line));
        } else{
            System.out.println("Wrong Command!!! Please review your input");
        }
    }

    public void activateCommandLeave(String enteredInput) {
        String[] inputs = enteredInput.split(" ");
        if (Objects.nonNull(parkingService) && inputs.length == 2) {
            System.out.println(parkingService.leave(Integer.valueOf(inputs[1])));
        } else{
            System.out.println("Wrong Command!!! Please review your input");
        }
    }

    public void activateCommandPark(String enteredInput) {
        String[] inputs = enteredInput.split(" ");
        if (Objects.nonNull(parkingService) && inputs.length == 3) {
            System.out.println(parkingService.park(
                    Car.newBuilder()
                            .withRegistration(inputs[1])
                            .withColor(inputs[2])
                            .build()));
        } else{
            System.out.println("Wrong Command!!! Please review your input");
        }
    }

    public void activateCommandCreateParkingLot(String enteredInput) {
        int capacity = Integer.valueOf(enteredInput.split(" ")[1]);
        if(capacity==0){
            System.out.println("Wrong Input!!");
        } else{
            parkingService = MemoryParkingService.getInstance(capacity);
            System.out.println(String.format("Created a parking lot with %d slots", capacity));
        }
    }
}
