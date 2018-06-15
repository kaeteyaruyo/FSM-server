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
			JSONObject response = new JSONObject();
			JSONArray mailList = null;
			switch( request.getString( "event" ) ) {
			// Registration event
			case "regist":
				response = Database.regist(request);
				if( response != null ) {
					System.out.println("Regist successed with session id = " + response.get("session").toString());
					response.put( "auth", "yes" );
				}
				// Failed to regist.
				else {
					System.out.println("Regist fail");
					response = new JSONObject()
						.put( "auth", "no" );
				}
				break;
			// Authentication event
			case "authenticate":
				response = Database.authenticate(request);
				if( response != null ) {
					System.out.println("Login successed with session id = " + response.get("session").toString());
					response.put( "auth", "yes" );
				}
				// Failed to authenticate.
				else {
					System.out.println("Login fail");
					response = new JSONObject()
						.put( "auth", "no" );
				}
				break;
			// Get all mail list event
			case "get all mail":
				mailList = Database.getAllMail(request);
				if( mailList != null ) {
					response.put( "auth", "yes" )
							.put( "mails", mailList );
				}
				// Failed to get all mail data.
				else {
					response.put( "auth", "no" );
				}
				break;
			// Get all mail list event
			case "get mail":
				response = Database.getMail(request);
				if( response != null ) {
					response.put( "auth", "yes" );
				}
				// Failed to get mail data.
				else {
					response = new JSONObject()
						.put( "auth", "no" );
				}
				break;
			// Send mail event
			case "send mail":
				// Successfully send all mail data.
				if( Database.sendMail(request) ) {
					response.put( "auth", "yes" );
				}
				// Failed to send mail data.
				else {
					response.put( "auth", "no" );
				}
				break;
			// Get all task list event
			case "get all task":
				//------------------------------------------------------------------------
				// TODO: get database user all task data
				//------------------------------------------------------------------------
				// Successfully get all task data.
				if( true ) {
					response = new JSONObject()
						.put( "auth", "yes" )
						.put( "mails", new JSONArray(
							new JSONObject[] {
								new JSONObject()
									.put( "id", "0" )
									.put( "from", "kinoe@lala.mail.com" )
									.put( "to", "kevin@lala.mail.com" )
									.put( "title", "fuck you" ),
								new JSONObject()
									.put( "id", "1" )
									.put( "from", "kevin@lala.mail.com" )
									.put( "to", "kinoe@lala.mail.com" )
									.put( "title", "eat shit" )
							}
						) );
				}
				// Failed to get all task data.
				else {
					response = new JSONObject()
						.put( "auth", "no" );
				}
				break;
			}
			this.out.writeUTF( response.toString() );
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
