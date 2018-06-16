package FSMServer;

import java.util.Date;

public class Task extends Mail {
	private Text[] text;
	private Date endDate;
	private int seconds;
	public Task( String from, String to, String title, Date startDate, Date endDate ) {
		super( from, to, title, startDate );
		this.endDate = endDate;
	}
	public Task( String from, String to, String title, Date startDate, int seconds ) {
		super( from, to, title, startDate );
		this.seconds = seconds;
	}
	public Task( String from, String to, String title, Text[] text, Date startDate, Date endDate ) {
		super( from, to, title, startDate );
		this.text = text;
		this.endDate = endDate;
	}
	public Task( String from, String to, String title, Text[] text, Date startDate, int seconds ) {
		super( from, to, title, startDate );
		this.text = text;
		this.seconds = seconds;
	}
	public Text[] getText() {
		return this.text;
	}
}
