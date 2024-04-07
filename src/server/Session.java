package server;

import com.google.gson.Gson;
import util.Request;
import util.Response;

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
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            String input = dataInputStream.readUTF();
            System.out.println("Server Received: " + input);
            Request request = new Gson().fromJson(input, Request.class);
            Response response = db.execute(request);
            String responseJson = new Gson().toJson(response);
            outputStream.writeUTF(responseJson);
            outputStream.flush();
            System.out.println("Server Sent: " + responseJson);
            socket.close();
            if (request.getType() == Request.RequestType.exit) {
                System.exit(0);
            }
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
}
