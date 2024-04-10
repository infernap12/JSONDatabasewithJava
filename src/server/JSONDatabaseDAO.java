package server;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.*;

public class JSONDatabaseDAO implements IDatabaseDAO {


    Gson prettyGson;
    ReadWriteLock lock;

    Lock readLock;
    Lock writeLock;
    File databaseFile;

    public JSONDatabaseDAO() {
        this.lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();

        if (Main.IS_TESTING) {
            this.databaseFile = new File("C:\\Users\\james\\IdeaProjects\\JSON Database with Java\\JSON Database with Java\\task\\src\\server\\data\\db.json");
        } else {
            this.databaseFile = new File(System.getProperty("user.dir") + "/src/server/data/db.json");
        }
        this.prettyGson = new GsonBuilder().setPrettyPrinting().create();
        //noinspection ResultOfMethodCallIgnored
        databaseFile.getParentFile().mkdirs();
        if (!databaseFile.exists()) {
            writeDB(new JsonObject());
        }
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
            prettyGson.toJson(db, bufferedWriter);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static String[] buildPathArray(JsonElement keyPath) {
        List<String> pathList = new ArrayList<>();
        keyPath.getAsJsonArray().iterator().forEachRemaining(x -> pathList.add(x.getAsString()));
        return pathList.toArray(new String[0]);
    }

    private static JsonObject getTargetObject(JsonElement keyPath, JsonObject db) {
        if (keyPath.isJsonPrimitive()) {
            return db;
        }

        JsonObject targetObject = db;
        String[] pathArray = buildPathArray(keyPath);

        for (int i = 0; i < pathArray.length - 1; i++) {
            targetObject = targetObject.getAsJsonObject(pathArray[i]);
        }
        return targetObject;
    }

    private String getKey(JsonElement key) {
        if (key.isJsonPrimitive()) {
            return key.getAsString();
        } else {
            String keyString = null;
            for (JsonElement jsonElement : key.getAsJsonArray()) {
                keyString = jsonElement.getAsString();
            }
            return keyString;
        }
    }

    @Override
    public boolean set(JsonElement key, JsonElement value) {
        writeLock.lock();
        JsonObject db = getDB();
        JsonObject targetObject = getTargetObject(key, db);
        targetObject.add(getKey(key), value);
        writeDB(db);
        writeLock.unlock();
        return true;
    }

    @Override
    public boolean delete(JsonElement keyPath) {
        writeLock.lock();
        JsonObject db = getDB();
        JsonObject targetObject = getTargetObject(keyPath, db);
        JsonElement elem = targetObject.remove(getKey(keyPath));
        writeDB(db);
        writeLock.unlock();
        return elem != null;
    }

    @Override
    public JsonElement get(JsonElement keyPath) {
        readLock.lock();
        JsonObject db = getDB();
        JsonObject targetObject = getTargetObject(keyPath, db);
        JsonElement output = targetObject.get(getKey(keyPath));
        readLock.unlock();
        return output;
    }

}
