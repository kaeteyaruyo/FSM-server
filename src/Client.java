import java.io.*;
import java.net.*;

public class Client {
	public static int port = 6666;
	public static String serverIP = "localhost";
	public static void main( String args[] ) {
		try {
			System.out.println( "Client is request to server " + serverIP + ":" + port + ", waiting response..." );
			Socket client = new Socket( serverIP, port );
			client.close();
		}
		catch (IOException e) {
			System.out.println( "Error" );
		}
	}
}
