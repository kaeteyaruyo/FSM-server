
public class Task extends TaskHead {
	private Text[] text;
	public Task(Id id, String to, String title ) {
		super( id, to, title );
	}
	public Task(Id id, String to, String title, Text[] text ) {
		super( id, to, title );
		this.text = text;
	}
}
