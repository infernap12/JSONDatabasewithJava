package server;

public interface IDatabaseDAO {
    boolean set(String key, String msg);

    boolean delete(String key);

    String get(String key);
}
