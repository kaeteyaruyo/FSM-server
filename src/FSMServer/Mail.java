package FSMServer;
import java.util.Date;
import java.time.*;

public class Mail {
	private String from;
	private String to;
	private String title;
	private String body;
	private Date date;
	public Mail( String from, String to, String title ) {	
		this.from = from;
		this.to = to;
		this.title = title;
		this.date = new Date();
	}
	public Mail( String from, String to, String title, Date timeStemp ) {	
		this.from = from;
		this.to = to;
		this.title = title;
		this.date = timeStemp;
	}
	public Mail( String from, String to, String title, String body, Date timeStemp ) {	
		this( from, to, title, timeStemp );
		this.body = body;
	}
	public String getSender() {
		return this.from;
	}
	public String getReceiver() {
		return this.to;
	}
	public String getTitle() {
		return this.title;
	}
	public String getBody() {
		return this.body;
	}
	public Date getTimeStemp() {
		return this.date;
	}
}
