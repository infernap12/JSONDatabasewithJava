package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    static int PORT = 25565;
    static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        init(args);
        JSONDatabase db = new JSONDatabase();
        System.out.println("Server started!");

        try (ServerSocket server = new ServerSocket(PORT)) {
            //while (true) {
                Session session = new Session(server.accept());
                session.start(); // does not block this server thread// it does until i let it not
           // }
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

    private static void init(String[] args) {
        if (args.length % 2 == 0) {
            Map<String, String> argumentMap = IntStream.range(0, args.length / 2)
                    .boxed()
                    .collect(Collectors.toMap(i -> args[i * 2], i -> args[i * 2 + 1]));
//            if (argumentMap.containsKey("-i")) {
//                address = argumentMap.get("-i");
//            }
            if (argumentMap.containsKey("-p")) {
                PORT = Integer.parseInt(argumentMap.get("-p"));
            }
        } else {
            System.out.println("Invalid args" + String.join(" ", args));
            System.out.println("-i for ip address\neg. 0.0.0.0\n-p for port\neg. 8080");
            System.exit(1);
        }
    }

    enum Command {
        SET,
        GET,
        DELETE,
        EXIT;

        String text;
        int index;

        public void setText(String text) {
            this.text = text;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        static Command get(String[] input) {
            Command output = Command.valueOf(input[0].toUpperCase());
            if (input.length > 1) {
                output.setIndex(Integer.parseInt(input[1]) - 1);
            }
            if (input.length > 2) {
                output.setText(Arrays.stream(input).skip(2).collect(Collectors.joining(" ")));
            }
            return output;
        }
    }

}
