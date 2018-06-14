import java.util.ArrayList;

public class testClient {
	public static void main(String arg[]) {
		Client c = new Client( "localhost", 3000 );
		c.regist("abc","123");
		c.authenticate("abc","123");
		MailHead[] m = c.getAllMail();
		for(int i=0;i<m.length;++i) {
			System.out.println( "id: " + m[i].getId() );
			System.out.println( "from: " + m[i].getSender() );
			System.out.println( "to: " + m[i].getReceiver() );
			System.out.println( "title: " + m[i].getTitle() );
		}
	}	
}
