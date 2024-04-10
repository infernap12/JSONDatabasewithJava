package client;

import com.google.gson.Gson;
import util.Request;

import java.io.*;
import java.net.Socket;

public class NetClient {
    Socket socket;
    Request request;

    NetClient(Socket socket, Request request) {
        this.socket = socket;
        this.request = request;
    }

    public void execute() throws IOException {
        try (
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            String jsonRequest = new Gson().toJson(request);
            dataOutputStream.writeUTF(jsonRequest);
            dataOutputStream.flush();
            System.out.println("Client Sent: " + jsonRequest);
            String input = dataInputStream.readUTF();
            System.out.println("Client Received: " + input);

        }

    }
}

