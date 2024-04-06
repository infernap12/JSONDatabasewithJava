package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session {
    private final Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
    }

    void start() {
        this.run();
    }

    private void run() {
        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            String input = inputStream.readUTF();
            System.out.println("Received: " + input);
            String output = "A record # 12 was sent!";
            outputStream.writeUTF(output);
            System.out.println("Sent: " + output);
            socket.close();
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
}
