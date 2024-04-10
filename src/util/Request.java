package util;

import com.google.gson.JsonElement;

import java.io.Serializable;

public class Request implements Serializable {
    RequestType type;
    JsonElement key;
    JsonElement value;

    public Request(RequestType type, JsonElement key, JsonElement value) {
        this.type = type;
        this.value = value;
        this.key = key; //key can be either JsonArray or JsonPrimitive String
    }

    public enum RequestType {
        set,
        get,
        delete,
        exit
    }

    public RequestType getType() {
        return type;
    }


    public JsonElement getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }

    @Override
    public String toString() {
        return """
                type  = %s
                key   = %s
                value = %s""".formatted(type.toString(), key.toString(), String.valueOf(value));
    }
}
