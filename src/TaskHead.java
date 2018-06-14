
public class TaskHead {
	private Id id;
	private String to;
	private String title;
	
	public TaskHead( Id id, String to, String title ) {
		this.id = id;
		this.to = to;
		this.title = title;
	}
	
	public String getId() {
		return this.id.getId(); 
	}
	
	public String getReceiver() {
		return this.to;
	}
	
	public String getTitle() {
		return this.title;
	}
}
