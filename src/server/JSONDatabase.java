package server;

import util.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JSONDatabase implements IDatabase{


    List<String> fakeDB = new ArrayList<>(Collections.nCopies(1000, ""));


    @Override
    public boolean set(int index, String text) {
        if (index < 0 || index > 999) {
            return false;
        } else {
            fakeDB.set(index, text);
        }
        return true;
    }

    @Override
    public boolean delete(int index) {
        return set(index, "");
    }

    @Override
    public String get(int index) {
        String output = fakeDB.get(index);
        if (output.isBlank()) {
            output = null;
        }
        return output;
    }

    public String execute(Command command) {
        boolean pass = false;
        String result = null;
        switch (command.getCommandType()) {
            case SET -> {
                pass = set(command.getIndex(), command.getMessage());
            }
            case GET -> {
                result = get(command.getIndex());
            }
            case DELETE -> {
                pass = delete(command.getIndex());
            }
            case EXIT -> {
                pass = true;
                //System.exit(0);
            }
        }
        if (result != null) {
            return result;
        } else {
            return pass ? "OK" : "ERROR";
        }

    }
}
