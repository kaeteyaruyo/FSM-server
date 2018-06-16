package FSMServer;
import java.util.Date;

public class Mail {
	private String from;
	private String to;
	private String title;
	private String body;
	private Date sendDate;
	public Mail( String from, String to, String title, Date sendDate ) {	
		this.from = from;
		this.to = to;
		this.title = title;
		this.sendDate = sendDate;
	}
	public Mail( String from, String to, String title, String body, Date sendDate ) {	
		this( from, to, title, sendDate );
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
	public Date getSendDate() {
		return this.sendDate;
	}
}
