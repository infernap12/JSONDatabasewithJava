package server;

public interface IDatabase {
    boolean set(int index, String text);

    boolean delete(int index);

    String get(int index);
}
