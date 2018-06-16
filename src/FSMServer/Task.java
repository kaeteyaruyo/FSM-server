package FSMServer;

import java.util.Date;

public class Task extends Mail {
	private Text[] text;
	private int interval;
	public Task( String from, String to, String title, Text[] text, Date sendDate, int interval ) {
		super( from, to, title, sendDate );
		this.interval = interval;
		this.text = text;
	}
	public Text[] getText() {
		return this.text;
	}
	public Date getSendDate() {
		return super.getTimeStemp();
	}
	public int getInterval() {
		return this.interval;
	}
}
