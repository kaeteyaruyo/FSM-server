package FSMServer;

import java.util.ArrayList;
import java.util.Date;

public class testClient {
	public static void main(String arg[]) {
		Client c = new Client( "localhost", 3000 );
		//c.regist("kevin","lala");
		c.authenticate("kevin","lala");
		c.sendMail(new Mail("kevin@mail.FSM.com", "kinoe@mail.FSM.com", "Fuck U", "Gan你老木的老木~~~", new Date()));

		MailHead[] mailHeads = c.getAllMail();
		for(int i = 0; i < mailHeads.length; ++i) {
			System.out.println( "id: " + mailHeads[i].getId() );
			System.out.println( "from: " + mailHeads[i].getSender() );
			System.out.println( "to: " + mailHeads[i].getReceiver() );
			System.out.println( "title: " + mailHeads[i].getTitle() );
			System.out.println( "timeStemp: " + mailHeads[i].getTimeStemp() );			
		}
		
		Mail mail = c.getMail( mailHeads[0].getId() );
		System.out.println( "from: " + mail.getSender() );
		System.out.println( "to: " + mail.getReceiver() );
		System.out.println( "title: " + mail.getTitle() );
		System.out.println( "body: " + mail.getBody() );
		System.out.println( "timeStemp: " + mail.getTimeStemp() );

		TaskHead[] taskHeads = c.getAllTask();
		for(int i = 0; i < taskHeads.length; ++i) {
			System.out.println( "id: " + taskHeads[i].getId() );
			System.out.println( "from: " + taskHeads[i].getSender() );
			System.out.println( "to: " + taskHeads[i].getReceiver() );
			System.out.println( "title: " + taskHeads[i].getTitle() );
		}
/*
		c.createTask( new Task("kevin@bla.com", "kinoe@bla.com", "Late at work", new Text[] { new SingleText("Sorry, "), new MultiText(new String[] {"Someone fuck up the server.", "working on routing.", "ZZZzzzz."}) }, new Date(), new Date()) );
		
		Task task = c.getTask( taskHeads[0].getId() );
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
		c.deleteTask( taskHeads[0].getId() );
*/
		c.logout();
	}	
}
