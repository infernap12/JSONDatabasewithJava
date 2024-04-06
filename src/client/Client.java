package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;

    Client(Socket socket) {
        this.socket = socket;
    }

    public void execute() throws IOException {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            Scanner scanner = new Scanner(System.in);
            //while (true) {//menu
                String userInput = "Give me a record # 12";//scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    System.exit(0);
                } else {
                    System.out.println("Client started!");
                    output.writeUTF(userInput);
                    System.out.println("Sent: " + userInput);
                    System.out.println("Received: " + input.readUTF());
                }
            //}
        }
    }
}
