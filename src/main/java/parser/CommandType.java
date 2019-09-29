package parser;

import java.util.HashMap;
import java.util.Map;

public enum CommandType {
    CREATE_PARKING_LOT("create_parking_lot"),
    PARK("park"),
    LEAVE("leave"),
    STATUS("status"),
    REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR("registration_numbers_for_cars_with_colour"),
    SLOT_NUMBERS_FOR_CARS_WITH_COLOUR("slot_numbers_for_cars_with_colour"),
    SLOT_NUMBER_FOR_REGISTRATION_NUMBER("slot_number_for_registration_number"),
    EXIT("exit");

    private String commandText;

    CommandType(String commandText) {
        this.commandText = commandText;
    }

    public String getCommandText() {
        return commandText;
    }

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, CommandType> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(CommandType commandType : CommandType.values())
        {
            lookup.put(commandType.getCommandText(), commandType);
        }
    }

    //This method can be used for reverse lookup purpose
    public static CommandType get(String commandText)
    {
        return lookup.get(commandText);
    }
}
