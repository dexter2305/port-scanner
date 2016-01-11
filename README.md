# port-scanner
A simple command line interfaced application to scan for available ports. Target hostname is currently hard-coded and the ports range scanned is 1-65535. Scanning includes the special ports like TELNET, FTP, SMTP. 

Usage: 

PortScanner scanner = new Port("192.168.1.1");
scanner.scan();

API scanner.scan() scans for port numbers ranging from 1 to 65535. 

Use scanner.scan(int startNumber, int endNumber) to limit to definite range of port numbers. 
