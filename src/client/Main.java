package client;

import com.beust.jcommander.*;
import util.Command;

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
    Command.CommandType commandType;
    @Parameter(names = {"--index", "-i"}, description = "Index of data")
    int index;
    @Parameter(names = {"--message", "-m"}, description = "Message data")
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
                Command command = new Command(commandType, index, message);
                Client client = new Client(socket, command);
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
