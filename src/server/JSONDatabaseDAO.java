package server;

import com.google.gson.*;
import util.Request;
import util.Response;

import java.io.*;
import java.util.concurrent.locks.*;

public class JSONDatabaseDAO implements IDatabaseDAO {


    ReadWriteLock lock;

    Lock readLock;
    Lock writeLock;
    File databaseFile;

    public JSONDatabaseDAO() {
        this.lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
        this.databaseFile = new File(System.getProperty("user.dir") + "/src/server/data/db.json");
        databaseFile.getParentFile().mkdirs();
        writeDB(new JsonObject());
    }

    private JsonObject getDB() {
        JsonObject db;
        try {
            FileReader fileReader = new FileReader(databaseFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            db = new Gson().fromJson(bufferedReader, JsonObject.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (db == null) {
            db = new JsonObject();
        }
        return db;
    }

    void writeDB(JsonObject db) {
        try {
            FileWriter fileWriter = new FileWriter(databaseFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            new Gson().toJson(db, bufferedWriter);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean set(String key, String msg) {
        writeLock.lock();
        JsonObject db = getDB();
        db.addProperty(key, msg);
        writeDB(db);
        writeLock.unlock();
        return true;
    }

    @Override
    public boolean delete(String key) {
        writeLock.lock();
        JsonObject db = getDB();
        JsonElement elem = db.remove(key);
        writeDB(db);
        writeLock.unlock();
        return elem != null;
    }

    @Override
    public String get(String key) {
        readLock.lock();
        JsonObject db = getDB();
        JsonElement output = db.get(key);
        readLock.unlock();
        return output == null ? null : output.getAsString();
    }

//    public Response execute(Request request) {
//        boolean isSucessful = false;
//        String result = null;
//        String reason = null;
//
//        switch (request.getType()) {
//            case set -> {
//                isSucessful = set(request.getKey(), request.getValue());
//            }
//            case get -> {
//                result = get(request.getKey());
//                if (result == null) {
//                    reason = "No such key";
//                } else {
//                    isSucessful = true;
//                }
//            }
//            case delete -> {
//                isSucessful = delete(request.getKey());
//                if (!isSucessful) {
//                    reason = "No such key";
//                }
//            }
//            case exit -> {
//                isSucessful = true;
//                //System.exit(0);
//            }
//        }
//        return new Response(isSucessful, result, reason);
//
//    }
}
