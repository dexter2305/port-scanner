import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by on 1/10/2016.
 */
public class PortScanner {

    private final static int START_PORT_NUMBER = 1;
    private final static int END_PORT_NUMBER = 65535;
    private final static String hostname = "192.168.1.1";
    private final static int THREADPOOL_SIZE = 100;

    public static void main(String[] args) {
        System.out.printf("Searching from %d %d in %s with threadpool(%d) \n",START_PORT_NUMBER, END_PORT_NUMBER, hostname, THREADPOOL_SIZE);
        final LocalDateTime startTime = LocalDateTime.now();
        final ExecutorService executorService = Executors.newFixedThreadPool(THREADPOOL_SIZE);
        final List<Future<Socket>> listOfFutures = new ArrayList<>();
        for (int portNumber = START_PORT_NUMBER; portNumber <= END_PORT_NUMBER; portNumber++) {
            Connector connector = new Connector(hostname, portNumber);
            Future<Socket> future = executorService.submit(connector);
            listOfFutures.add(future);
        }
        listOfFutures.parallelStream().filter(socketFuture -> {
            try {
                return socketFuture.get() != null;
            } catch (InterruptedException e) {
                return false;
            } catch (ExecutionException e) {
                return false;
            }
        }).forEach(socketFuture1 -> {
            try {
                Socket socket = socketFuture1.get();
                System.out.println("Connected @ " + socket.getInetAddress().getHostAddress() + ":" + socket.getRemoteSocketAddress());
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            }
        });
        executorService.shutdown();
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("Total time taken : " + duration.toString());
    }
}
