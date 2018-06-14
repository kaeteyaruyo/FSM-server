
public class SingleText implements Text {
	String text;
	public SingleText( String text ) {
		this.text = text;
	}
	public String[] getAllText() {
		String[] arr = new String[] { this.text };
		return arr;
	}
	public String getText( int index ) {
		return this.text;
	}
	public String getText() {
		return this.text;
	}
}
