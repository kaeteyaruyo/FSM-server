
public class Id {
	private int id;
	/****************************************************************
	 * Constructor
	 ***************************************************************/
	public Id( int id ) {
		this.id = id;
	}
	public Id( String id ) {
		this.id = Integer.parseInt( id );
	}
	/****************************************************************
	 * return id
	 ***************************************************************/
	public String getId() {
		return this.toString();
	}
	
	public String toString() {
		return Integer.toString( this.id );
	}
}
