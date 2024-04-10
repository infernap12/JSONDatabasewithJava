package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import util.Request;
import util.Response;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

public class Session implements Callable<Boolean> {
    private final Socket socket;
    JSONDatabaseDAO dao;

    public Session(Socket socket, JSONDatabaseDAO dao) {
        this.socket = socket;
        this.dao = dao;
    }

    public Boolean call() {
        try (
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            String input = dataInputStream.readUTF();
            System.out.println("Server Received: " + input);
            Request request = new Gson().fromJson(input, Request.class);
            Response response = invoke(request);
            String responseJson = new Gson().toJson(response);
            outputStream.writeUTF(responseJson);
            outputStream.flush();
            System.out.println("Server Sent: " + responseJson);
            socket.close();
            if (request.getType() == Request.RequestType.exit) {
                return true;
            }
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return false;
    }

    private Response invoke(Request request) {
        boolean isSuccessful = false;
        JsonElement result = null;
        String reason = null;

        switch (request.getType()) {
            case set -> {
                isSuccessful = dao.set(request.getKey(), request.getValue());
            }
            case get -> {
                result = dao.get(request.getKey());
                if (result == null) {
                    reason = "No such key";
                } else {
                    isSuccessful = true;
                }
            }
            case delete -> {
                isSuccessful = dao.delete(request.getKey());
                if (!isSuccessful) {
                    reason = "No such key";
                }
            }
            case exit -> {
                isSuccessful = true;
                //System.exit(0);
            }
        }
        return new Response(isSuccessful, result, reason);
    }
}
