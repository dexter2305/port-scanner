import javax.net.SocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

public class Connector implements Callable<Socket> {
    private final String hostname;
    private final int port;
    Connector(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }
    Socket connect(){
        Socket socket = null;
        try {
            socket = SocketFactory.getDefault().createSocket(hostname, port);
        } catch (IOException e) {
        }
        return socket;
    }

    @Override
    public Socket call() throws Exception {
        return connect();
    }
}
