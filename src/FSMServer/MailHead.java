package FSMServer;

import java.util.Date;

public class MailHead extends Head {	
	private Date timeStemp;
	public MailHead( String id, String from, String to, String title ) {
		super( id, from, to, title );
	}
}
