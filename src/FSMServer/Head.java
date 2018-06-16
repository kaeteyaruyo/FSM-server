package FSMServer;

import java.util.Date;

public abstract class Head {
	private String id;
	private String from;
	private String to;
	private String title;	
	private Date date;
	
	public Head( String id, String from, String to, String title, Date date ) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.title = title;
		this.date = date;
	}
	
	public String getId() {
		return this.id;
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
	
	public Date getDate() {
		return this.date;
	}
}
