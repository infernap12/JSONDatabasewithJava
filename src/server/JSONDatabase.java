package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import util.Request;
import util.Response;

public class JSONDatabase implements IDatabase{


    JsonObject db = new JsonObject();

    //add == map.put()
    @Override
    public boolean set(String key, String msg) {
        db.addProperty(key, msg);
        return true;
    }

    @Override
    public boolean delete(String key) {
        JsonElement elem = db.remove(key);
        return elem != null;
    }

    @Override
    public String get(String key) {
        JsonElement output = db.get(key);
        return output == null ? null : output.getAsString();
    }

    public Response execute(Request request) {
        boolean isSucessful = false;
        String result = null;
        String reason = null;

        switch (request.getType()) {
            case set -> {
                isSucessful = set(request.getKey(), request.getValue());
            }
            case get -> {
                result = get(request.getKey());
                if (result == null) {
                    reason = "No such key";
                } else {
                    isSucessful = true;
                }
            }
            case delete -> {
                isSucessful = delete(request.getKey());
                if (!isSucessful) {
                    reason = "No such key";
                }
            }
            case exit -> {
                isSucessful = true;
                //System.exit(0);
            }
        }
        return new Response(isSucessful, result, reason);

    }
}
