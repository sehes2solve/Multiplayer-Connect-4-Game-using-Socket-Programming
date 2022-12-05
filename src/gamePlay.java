import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class gamePlay {
	
	player Player1 , Player2;
	
	public gamePlay()
	{
		Player1 = new player();
		Player2 = new player();
	}
		
	public class game implements Runnable
	{
		
		public game( Socket socket , char mark ) throws IOException
		{
			if 		( Player1.mark == '?' )
			{
				Player1.setPlayer( socket , mark );
			}
			else if ( Player2.mark == '?' )
			{
				Player2.setPlayer( socket , mark );
			}
		}
		
		public void run() 
		{
			
			boolean P1_turn = true;
			boolean EndGame = false;
			
			if (Player1.mark != '?' && Player2.mark != '?')
			{
				while (true)
				{
					if (P1_turn)
					{

						//System.out.println( "Henaaaa0 " );
						Player1.out.println("p1");
						Player2.out.println("p2");
						
						int index = Player1.in.nextInt();
						Player2.out.println( index ); 						
						
						// >>>>>  player 1 END GAME
						EndGame = Player1.in.nextBoolean();
						/*
						 * if ( end game == true )
						 * {
						 * 		break;
						 * }
						 */
						P1_turn = false;
						
						//System.out.println( "Henaaaa2 " );
						if ( EndGame == true )
						{
							break;
						}
						
					}
					else
					{
						//System.out.println( "Henaaaa1 " );
						Player2.out.println("p1");
						Player1.out.println("p2");

						int index = Player2.in.nextInt();
						Player1.out.println( index );
						
						// >>>>>  player 2 END GAME
						EndGame = Player2.in.nextBoolean();
						
						/*
						 * if ( end game == true )
						 * {
						 * 		break;
						 * }
						 */
						
						if ( EndGame == true )
						{
							break;
						}
						
						P1_turn = true;
					}
				}
				
				Player1.in.close();
				Player1.out.close();
						
				Player2.in.close();
				Player2.out.close();
				
				try {
					Player1.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					Player2.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public class player
	{
		char mark = '?';
		Socket socket = null;
		
		Scanner in = null;
		PrintWriter out = null;
		
		public void setPlayer( Socket socket , char mark ) throws IOException
		{
			this.socket = socket;
			this.mark   = mark;
			
			in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            
            out.println( "Welcome Player " + mark );
		}
	}
}
