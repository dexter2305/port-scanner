import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PortScanner {

    private final static int DEFAULT_THREAD_POOL_SIZE = 100;
    private String hostname;
    private int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;


    public PortScanner(String hostname){
        this.hostname = hostname;
    }

    public void setThreadpoolSize(int poolSize){
        threadPoolSize = poolSize;
    }

    public String getHostname() {
        return hostname;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void scan(int startPortNumber, int finalPortNumber){
        System.out.printf("Searching range [%d-%d] in \"%s\" with threadpool(%d) \n",startPortNumber, finalPortNumber, hostname, threadPoolSize);
        final LocalDateTime startTime = LocalDateTime.now();
        final ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        final List<Future<Socket>> listOfFutures = new ArrayList<>();
        for (int portNumber = startPortNumber; portNumber <= finalPortNumber; portNumber++) {
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
                socket.close();
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (IOException e) {
            }
        });
        executorService.shutdown();
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("Total time taken : " + duration.toString());
    }

    public void scan(){
        scan(1,65535);
    }
}
