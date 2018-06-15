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
						mailHead.getString( "id" ),
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
			return temp.toArray( new MailHead[ temp.size() ] );
		}
				
		// Failed to get all mail.
		return null;
	}
	/****************************************************************
	 * Get mail by id
	 ***************************************************************/
	public Mail getMail( String id ) {
		// Create get mail request data.
		JSONObject request = new JSONObject()
			.put( "event", "get mail")
			.put( "session", this.session )
			.put( "id", id );
		
		// Send get mail request data.
		this.connect().sendText( request.toString() );
		
		// Receive get mail response data.
		JSONObject response = new JSONObject( this.receiveText() );
		
		// Close server connection.
		this.close();
		
		// Successfully get mail response data.
		if( response.getString( "auth" ).equals( "yes" ) ) {
			return new Mail(
					response.getString( "from" ),
					response.getString( "to" ),
					response.getString( "title" ),
					response.getString( "body" )
			);
		}
		
		// Failed to get mail.
		return null;
	}
	/****************************************************************
	 * Send mail
	 ***************************************************************/
	public boolean sendMail( Mail mail ) {
		// Create send mail request data.
		JSONObject request = new JSONObject()
			.put( "event", "send mail")
			.put( "session", this.session )
			.put( "from", mail.getSender() )
			.put( "to", mail.getReceiver() )
			.put( "title", mail.getTitle() )
			.put( "body", mail.getBody() );
		
		// Send send mail request data.
		this.connect().sendText( request.toString() );
		
		// Receive send mail response data.
		JSONObject response = new JSONObject( this.receiveText() );
		
		// Close server connection.
		this.close();
		
		// Successfully send mail response data.
		if( response.getString( "auth" ).equals( "yes" ) ) {
			return true;
		}
				
		// Failed to send mail.
		return false;
	}
	/****************************************************************
	 * Get all task header
	 ***************************************************************/
	public TaskHead[] getAllTask() {
		// Create get all task request data.
		JSONObject request = new JSONObject()
			.put( "event", "get all task")
			.put( "session", this.session );
		
		// Send get all task request data.
		this.connect().sendText( request.toString() );
		
		// Receive get all task response data.
		JSONObject response = new JSONObject( this.receiveText() );
		
		// Close server connection.
		this.close();
		
		// Successfully get all task response data.
		if( response.getString( "auth" ).equals( "yes" ) ) {
			ArrayList<TaskHead> temp = new ArrayList<TaskHead>();
			JSONArray tasks = response.getJSONArray( "tasks" );
			for( int index = 0; index < tasks.length(); ++index ) {
				JSONObject taskHead = tasks.getJSONObject( index );
				temp.add(
					new TaskHead(
						taskHead.getString( "id" ),
						taskHead.getString( "from" ),
						taskHead.getString( "to" ),
						taskHead.getString( "title" )
					)
				);
			}
			// No task available yet.
			if( temp.size() == 0 )
				return null;
			// Return all available task.
			return temp.toArray( new TaskHead[ temp.size() ] );
		}
				
		// Failed to get all task.
		return null;
	}
	/****************************************************************
	 * Get task
	 ***************************************************************/
	public Task getTask( String id ) {
		// Create get task request data.
		JSONObject request = new JSONObject()
			.put( "event", "get task")
			.put( "session", this.session )
			.put( "id", id );
		
		// Send get task request data.
		this.connect().sendText( request.toString() );
		
		// Receive get task response data.
		JSONObject response = new JSONObject( this.receiveText() );
		
		// Close server connection.
		this.close();
		
		// Successfully get task response data.
		if( response.getString( "auth" ).equals( "yes" ) ) {
			ArrayList<Text> tempText = new ArrayList<Text>();
			JSONObject task = response.getJSONObject( "task" );
			JSONArray texts = task.getJSONArray( "text" );
			for( int index = 0; index < texts.length(); ++index ) {
				JSONArray text = texts.getJSONArray( index );
				// Single-text
				if( text.length() == 1 ) {
					tempText.add( new SingleText( text.getString( 0 ) ) );
				}
				// Multi-text
				else {
					ArrayList<String> tempString = new ArrayList<String>();
					for( int index2 = 0; index2 < text.length(); ++index2 ) {
						tempString.add( text.getString( index2 ) );
					}
					tempText.add( new MultiText( tempString.toArray( new String[ tempString.size() ] ) ) );	
				}
			}
			return new Task(
				task.getString( "from" ),
				task.getString( "to" ),
				task.getString( "title" ),
				tempText.toArray( new Text[ tempText.size() ] )
			);
		}
		
		// Failed to get task.
		return null;
	}
	/****************************************************************
	 * Create task
	 ***************************************************************/
	public boolean createTask( Task task ) {
		// Create create task request data.
		Text[] texts = task.getText();
		JSONArray temp = new JSONArray();
		for( int index = 0; index < texts.length; ++index ) {
			if( texts[ index ] instanceof SingleText ) {
				temp.put( new JSONArray().put( ( ( SingleText ) texts[ index ] ).getText() ) );
			}
			else {
				temp.put( new JSONArray( texts[ index ].getAllText() ) );
			}
		}
		JSONObject request = new JSONObject()
			.put( "event", "create task")
			.put( "session", this.session )
			.put( "from", task.getSender() )
			.put( "to", task.getReceiver() )
			.put( "title", task.getTitle() )
			.put( "text", temp );
		
		// Send create task request data.
		this.connect().sendText( request.toString() );
		
		// Receive create task response data.
		JSONObject response = new JSONObject( this.receiveText() );
		
		// Close server connection.
		this.close();
		
		// Successfully create task response data.
		if( response.getString( "auth" ).equals( "yes" ) ) {
			return true;
		}
		// Failed to create task.
		return false;
	}
	public boolean updateTask( Task task ) {
		return true;
	}
	/****************************************************************
	 * Delete task
	 ***************************************************************/
	public boolean deleteTask( String id ) {
		// Create delete task request data.
		JSONObject request = new JSONObject()
			.put( "event", "delete task")
			.put( "session", this.session )
			.put( "id", id );
		
		// Send delete task request data.
		this.connect().sendText( request.toString() );
		
		// Receive delete task response data.
		JSONObject response = new JSONObject( this.receiveText() );
		
		// Close server connection.
		this.close();
		
		// Successfully create task response data.
		if( response.getString( "auth" ).equals( "yes" ) ) {
			return true;
		}
		// Failed to create task.
		return false;
	}
}
