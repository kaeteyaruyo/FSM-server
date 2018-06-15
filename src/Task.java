
public class Task extends Mail {
	private Text[] text;
	public Task( String from, String to, String title ) {
		super( from, to, title );
	}
	public Task( String from, String to, String title, Text[] text ) {
		super( from, to, title );
		this.text = text;
	}
	public Text[] getText() {
		return this.text;
	}
}
