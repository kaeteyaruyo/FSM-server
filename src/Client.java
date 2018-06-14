import java.io.*;
import java.net.*;
import org.json.*;

public class Client {
	private int port;
	private String serverIP;
	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;
	private String session;
	/****************************************************************
	 * Constructor
	 ***************************************************************/
	public Client( String serverIP, int port ) {
		this.serverIP = serverIP;
		this.port = port;
	}
	/****************************************************************
	 * Build connection
	 ***************************************************************/
	private Client connect() {
		try {
			System.out.println(
				"Client is request to server " +
				this.serverIP +
				":" +
				this.port +
				", waiting response..."
			);
			this.clientSocket = new Socket( this.serverIP, this.port );
			this.in = new DataInputStream( this.clientSocket.getInputStream() );
			this.out = new DataOutputStream( this.clientSocket.getOutputStream() );
			System.out.println(
				"Successfully connect to server" +
				this.serverIP +
				":" +
				this.port
			);
		}
		catch ( IOException e ) {
			System.out.println( "Failed to connect." );
			e.printStackTrace();
			this.close();
		}
		return this;
	}
	/****************************************************************
	 * Close connection
	 ***************************************************************/
	private Client close() {
		try {
			this.in.close();
			this.out.close();
			this.clientSocket.close();
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		finally {
			System.out.println(
				"Close connection with server " +
				this.serverIP +
				":" +
				this.port
			);
		}
		return this;
	}
	/****************************************************************
	 * Send text to server
	 ***************************************************************/
	private Client sendText( String text ) {
		try {
			this.out.writeUTF( text );
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		return this;
	}
	/****************************************************************
	 * Get text to server
	 ***************************************************************/
	private String receiveText() {
		String text = "";
		try {
			text = this.in.readUTF();
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		return text;
	}
	/****************************************************************
	 * User registration
	 ***************************************************************/
	public boolean regist( String account, String password ) {
		// Create registration request data.
		JSONObject request = new JSONObject()
			.put( "event", "regist")
			.put( "account", account )
			.put( "password", password );
		
		// Send registration request data.
		this.connect().sendText( request.toString() );
		
		// Receive registration response data.
		JSONObject response = new JSONObject( this.receiveText() );
		
		// Close server connection.
		this.close();
		
		// Successfully register.
		if( response.getString( "auth" ).equals( "yes" ) ) {
			this.session = response.getString( "session" );
			return true;
		}
		
		// Failed to register.
		return false;
	}
	/****************************************************************
	 * User authentication
	 ***************************************************************/
	public boolean authenticate( String account, String password ) {
		// Create authentication request data.
		JSONObject request = new JSONObject()
			.put( "event", "authenticate")
			.put( "account", account )
			.put( "password", password );
		
		// Send authentication request data.
		this.connect().sendText( request.toString() );
		
		// Receive authentication response data.
		JSONObject response = new JSONObject( this.receiveText() );
		
		// Close server connection.
		this.close();
		
		// Successfully authenticated.
		if( response.getString( "auth" ).equals( "yes" ) ) {
			this.session = response.getString( "session" );
			return true;
		}
		
		// Failed to authenticate.
		return false;
	}
}
