import parser.CommandParser;
import parser.CommandType;
import services.CommandService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Runner {

    public static void main(String[] args) {
        CommandService commandService = new CommandService();
        if (args != null && args.length > 0) {
            List<String> commandTexts = readLines(args[0]);
            commandTexts.stream().forEach(commandText -> processInput(commandService,
                    commandText, CommandParser.fetchCommandType(commandText)));
        } else {
            PerformCliOperations(commandService);
        }
    }

    private static void PerformCliOperations(CommandService commandService) {
        Scanner scn = new Scanner(System.in);
        System.out.print("$ ");
        while (true) {
            String enteredInput = scn.nextLine();
            CommandType commandType = CommandParser.fetchCommandType(enteredInput);
            if (Objects.nonNull(commandType) && processInput(commandService, enteredInput, commandType)) {
                break;
            }
            System.out.print("$ ");
        }
    }

    private static boolean processInput(CommandService commandService, String enteredInput, CommandType commandType) {
        try {
            if (commandType.equals(CommandType.CREATE_PARKING_LOT)) {
                commandService.activateCommandCreateParkingLot(enteredInput);
            } else if (commandType.equals(CommandType.PARK)) {
                commandService.activateCommandPark(enteredInput);
            } else if (commandType.equals(CommandType.LEAVE)) {
                commandService.activateCommandLeave(enteredInput);

            } else if (commandType.equals(CommandType.STATUS)) {
                commandService.activateCommandStatus(enteredInput);

            } else if (commandType.equals(CommandType.REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR)) {
                commandService.activateCommandRegistrationNumbersForCarsWithColour(enteredInput);

            } else if (commandType.equals(CommandType.SLOT_NUMBERS_FOR_CARS_WITH_COLOUR)) {
                commandService.activateCommandSlotNumbersForCarsWithColour(enteredInput);
            } else if (commandType.equals(CommandType.SLOT_NUMBER_FOR_REGISTRATION_NUMBER)) {
                commandService.activateCommandSlotNumberForRegistrationNumber(enteredInput);
            } else if (commandType.equals(CommandType.EXIT)) {
                return true;
            } else {
                System.out.println("Wrong Command!!! Please review your input");
            }
        } catch (Exception ex) {
            System.out.println("Wrong Command!!! Please review your input");
        }
        return false;
    }

    private static List<String> readLines(String file) {
        List<String> commandTexts = new LinkedList<>();
        try {
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNextLine()) {
                commandTexts.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return commandTexts;
    }
}
