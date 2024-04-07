package server;

import util.Command;

import java.io.*;
import java.net.Socket;

public class Session {
    private final Socket socket;
    JSONDatabase db;

    public Session(Socket socket, JSONDatabase db) {
        this.socket = socket;
        this.db = db;
    }

    void start() {
        this.run();
    }

    private void run() {
        try (
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                BufferedInputStream bis = new BufferedInputStream(dataInputStream);
                ObjectInputStream ois = new ObjectInputStream(bis);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            Command command = (Command) ois.readObject();
            System.out.println("SReceived: " + command.toString());
            String output = db.execute(command);
            outputStream.writeUTF(output);
            outputStream.flush();
            System.out.println("SSent: " + output);
            socket.close();
            if (command.getCommandType() == Command.CommandType.EXIT) {
                System.exit(0);
            }
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
