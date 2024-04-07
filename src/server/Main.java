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





//        while (true) {//menu
//            String[] input = SCANNER.nextLine().split(" ");
//            Command command = Command.get(input);
//            boolean result = true;
//            String output = null;
//            switch (command) {
//                case SET -> {
//                    result = db.set(command.index, command.text);
//                }
//                case GET -> {
//                    output = db.get(command.index);
//                    result = !output.isEmpty();
//                }
//                case DELETE -> {
//                    result = db.delete(command.index);
//                }
//                case EXIT -> System.exit(0);
//                default -> throw new IllegalStateException("Unexpected value: " + command);
//            }
//            if (result) {
//                System.out.println(Objects.requireNonNullElse(output, "OK"));
//            } else {
//                System.out.println("ERROR");
//            }
//        }
    }



}
