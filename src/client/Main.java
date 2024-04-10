package client;

import com.beust.jcommander.*;
import com.google.gson.*;
import util.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    @Parameter(names = {"--address", "-a"}, description = "Ip address of server")
    static String ADDRESS = "127.0.0.1";
    @Parameter(names = {"--port", "-p"}, description = "Port of server")
    static int PORT = 25565;
    @Parameter(names = {"--type", "-t"}, description = "Command type (get, set, delete, exit)")
    Request.RequestType commandRequestType;
    @Parameter(names = {"--key", "-k"}, description = "Key/KeyPath for data")
    String key;
    @Parameter(names = {"--message", "-m", "-v"}, description = "Message data")
    String message;
    @Parameter(names = {"--file", "-in"}, description = "Direct JSON request file")
    String requestFileName = null;
    @Parameter(names = {"--test", "-b"}, description = "testing debug flag")
    public static boolean IS_TESTING = false;

    File dataFolder;
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static void main(String[] argv) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    private void run() {
        if (IS_TESTING) {
            dataFolder = new File("C:\\Users\\james\\IdeaProjects\\JSON Database with Java\\JSON Database with Java\\task\\src\\client\\data");
        } else {
            dataFolder = new File(System.getProperty("user.dir") + "/src/client/data/");
        }
        //noinspection ResultOfMethodCallIgnored
        dataFolder.mkdirs();
        try {
            try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT)) {
                Request request = buildRequest();
                NetClient netClient = new NetClient(socket, request);
                netClient.execute();
            }
        } catch (UnknownHostException e) {
            System.out.println("Oop, bad IP");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Request buildRequest() {
        Request request;
        if (requestFileName != null) {
            try {
                FileReader fileReader = new FileReader(new File(dataFolder, requestFileName));
                request = gson.fromJson(fileReader, Request.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            JsonElement value = message == null ? null : new JsonPrimitive(message);
            JsonElement keyPath = key == null ? null : new JsonPrimitive(key);
            request = new Request(commandRequestType, keyPath, value);
        }
        return request;
    }

}
