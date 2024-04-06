package server;

import javax.xml.crypto.Data;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        JSONDatabase db = new JSONDatabase();
        System.out.println("Hello, world!");

        while (true) {//menu
            String[] input = SCANNER.nextLine().split(" ");
            Command command = Command.get(input);
            boolean result = true;
            String output = null;
            switch (command) {
                case SET -> {
                    result = db.set(command.index, command.text);
                }
                case GET -> {
                    output = db.get(command.index);
                    result = !output.isEmpty();
                }
                case DELETE -> {
                    result = db.delete(command.index);
                }
                case EXIT -> System.exit(0);
                default -> throw new IllegalStateException("Unexpected value: " + command);
            }
            if (result) {
                System.out.println(Objects.requireNonNullElse(output, "OK"));
            } else {
                System.out.println("ERROR");
            }
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
