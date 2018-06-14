
public class MailHead {
	private Id id;
	private String from;
	private String to;
	private String title;
	
	public MailHead( Id id, String from, String to, String title ) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.title = title;
	}
	
	public String getId() {
		return this.id.getId(); 
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
}
