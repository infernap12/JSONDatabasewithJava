package server;

import com.beust.jcommander.Parameter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    @Parameter(names = {"--port", "-p"}, description = "Port to listen on")
    static int PORT = 25565;
    static boolean isTerminationReady = false;

    public static void main(String[] args) {
        JSONDatabaseDAO db = new JSONDatabaseDAO();
        System.out.println("Server started!");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        List<Future<Boolean>> resultList = new ArrayList<>();
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
