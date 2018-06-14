
public class Mail {
	private String from;
	private String to;
	private String title;
	private String body;
	public Mail( String from, String to, String title ) {	
		this.from = from;
		this.to = to;
		this.title = title;
	}
	public Mail( String from, String to, String title, String body ) {	
		this( from, to, title );
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
}
