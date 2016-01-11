
public class Main {

    public static void main(String[] args) {
        final String hostname = "192.168.1.1";
        final int START_PORT_NUMBER = 1;
        final int END_PORT_NUMBER = 65535;
        PortScanner portScanner = new PortScanner(hostname);
        portScanner.scan(START_PORT_NUMBER,END_PORT_NUMBER);
    }
}
