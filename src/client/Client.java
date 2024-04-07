package client;

import util.Command;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;
    Command command;

    Client(Socket socket, Command command) {
        this.socket = socket;
        this.command = command;
    }

    public void execute() throws IOException {
        try (
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                BufferedOutputStream bos = new BufferedOutputStream(dataOutputStream);
                ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            String type = command.getCommandType() != null ? command.getCommandType().name() : "";

            String index = String.valueOf(command.getIndex()) == null ? "" : String.valueOf(command.getIndex());
            String msg = command.getMessage() != null ? command.getMessage() : "";
            oos.writeObject(command);
            oos.flush();
            System.out.println("CSent: %s %s %s".formatted(type, index, msg));
            String input = dataInputStream.readUTF();
            System.out.println("CReceived: " + input);

        }

    }
}

