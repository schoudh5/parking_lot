package parser;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CommandParserTest {

    @Test
    public void fetchCommandType_givenCommandText_thenReturnCommandText() {
        String commandText = "create_parking_lot 6";
        CommandType actualCommandType = CommandParser.fetchCommandType(commandText);
        assertThat(actualCommandType, is(CommandType.CREATE_PARKING_LOT));
    }

    @Test
    public void fetchCommandType_givenInvalidCommandText_thenReturnNull() {
        String commandText = "create_parking_lot_6";
        CommandType actualCommandType = CommandParser.fetchCommandType(commandText);
        assertNull(actualCommandType);
    }
}