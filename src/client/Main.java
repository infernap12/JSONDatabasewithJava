package client;

import javax.xml.stream.XMLOutputFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    static String address = "127.0.0.1";
    static int port = 25565;


    public static void main(String[] args) {
        init(args);
        int attempts = 10;
        while (attempts >= 0) {
            try {
                try (Socket socket = new Socket(InetAddress.getByName(address), port)) {
                    Client client = new Client(socket);
                    client.execute();
                    break;
                }
            } catch (UnknownHostException e) {
                System.out.println("Oop, bad IP");
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            attempts--;
        }
    }

    private static void init(String[] args) {
        if (args.length % 2 == 0) {
            Map<String, String> argumentMap = IntStream.range(0, args.length / 2)
                    .boxed()
                    .collect(Collectors.toMap(i -> args[i * 2], i -> args[i * 2 + 1]));
            if (argumentMap.containsKey("-i")) {
                address = argumentMap.get("-i");
            }
            if (argumentMap.containsKey("-p")) {
                port = Integer.parseInt(argumentMap.get("-p"));
            }
        } else {
            System.out.println("Invalid args" + String.join(" ", args));
            System.out.println("-i for ip address\neg. 0.0.0.0\n-p for port\neg. 8080");
            System.exit(1);
        }
    }
}
