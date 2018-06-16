package FSMServer;

import java.util.Date;

public class MailHead extends Head {	
	private Date timeStemp;
	public MailHead( String id, String from, String to, String title, Date timeStemp ) {
		super( id, from, to, title );
		this.timeStemp = timeStemp;
	}
	public Date getTimeStemp() {
		return this.timeStemp;
	}
}
