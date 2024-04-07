package server;

import com.beust.jcommander.Parameter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Main {
    @Parameter(names = {"--port", "-p"}, description = "Port to listen on")
    static int PORT = 25565;
    static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        JSONDatabase db = new JSONDatabase();
        System.out.println("Server started!");

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Session session = new Session(server.accept(), db);
                session.start(); // does not block this server thread// it does until i let it not
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
