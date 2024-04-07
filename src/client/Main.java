package client;

import com.beust.jcommander.*;
import util.Request;

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
                Request request = new Request(commandRequestType, key, message);
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

}
