package parser;

public final class CommandParser {

    public static CommandType fetchCommandType(String command) {
        String commandText = command.split(" ")[0];
        return CommandType.get(commandText);
    }
}
