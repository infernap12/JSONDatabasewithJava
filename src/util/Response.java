package util;

public class Response {
    ResponseType response;
    String value;
    String reason;

    public Response(boolean pass, String result, String reason) {
        this.value = result;
        this.response = pass ? ResponseType.OK : ResponseType.ERROR;
        this.reason = reason;
    }

    public enum ResponseType {
        OK,
        ERROR;
    }
}
