package server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        ExecutorService executor = Executors.newCachedThreadPool();
        try (ServerSocket server = new ServerSocket(PORT)) {
            server.setSoTimeout(1000);
            List<Future<Boolean>> futures = new ArrayList<>();
            while (!isTerminationReady) {
                try {
                    Session session = new Session(server.accept(), db);
                    Future<Boolean> future = executor.submit(session);
                    futures.add(future);
                } catch (SocketTimeoutException e) {
                    // Timeout exception is expected, just ignore and continue
                }
                Iterator<Future<Boolean>> iterator = futures.iterator();
                while (iterator.hasNext()) {
                    Future<Boolean> future = iterator.next();
                    if (future.isDone()) {
                        iterator.remove();
                        if (future.get()) {
                            isTerminationReady = true;
                            break;
                        }
                    }
                }


            }
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
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
