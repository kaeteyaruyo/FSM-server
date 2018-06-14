
public class testClient {
	public static void main(String arg[]) {
		Client c = new Client( "localhost", 3000 );
		//c.regist("abc","123");
		c.authenticate("abc","123");
	}	
}
