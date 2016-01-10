import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by on 1/10/2016.
 */
public class Main {

    public static void main(String[] args) {


        appraochWithSocketFactoryForSocket();
    }

    public static void appraochWithSocketFactoryForSocket() {
        final String routerIpAddress = "192.168.1.1";
        final int START_PORT_NUMBER = 1780;
        final int MAX_PORT_NUMBER = 65535;
        Socket clientSocket;
        int portNumber = START_PORT_NUMBER;
        System.out.println("Searching for port on " + routerIpAddress + " starting from " + START_PORT_NUMBER);
        while (portNumber < MAX_PORT_NUMBER) {
            try {
                InetSocketAddress address = new InetSocketAddress(routerIpAddress, portNumber);
                clientSocket = SocketFactory.getDefault().createSocket(routerIpAddress,portNumber);
                System.out.print("\n attempt to .. " + address.getHostString() + "/" + address.getHostName() + ":" + address.getPort() + " - ");
                System.out.println("success.Connected? " + clientSocket.isConnected());
            } catch (IOException e) {
                final int numberOfPortsTested = portNumber - START_PORT_NUMBER;
                final int totalNumberOfPorts = MAX_PORT_NUMBER - START_PORT_NUMBER;
                if (numberOfPortsTested > 0 && numberOfPortsTested % 5000 == 0){
                    System.out.println(numberOfPortsTested * 100 /  totalNumberOfPorts + " % completed");
                }else{
                    if (portNumber % 5 == 0){
                        System.out.print(".");
                    }
                }
            }
            portNumber++;
        }
        System.out.print("Search completed ");
    }

    public static void approachWithSocketFactoryWithSingleSocketInstance() {
        final String routerIpAddress = "192.168.1.1";
        final int MAX_PORT_NUMBER = 65535;
        final int DEFAULT_TIMEOUT_IN_SECONDS = 1000 * 60;
        Socket clientSocket;
        try {
            clientSocket = SocketFactory.getDefault().createSocket();
            int portNumber = 0;
            while (portNumber < MAX_PORT_NUMBER) {
                try {
                    InetSocketAddress address = new InetSocketAddress(routerIpAddress, portNumber);
                    System.out.print("attempt to .. " + address.getHostString() + "/" + address.getHostName() + ":" + address.getPort() + " - ");
                    clientSocket.connect(address, DEFAULT_TIMEOUT_IN_SECONDS);
                    System.out.println("success");
                } catch (IOException e) {
                    System.out.println("failed");
                }
                portNumber++;
            }
        } catch (IOException e) {
            System.err.println("Error creating socket from socket factory.");
        }
    }

}
