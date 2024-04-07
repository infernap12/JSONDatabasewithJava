package server;

public interface IDatabase {
    boolean set(String key, String msg);

    boolean delete(String key);

    String get(String key);
}
