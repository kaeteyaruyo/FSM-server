import org.json.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.*;

public class Database{
	private static MongoDatabase database = null;
	
	static {
		try{   
			MongoClient connection = new MongoClient(Config.uri());
			database = connection.getDatabase("fire_mail_server");
			System.out.println("Connect to database successfully");
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}		
	}
	
	public Database(){
	}

	private static String checkLogin(String session) {
		try{
			MongoCollection<Document> users = database.getCollection("user");
	        MongoCursor<Document> results = users.find(eq("session", session)).iterator();
	        
	        while(results.hasNext()){
	        	return results.next().get("account").toString();
	        }
	        System.out.println("User not login.");
			return "";
		} catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		return "";
	}
	
	public static JSONObject regist(JSONObject user) {
		try{   
			MongoCollection<Document> users = database.getCollection("user");
	        MongoCursor<Document> results = users.find().iterator();
	        
	        while(results.hasNext()){
	        	if(results.next().get("password").equals(user.get("password"))) {
	        		System.out.println("User name has existed.");
	        		return null;
	        	}
	        }

	        Document newUser = new Document()
	        		.append("account", user.get("account"))
	        		.append("password", user.get("password"))
	        		.append("session", "");
	        users.insertOne(newUser);
			return authenticate(new JSONObject().put("account", user.get("account")).put("password", user.get("password")));
		} catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		return null;
	}
	
	public static JSONObject authenticate(JSONObject user) {
		try{
			MongoCollection<Document> users = database.getCollection("user");
	        MongoCursor<Document> results = users.find(eq("account", user.get("account"))).iterator();
	        
	        while(results.hasNext()){
	        	Document d = results.next();
	        	if(d.get("password").equals(user.get("password"))) {
	        		users.updateOne(eq("account", user.get("account")), new Document("$set", new Document("session", d.get("_id").toString())));
	        		return new JSONObject().put("session", d.get("_id").toString());
	        	}
	        }
	        System.out.println("User name doesn't match.");
			return null;
		} catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		return null;
	}
	
	public static JSONArray getAllMail(JSONObject session) {
		try{
			String account = checkLogin(session.get("session").toString());
			if(!account.equals("")) {
				MongoCollection<Document> mails = database.getCollection("mail");				
		        MongoCursor<Document> results = mails.find(eq("to", account + "@bla.com")).iterator();
		        JSONArray mailList = new JSONArray();
		        while(results.hasNext()){
		        	Document d = results.next();
		        	mailList.put(new JSONObject()
		        					 .put("id", d.get("_id").toString())
		        					 .put("from", d.get("from"))
		        					 .put("to", d.get("to"))
		        					 .put("title", d.get("title")));
		        }
				return mailList;
			}
		} catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}		
		return null;
	}

	public static JSONObject getMail(JSONObject session) {
		try{
			String account = checkLogin(session.get("session").toString());
			if(!account.equals("")) {
				MongoCollection<Document> mails = database.getCollection("mail");	
		        MongoCursor<Document> results = mails.find(eq("_id", new ObjectId(session.get("id").toString()))).iterator();
		        JSONObject mail = null;
		        if(results.hasNext()){
		        	Document d = results.next();
		        	mail = new JSONObject().put("from", d.get("from"))
		        		.put("to", d.get("to"))
		        		.put("title", d.get("title"))
		        		.put("body", d.get("body"));
		        }
				return mail;
			}
		} catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}		
		return null;
	}
	
	public static boolean sendMail(JSONObject session) {
		try{
			String account = checkLogin(session.get("session").toString());
			if(!account.equals("")) {				
				MongoCollection<Document> mails = database.getCollection("mail");
		        Document newMail = new Document()
		        		.append("from", session.get("from"))
		        		.append("to", session.get("to"))
		        		.append("title", session.get("title"))
		        		.append("body", session.get("body"));
		        mails.insertOne(newMail);
		        
				return true;
			}
		} catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}		
		return false;
	}
}
