package util;

import com.google.gson.JsonElement;

public class Response {
    ResponseType response;
    JsonElement value;
    String reason;

    public Response(boolean pass, JsonElement value, String reason) {
        this.value = value;
        this.response = pass ? ResponseType.OK : ResponseType.ERROR;
        this.reason = reason;
    }

    public enum ResponseType {
        OK,
        ERROR
    }
}
