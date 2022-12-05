import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
	
	private static ServerSocket server = null;
	private static int port = 59090;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		server = new ServerSocket( port );
		System.out.println( "Connect-4 server ....\n" );
		Executor pool = Executors.newFixedThreadPool(20);
		while ( true )
		{
			gamePlay round = new gamePlay();
			pool.execute( round.new game( server.accept() , '1') );
			pool.execute( round.new game( server.accept() , '2') );
		}
		
	}

}
