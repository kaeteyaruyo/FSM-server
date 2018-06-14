import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.*;
import org.json.*;
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

	public static String regist(JSONObject user) {
		try{   
			MongoCollection<Document> users = database.getCollection("user");
	        MongoCursor<Document> results = users.find().iterator();
	        
	        while(results.hasNext()){
	        	if(results.next().get("password").equals(user.get("password"))) {
	        		System.out.println("User name has existed.");
	        		return "";
	        	}
	        }

	        Document newUser = new Document()
	        		.append("account", user.get("account"))
	        		.append("password", user.get("password"));
	        users.insertOne(newUser);
			return authenticate(new JSONObject().put("account", user.get("account")).put("password", user.get("password")));
		} catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		return "";
	}
	
	public static String authenticate(JSONObject user) {
		try{
			MongoCollection<Document> users = database.getCollection("user");
	        MongoCursor<Document> results = users.find(eq("account", user.get("account"))).iterator();
	        
	        while(results.hasNext()){
	        	Document d = results.next();
	        	if(d.get("password").equals(user.get("password")))
	        		return d.get("_id").toString();
	        }
	        System.out.println("User name doesn't match.");
			return "";
		} catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		return "";
	}
}
