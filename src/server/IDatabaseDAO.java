package server;

import com.google.gson.JsonElement;

public interface IDatabaseDAO {
    boolean set(JsonElement key, JsonElement value);

    boolean delete(JsonElement key);

    JsonElement get(JsonElement key);
}
