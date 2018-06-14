import java.util.ArrayList;

public class testClient {
	public static void main(String arg[]) {
		Client c = new Client( "localhost", 3000 );
		c.regist("abc","123");
		c.authenticate("abc","123");
		MailHead[] mailHeads = c.getAllMail();
		for(int i=0;i<mailHeads.length;++i) {
			System.out.println( "id: " + mailHeads[i].getId() );
			System.out.println( "from: " + mailHeads[i].getSender() );
			System.out.println( "to: " + mailHeads[i].getReceiver() );
			System.out.println( "title: " + mailHeads[i].getTitle() );
		}
		Mail mail = c.getMail( "abcdefg" );
		System.out.println( "from: " + mail.getSender() );
		System.out.println( "to: " + mail.getReceiver() );
		System.out.println( "title: " + mail.getTitle() );
		System.out.println( "body: " + mail.getBody() );
		c.sendMail(mail);
	}	
}
