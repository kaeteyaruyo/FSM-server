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
		TaskHead[] taskHeads = c.getAllTask();
		for(int i=0;i<taskHeads.length;++i) {
			System.out.println( "id: " + taskHeads[i].getId() );
			System.out.println( "from: " + taskHeads[i].getSender() );
			System.out.println( "to: " + taskHeads[i].getReceiver() );
			System.out.println( "title: " + taskHeads[i].getTitle() );
		}
		Task task = c.getTask( "0" );
		Text[] text = task.getText();
		for(int i=0;i<text.length;++i) {
			if( text[i] instanceof SingleText ) {
				System.out.println( ( ( SingleText )text[i] ).getText() );
			}
			else {
				for(int j=0;j<text[i].getAllText().length;++j) {
					System.out.println( ( ( MultiText )text[i] ).getAllText()[j] );
				}
			}
		}
		c.createTask( task );
		c.deleteTask( "id" );
		c.logout();
	}	
}
