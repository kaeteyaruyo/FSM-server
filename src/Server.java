import java.io.*;
import java.net.*;

public class Server {
	public static int port = 6666;
	public static void main( String args[] ) {
		try {
			ServerSocket server = new ServerSocket( port );
			System.out.println( "Server is created on " + port + ", waitting for connection..." );
			Socket request = server.accept();
			System.out.println( "Client is connected, IP: " + request.getInetAddress() );
			request.close();
		}
		catch (IOException e) {
			System.out.println( "Error" );
		}
	}
}
