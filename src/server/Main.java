package server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

public class Main {
    @Parameter(names = {"--port", "-p"}, description = "Port to listen on")
    static int PORT = 25565;
    @Parameter(names = {"--test", "-b"}, description = "test flag")
    public static boolean IS_TESTING = false;
    static boolean isTerminationReady = false;

    public static void main(String[] argv) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }


    private void run() {
        JSONDatabaseDAO db = new JSONDatabaseDAO();
        System.out.println("Server started!");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try (ServerSocket server = new ServerSocket(PORT)) {
            server.setSoTimeout(1000);
            while (!isTerminationReady) {
                try {
                    Session session = new Session(server.accept(), db);
                    Future<Boolean> future = executor.submit(session);
                    isTerminationReady = future.get();
                } catch (SocketTimeoutException e) {
                    continue;
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


                //need to handle executor shutdown somehow?
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        try {
            if (executor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.exit(0);
            } else {
                System.exit(1);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
