package client;

import com.beust.jcommander.*;
import com.google.gson.Gson;
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
    @Parameter(names = {"--key", "-k"}, description = "Key for data")
    String key;
    @Parameter(names = {"--message", "-m", "-v"}, description = "Message data")
    String message;
    @Parameter(names = {"--file", "-in"}, description = "Direct JSON request file")
    String requestFileName = null;

    {
        File dataFolder = new File(System.getProperty("user.dir") + "/src/client/data/");
        dataFolder.mkdirs();
    }


    public static void main(String[] argv) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    private void run() {
        try {
            try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT)) {
                Request request = buildRequest();
                Client client = new Client(socket, request);
                client.execute();
            }
        } catch (UnknownHostException e) {
            System.out.println("Oop, bad IP");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Request buildRequest() {
        Request request = null;
        if (requestFileName != null) {
            try {
                FileReader fileReader = new FileReader(System.getProperty("user.dir") + "/src/client/data/" + requestFileName);
                request = new Gson().fromJson(fileReader, Request.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            request = new Request(commandRequestType, key, message);
        }
        return request;
    }

}
