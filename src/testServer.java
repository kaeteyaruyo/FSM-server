
public class testServer {
	public static void main(String arg[]) {
		Server s = new Server( 3000 );
		s.start().listen();
	}
}
