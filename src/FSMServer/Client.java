package FSMServer;

import java.io.*;
import java.net.*;
import org.json.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

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
		catch ( Exception e ) {
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
		catch ( Exception e ) {
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
		catch ( Exception e ) {
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
		catch ( Exception e ) {
			e.printStackTrace();
		}
		return text;
	}
	/****************************************************************
	 * User registration
	 ***************************************************************/
	public boolean regist( String account, String password ) {
		// Create request for registration JSON data.
		JSONObject request = null;
		try {
			request = new JSONObject()
				.put( "event", "regist")
				.put( "account", account )
				.put( "password", password );
		}
		// Failed to create request for registration JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return false;
		}
		// Send request for registration JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive response for registration JSON data.
		JSONObject response = null;
		try {
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse response for registration JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
			return false;
		}
		
		// Close server connection.
		this.close();
		
		// Successfully register.
		try {
			if( response.getString( "auth" ).equals( "yes" ) ) {
				this.session = response.getString( "session" );
				return true;
			}
		}
		// Failed to parse response for registration JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}

		// Failed to register.
		return false;
	}
	/****************************************************************
	 * User authentication
	 ***************************************************************/
	public boolean authenticate( String account, String password ) {
		// Create request for authentication JSON data.
		JSONObject request = null;
		try {
			request = new JSONObject()
				.put( "event", "authenticate")
				.put( "account", account )
				.put( "password", password );
		}
		// Failed to create request for authentication JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return false;
		}
		
		// Send request for authentication JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive response for authentication JSON data.
		JSONObject response = null;
		try {
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse response for authentication JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
			return false;
		}
		
		// Close server connection.
		this.close();
		
		// Successfully authenticated.
		try {
			if( response.getString( "auth" ).equals( "yes" ) ) {
				this.session = response.getString( "session" );
				return true;
			}
		}
		// Failed to parse response for authentication JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}

		// Failed to authenticate.
		return false;
	}
	/****************************************************************
	 * Get all mail header
	 ***************************************************************/
	public MailHead[] getAllMail() {
		// Create request for getting all mail JSON data.
		JSONObject request = null;
		try {
			request = new JSONObject()
				.put( "event", "get all mail")
				.put( "session", this.session );
		}
		// Failed to create request for getting all mail JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return null;
		}
		
		// Send request for getting all mail JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive response for getting all mail JSON data.
		JSONObject response = null;
		try {
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse response for getting all mail JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
			return null;
		}
		
		// Close server connection.
		this.close();
		
		// Successfully get all mail.
		try {
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
							mailHead.getString( "title" ),
							new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).parse( mailHead.getString( "timeStemp" ) )
						)
					);
				}
				// No mail available yet.
				if( temp.size() == 0 )
					return null;
				// Return all available mail.
				return temp.toArray( new MailHead[ temp.size() ] );
			}
		}
		// Failed to parse response for getting all mail JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}

		// Failed to get all mail.
		return null;
	}
	/****************************************************************
	 * Get mail by id
	 ***************************************************************/
	public Mail getMail( String id ) {
		// Create request for getting mail JSON data.
		JSONObject request = null;
		try {
			request = new JSONObject()
				.put( "event", "get mail")
				.put( "session", this.session )
				.put( "id", id );
		}
		// Failed to create request for getting mail JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return null;
		}
		
		// Send request for getting mail JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive response for getting mail JSON data.
		JSONObject response = null;
		try {
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse response for get mail JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
			return null;
		}
		
		// Close server connection.
		this.close();
		
		// Successfully get mail.
		try {
			if( response.getString( "auth" ).equals( "yes" ) ) {

				return new Mail(
					response.getString( "from" ),
					response.getString( "to" ),
					response.getString( "title" ),
					response.getString( "body" ),
					new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).parse( response.getString( "timeStemp" ) )
				);
			}
		}
		// Failed to parse response for get mail JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}
		
		// Failed to get mail.
		return null;
	}
	/****************************************************************
	 * Send mail
	 ***************************************************************/
	public boolean sendMail( Mail mail ) {
		// Create request for sending mail JSON data.
		JSONObject request = null;
		try {
			request = new JSONObject()
				.put( "event", "send mail")
				.put( "session", this.session )
				.put( "from", mail.getSender() )
				.put( "to", mail.getReceiver() )
				.put( "title", mail.getTitle() )
				.put( "body", mail.getBody() )
				.put( "timeStemp", new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).format( mail.getTimeStemp() ) );
		}
		// Failed to create request for sending mail JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return false;
		}
		
		// Send request for sending mail JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive response for sending mail JSON data.
		JSONObject response = null;
		try {
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse response for sending mail JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
			return false;
		}
		
		// Close server connection.
		this.close();
		
		// Successfully send mail.
		try {
			if( response.getString( "auth" ).equals( "yes" ) ) {
				return true;
			}
		}
		// Failed to parse response for sending mail JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}
				
		// Failed to send mail.
		return false;
	}
	/****************************************************************
	 * Get all task header
	 ***************************************************************/
	public TaskHead[] getAllTask() {
		// Create request for getting all task JSON data.
		JSONObject request = null;
		try {
			request = new JSONObject()
				.put( "event", "get all task" )
				.put( "session", this.session );
		}
		// Failed to create request for getting all task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return null;
		}
		
		// Send request for getting all task JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive response for getting all task JSON data.
		JSONObject response = null;
		try {
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse response for getting all task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
			return null;
		}
		
		// Close server connection.
		this.close();
		
		// Successfully get all task.
		try {
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
							taskHead.getString( "title" ),
							new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).parse( taskHead.getString( "createDate" ) )
						)
					);
				}
				// No task available yet.
				if( temp.size() == 0 )
					return null;
				// Return all available task.
				return temp.toArray( new TaskHead[ temp.size() ] );
			}
		}
		// Failed to parse response for getting all task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}
				
		// Failed to get all task.
		return null;
	}
	/****************************************************************
	 * Get task by id
	 ***************************************************************/
	public Task getTask( String id ) {
		// Create request for getting task JSON data.
		JSONObject request = null;
		try {
			request = new JSONObject()
				.put( "event", "get task")
				.put( "session", this.session )
				.put( "id", id );
		}
		// Failed to create request for getting task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return null;
		}
		
		// Send request for getting task JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive response for getting task JSON data.
		JSONObject response = null;
		try {
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse response for getting task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
			return null;
		}
		
		// Close server connection.
		this.close();
		
		// Successfully get task.
		try {
			if( response.getString( "auth" ).equals( "yes" ) ) {
				ArrayList<Text> tempText = new ArrayList<Text>();
				JSONArray texts = response.getJSONArray( "text" );
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
					response.getString( "from" ),
					response.getString( "to" ),
					response.getString( "title" ),
					tempText.toArray( new Text[ tempText.size() ] ),
					new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).parse( response.getString( "sendDate" ) ),
					response.getInt( "interval" )
				);
			}
		}
		// Failed to parse response for getting task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}
		
		// Failed to get task.
		return null;
	}
	/****************************************************************
	 * Create task
	 ***************************************************************/
	public boolean createTask( Task task ) {
		// Create request for creating task JSON data.
		Text[] texts = task.getText();
		JSONArray temp = new JSONArray();
		JSONObject request = null;
		try {
			for( int index = 0; index < texts.length; ++index ) {
				if( texts[ index ] instanceof SingleText ) {
					temp.put( new JSONArray().put( ( ( SingleText ) texts[ index ] ).getText() ) );
				}
				else {
					temp.put( new JSONArray( texts[ index ].getAllText() ) );
				}
			}
			request = new JSONObject()
				.put( "event", "create task")
				.put( "session", this.session )
				.put( "from", task.getSender() )
				.put( "to", task.getReceiver() )
				.put( "title", task.getTitle() )
				.put( "text", temp )
				.put( "createDate", new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).format( new Date() ) )
				.put( "sendDate", new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).format( task.getSendDate() ) )
				.put( "interval", task.getInterval() );
		}
		// Failed to create request for creating task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return false;
		}
		
		// Send request for creating task JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive response for creating task JSON data.
		JSONObject response = null;
		try { 
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse response for creating task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}
		
		// Close server connection.
		this.close();
		
		// Successfully create task.
		try {
			if( response.getString( "auth" ).equals( "yes" ) ) {
				return true;
			}
		}
		// Failed to parse response for creating task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}
		
		// Failed to create task.
		return false;
	}
	/****************************************************************
	 * Update task
	 ***************************************************************/
	public boolean updateTask( String id, Task task ) {
		// Create request for updating task JSON data.
		Text[] texts = task.getText();
		JSONArray temp = new JSONArray();
		JSONObject request = null;
		try {
			for( int index = 0; index < texts.length; ++index ) {
				if( texts[ index ] instanceof SingleText ) {
					temp.put( new JSONArray().put( ( ( SingleText ) texts[ index ] ).getText() ) );
				}
				else {
					temp.put( new JSONArray( texts[ index ].getAllText() ) );
				}
			}
			request = new JSONObject()
				.put( "event", "update task")
				.put( "session", this.session )
				.put( "id", id )
				.put( "from", task.getSender() )
				.put( "to", task.getReceiver() )
				.put( "title", task.getTitle() )
				.put( "text", temp )
				.put( "createDate", new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).format( new Date() ) )
				.put( "sendDate", new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).format( task.getSendDate() ) )
				.put( "interval", task.getInterval() );
		}
		// Failed to create request for updating task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return false;
		}
		
		// Send request for updating task JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive response for updating task JSON data.
		JSONObject response = null;
		try { 
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse response for updating task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
			return false;
		}
		
		// Close server connection.
		this.close();
		
		// Successfully update task.
		try {
			if( response.getString( "auth" ).equals( "yes" ) ) {
				return true;
			}
		}
		// Failed to parse response for updating task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}
		
		// Failed to update task.
		return false;
	}
	/****************************************************************
	 * Delete task
	 ***************************************************************/
	public boolean deleteTask( String id ) {
		// Create request for deleting task JSON data.
		JSONObject request = null;
		try {
			request = new JSONObject()
			.put( "event", "delete task")
			.put( "session", this.session )
			.put( "id", id );
		}
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return false;
		}
			
		// Send request for deleting task JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive response for deleting task JSON data.
		JSONObject response = null;
		try {
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse response for deleting task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
			return false;
		}
		
		// Close server connection.
		this.close();
		
		// Successfully create task.
		try {
			if( response.getString( "auth" ).equals( "yes" ) ) {
				return true;
			}
		}
		// Failed to parse response for deleting task JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}
		
		// Failed to create task.
		return false;
	}
	/****************************************************************
	 * logout
	 ***************************************************************/
	public boolean logout() {
		// Create request for logout JSON data.
		JSONObject request = null;
		try {
			request = new JSONObject()
				.put( "event", "logout")
				.put( "session", this.session );
		}
		// Failed to create request for logout JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from client." );
			e.printStackTrace();
			return false;
		}
		
		// Send request for logout JSON data.
		this.connect().sendText( request.toString() );
		
		// Receive request for logout JSON data.
		JSONObject response = null;
		try {
			response = new JSONObject( this.receiveText() );
		}
		// Failed to parse request for logout JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
			return false;
		}
		
		// Close server connection.
		this.close();
		
		// Successfully logout.
		try {
			if( response.getString( "auth" ).equals( "yes" ) ) {
				this.session = null;
				return true;
			}
		}
		// Failed to parse request for logout JSON data.
		catch ( Exception e ) {
			System.out.println( "Invalid JSONObject send from server." );
			e.printStackTrace();
		}
		
		// Failed to logout.
		return false;
	}
}
