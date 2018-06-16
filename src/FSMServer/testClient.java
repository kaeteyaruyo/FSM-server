package FSMServer;

import java.util.ArrayList;
import java.util.Date;

public class testClient {
	public static void main(String arg[]) {
		Client c = new Client( "localhost", 3000 );
		//c.regist("kevin","lala");
		c.authenticate("kinoe","0930");
		//c.sendMail(new Mail("kinoe@mail.FSM.com", "kevin@mail.FSM.com", "=3=", ":D", new Date()));

		MailHead[] mailHeads = c.getAllMail();
		if(mailHeads != null) {
			for(int i = 0; i < mailHeads.length; ++i) {
				System.out.println( "id: " + mailHeads[i].getId() );
				System.out.println( "from: " + mailHeads[i].getSender() );
				System.out.println( "to: " + mailHeads[i].getReceiver() );
				System.out.println( "title: " + mailHeads[i].getTitle() );
				System.out.println( "timeStemp: " + mailHeads[i].getTimeStemp() );			
			}
		}
		else
			System.out.println("mailHead is null");

		if(mailHeads != null) {
			Mail mail = c.getMail( mailHeads[0].getId() );
			System.out.println( "from: " + mail.getSender() );
			System.out.println( "to: " + mail.getReceiver() );
			System.out.println( "title: " + mail.getTitle() );
			System.out.println( "body: " + mail.getBody() );
			System.out.println( "timeStemp: " + mail.getTimeStemp() );
		}
		else
			System.out.println("mail is null");

		c.createTask( new Task("kinoe@bla.com", "kevin@bla.com", "Late at work", new Text[] { new SingleText("Sorry, "), new MultiText(new String[] {"Someone fuck up the server.", "working on routing.", "ZZZzzzz."}) }, new Date(), 0) );

		TaskHead[] taskHeads = c.getAllTask();
		for(int i = 0; i < taskHeads.length; ++i) {
			System.out.println( "id: " + taskHeads[i].getId() );
			System.out.println( "from: " + taskHeads[i].getSender() );
			System.out.println( "to: " + taskHeads[i].getReceiver() );
			System.out.println( "title: " + taskHeads[i].getTitle() );
			System.out.println( "create Date: " + taskHeads[i].getCreateDate() );
		}
		
		Task task = c.getTask( taskHeads[0].getId() );
		System.out.println( "from: " + task.getSender() );
		System.out.println( "to: " + task.getReceiver() );
		System.out.println( "title: " + task.getTitle() );
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
		System.out.println( "send Date: " + task.getSendDate() );
		c.updateTast( );
		c.deleteTask( taskHeads[0].getId() );

		c.logout();
	}	
}
