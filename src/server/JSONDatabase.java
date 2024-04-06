package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JSONDatabase implements IDatabase{


    List<String> fakeDB = new ArrayList<>(Collections.nCopies(100, ""));


    @Override
    public boolean set(int index, String text) {
        if (index < 0 || index > 99) {
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
        return fakeDB.get(index);
    }
}
