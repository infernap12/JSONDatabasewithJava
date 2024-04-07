package util;

import java.io.Serializable;

public class Command implements Serializable {
    CommandType commandType;
    Integer index;
    String message;

    public Command(CommandType commandType, Integer index, String message) {
        this.commandType = commandType;
        this.index = index;
        this.message = message;
    }

    public enum CommandType {
        SET,
        GET,
        DELETE,
        EXIT;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public int getIndex() {
        return index;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return commandType + " " + index + " " + message;
    }
}
