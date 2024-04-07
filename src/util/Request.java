package util;

import java.io.Serializable;

public class Request implements Serializable {
    RequestType type;
    String key;
    String value;

    public Request(RequestType type, String key, String message) {
        this.type = type;
        this.key = key;
        this.value = message;
    }

    public enum RequestType {
        set,
        get,
        delete,
        exit;
    }

    public RequestType getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + " " + key + " " + value;
    }
}
