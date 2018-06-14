import java.io.*;
import java.net.*;
import org.json.*;
import java.util.ArrayList;

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
	/****************************************************************
	 * Get all mail header
	 ***************************************************************/
	public MailHead[] getAllMail() {
		// Create get all mail request data.
		JSONObject request = new JSONObject()
			.put( "event", "get all mail")
			.put( "session", this.session );
		
		// Send get all mail request data.
		this.connect().sendText( request.toString() );
		
		// Receive get all mail response data.
		JSONObject response = new JSONObject( this.receiveText() );
		
		// Close server connection.
		this.close();
		
		// Successfully get all mail response data.
		if( response.getString( "auth" ).equals( "yes" ) ) {
			ArrayList<MailHead> temp = new ArrayList<MailHead>();
			JSONArray mails = response.getJSONArray( "mails" );
			for( int index = 0; index < mails.length(); ++index ) {
				JSONObject mailHead = mails.getJSONObject( index );
				temp.add(
					new MailHead(
						new Id( mailHead.getInt( "id" ) ),
						mailHead.getString( "from" ),
						mailHead.getString( "to" ),
						mailHead.getString( "title" )
					)
				);
			}
			// No mail available yet.
			if( temp.size() == 0 )
				return null;
			// Return all available mail.
			return ( MailHead[] ) temp.toArray(new MailHead[ temp.size() ]);
		}
				
		// Failed to authenticate.
		return null;
	}
	public Mail getMail(Id id) {
		return new Mail(id,"from","to","title","body");
	}
	public boolean sendMail(Mail mail) {
		return true;
	}
	public TaskHead[] getAllTask() {
		return new TaskHead[1];
	}
	public Task getTask(Id id) {
		return new Task(id, "from", "to", "title", new Text[1]);
	}
	public boolean createTask(Task task) {
		return true;
	}
	public boolean updateTask(Task task) {
		return true;
	}
	public boolean deleteTask(Task task) {
		return true;
	}
}
