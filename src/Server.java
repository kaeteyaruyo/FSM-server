import java.io.*;
import java.net.*;
import org.json.*;

public class Server {
	private int port;
	private ServerSocket serverSocket;
	
	/****************************************************************
	 * Constructor
	 ***************************************************************/
	public Server( int port ) {
		this.port = port;
	}
	/****************************************************************
	 * Start server
	 ***************************************************************/
	public Server start() {
		try {
			serverSocket = new ServerSocket( this.port );
			System.out.println(
				"Server is created on " +
				this.port
			);
		}
		catch ( IOException e ) {
			System.out.println(
				"Fail to start server on port" +
				this.port
			);
			e.printStackTrace();
		}
		return this;
	}
	/****************************************************************
	 * Listen for client request
	 ***************************************************************/
	public Server listen() {
		while( true ) {
			// Client's socket
			Socket clientSocket = null;
			try {
				// Server accept client request
				System.out.println(	"Waitting for connection..." );
				clientSocket = this.serverSocket.accept();
				
				System.out.println(
					"Client is connected, IP: " +
					clientSocket.getInetAddress()
				);
				
				// Build data stream between server and client
				DataInputStream in = new DataInputStream( clientSocket.getInputStream() );
				DataOutputStream out = new DataOutputStream( clientSocket.getOutputStream() );
				
				// Serve client with thread, make it non-blocking IO
				Thread t = new ClientHandler( clientSocket, in, out );
				t.start();
			}
			catch (IOException e) {
				System.out.println( "Fail to listen." );
				e.printStackTrace();
				break;
			}
		}
		return this;
	}
	/****************************************************************
	 * Close server
	 ***************************************************************/
	public Server stop() {
		try {
			this.serverSocket.close();
		}
		catch( IOException e ) {
			e.printStackTrace();
		}
		finally {
			System.out.println( "Server is closed." );
		}
		return this;
	}
}

/****************************************************************
 * Client request handler
 ***************************************************************/
class ClientHandler extends Thread {
	final private Socket clientSocket;
	final private DataInputStream in;
	final private DataOutputStream out;
	
	/****************************************************************
	 * Constructor
	 ***************************************************************/
	public ClientHandler( Socket clientSocket, DataInputStream in, DataOutputStream out ) {
		this.clientSocket = clientSocket;
		this.in = in;
		this.out = out;
	}
	
	/****************************************************************
	 * Multi-thread implementation
	 ***************************************************************/
	@Override
	public void run() {
		try {
			JSONObject request = new JSONObject( this.in.readUTF() );
			JSONObject response;
			String session = "";
			switch( request.getString( "event" ) ) {
			// Registration event
			case "regist":
				session = Database.regist(request);
				if( !session.equals("") ) {
					System.out.println("Regist successed with session id = " + session);
					response = new JSONObject()
						.put( "auth", "yes" )
						.put( "session", session );
				}
				// Failed to regist.
				else {
					System.out.println("Regist fail");
					response = new JSONObject()
						.put( "auth", "no" );
				}
				this.out.writeUTF( response.toString() );
				break;
			// Authentication event
			case "authenticate":
				session = Database.authenticate(request);
				if( !session.equals("") ) {
					System.out.println("Login successed with session id = " + session);
					response = new JSONObject()
						.put( "auth", "yes" )
						.put( "session", session );
				}
				// Failed to authenticate.
				else {
					System.out.println("Login fail");
					response = new JSONObject()
						.put( "auth", "no" );
				}
				this.out.writeUTF( response.toString() );
				break;
			case "get task":
					this.out.writeUTF( "" );
			}
			this.in.close();
			this.out.close();
			this.clientSocket.close();
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		finally {
		}
	}
}
