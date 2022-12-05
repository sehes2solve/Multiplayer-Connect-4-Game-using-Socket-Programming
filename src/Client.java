import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Client {
	
	private static int port = 59090;
	private static Socket socket;
    private static Scanner in;
    private static PrintWriter out;
    static int[][] board;
    
    private static final LayoutManager GridLayout = null;
    private static int PanelWidth = 595, PanelHight = 623;
    private static int Square  = 85;
	private static JButton grids[][];
	private static JButton control[];
	private static JFrame gameFrame;
    
	private static Color player1Color = Color.blue;
	private static Color player2Color = Color.red;
	
	private static boolean EndGame = false;
	private static int winner = -1;
	
	//private static int[] lastMove;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//
		gameFrame = new JFrame( "Connect-4 Game" );
		
		// memset the board
		board = new int[6][7];
		for ( int i = 0; i < 6; i++ )
		{
			for ( int j = 0; j < 7; j++ )
			{
				board[i][j] = 0;
			}
		}
		
		NetworkSetup();
		paintGUI();
		startGame();
		
		// play again OR close window
	}
	
	public static void NetworkSetup() throws IOException 
	{
		InetAddress host = InetAddress.getLocalHost();
        socket = new Socket( host.getHostName() , port ); 
        
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        
        String connected = in.nextLine();
        System.out.println( connected );
	}
	
	public static void paintGUI()
	{
		gameFrame.setVisible(true);
		gameFrame.setSize( PanelWidth , PanelHight ); 
		gameFrame.setResizable(false);
		gameFrame.setLayout( GridLayout );
		gameFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		//Color color1 = new Color(250, 146, 102); // bate5y color
		Color color2 = new Color(66 , 21 , 179); // Blue color
		//Color color3 = new Color(86 , 209, 240); // cyan color
		
		grids = new JButton[6][7];
		for ( int i = 0; i < 6; i++ )
		{
			for ( int j = 0; j < 7; j++ )
			{
				grids[i][j] = new JButton("");
				grids[i][j].setFont( new Font( "Arial" , Font.PLAIN  , 65 ) );
				grids[i][j].setBounds( j*Square , i*Square + Square , Square , Square );
				grids[i][j].setContentAreaFilled(false);
				
				grids[i][j].setFocusable(false);
				gameFrame.add( grids[i][j] );
			}
		}
		
		control = new JButton[7];
		for ( int i = 0; i < 7; i++ )
		{
			control[i] = new JButton("");
			control[i].setBounds( i*Square , 0 , Square , Square );
			control[i].setText("▼");
			control[i].setFont( new Font( "Arial" , Font.PLAIN  , 35 ) );
			control[i].setForeground( color2 );
			gameFrame.add( control[i] );
			
			int[] index = new int[1]; 
			index [0] = i;
			
			control[i].addActionListener( new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
					for ( int row = 5; row >= 0; row -= 1 )
					{
						if ( board[ row ][ index[0] ] == 0 )
						{
							//TimeUnit.MILLISECONDS.sleep(arg0);
							grids[ row ][ index[0] ].setText("O");
							grids[ row ][ index[0] ].setForeground( player1Color );
							board[ row ][ index[0] ] = 1;		
							
							// lock if full
							
							for ( int i = 0; i < 7; i++ )
							{
								control[i].setEnabled( false );
							}
							
							out.println( index[0] );
							
							CheckWinner();
							
							// send END GAME attribute
							
							out.println( EndGame );
							
							break;
						}
					}
					
					if ( board[0][ index[0] ] != 0 )
					{
						control[ index[0] ].setText( "▬" );
						control[ index[0] ].setEnabled( false );
					}
						
				}
			} );
		}
		
		gameFrame.repaint();
	}
	
	public static void CheckWinner()
	{
		// horizontal check
		for ( int i = 0; i < 6; i += 1 )
		{
			for ( int j = 0; j < 4; j += 1 )
			{
				int counter = 0;
				for ( int k = 0; k < 4; k += 1 )
				{
					if ( board[ i ][ j ] == board[ i ][ j+k ] )
					{
						counter += 1;
					}
				}
				
				if ( counter == 4 )
				{
					if 		( board[ i ][ j ] == 1 )
					{
						// player 1 wins , player 2 loses
						EndGame = true;
						winner = 1;
					}
					else if ( board[ i ][ j ] == 2 )
					{
						// player 2 wins , player 1 loses
						EndGame = true;
						winner = 2;
					}
				}
			}
		}
	
		
		// vertical check
		
		for ( int i = 0; i < 7; i += 1 )
		{
			for ( int j = 0; j < 3; j += 1 )
			{
				int counter = 0;
				for ( int k = 0; k < 4; k += 1 )
				{
					if ( board[ j ][ i ] == board[ j + k ][ i ] )
					{
						counter += 1;
					}
				}
				
				if ( counter == 4 )
				{
					if 		( board[ j ][ i ] == 1 )
					{
						// player 1 wins , player 2 loses
						EndGame = true;
						winner = 1;
					}
					else if ( board[ j ][ i ] == 2 )
					{
						// player 2 wins , player 1 loses
						EndGame = true;
						winner = 2;
					}
				}
			}
		}		
		// main diagonal check 
		
		for ( int i = 0; i < 3; i += 1 )
		{
			for ( int j = 0; j < 4; j += 1 )
			{
				int counter = 0;
				for ( int k = 0; k < 4; k += 1 )
				{
					if ( board[ i ][ j ] == board[ i+k ][ j+k ] )
					{
						counter += 1;
					}
				}
				
				if ( counter == 4 )
				{
					if 		( board[ i ][ j ] == 1 )
					{
						// player 1 wins , player 2 loses
						EndGame = true;
						winner = 1;
					}
					else if ( board[ i ][ j ] == 2 )
					{
						// player 2 wins , player 1 loses
						EndGame = true;
						winner = 2;
					}
				}
			}
		}
		
		// reverse diagonal check
		for ( int i = 3;  i < 6; i += 1 )
		{
			for ( int j = 0; j < 4; j += 1 )
			{
				int counter = 0;
				for ( int k = 0; k < 4; k += 1 )
				{
					if ( board[ i ][ j ] == board[ i-k ][ j+k ] )
					{
						counter += 1;
					}
				}
				
				if ( counter == 4 )
				{
					if 		( board[ i ][ j ] == 1 )
					{
						// player 1 wins , player 2 loses
						EndGame = true;
						winner = 1;
					}
					else if ( board[ i ][ j ] == 2 )
					{
						// player 2 wins , player 1 loses
						EndGame = true;
						winner = 2;
					}
				}
			}
		}
		
		// check for draw
		
		boolean draw = true;
		for ( int i = 0; i < 6; i += 1 )
		{
			for ( int j = 0; j < 7; j += 1 )
			{
				if ( board[i][j] == 0 )
				{
					draw = false;
					break;
				}
			}
		}
		
		if ( draw == true )
		{
			// Action for draw
			EndGame = true;
			winner  = 0;
		}
		
	}
	
	public static void ShowResult()
	{
		JFrame ResultFrame = new JFrame( "Result" );
		
		ResultFrame.setVisible(true);
		ResultFrame.setSize( 550 , 250 ); 
		ResultFrame.setResizable(false);
		ResultFrame.setLayout( GridLayout );
		ResultFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		JLabel ResultText = new JLabel("");
		
		if 		( winner == 1 )
		{
			ResultText.setText( "   Congrats!  You are the Winner" );	
			ResultText.setForeground( Color.blue );
		}
		else if ( winner == 2 )
		{
			ResultText.setText( "   Hard Luck! You are the Loser" );
			ResultText.setForeground( Color.red );
		}
		else if ( winner == 0 )
		{
			ResultText.setText( "Draw" );
			ResultText.setForeground( Color.darkGray );
		}
		
		ResultText.setBounds(30, 10, 500, 180);
		ResultText.setFont( new Font( "Arial" , Font.BOLD , 30 ) );
		ResultFrame.add( ResultText );
		
	}
	
	public static void startGame() throws IOException
	{
		for ( int i = 0; i < 7; i++ )
		{
			control[i].setEnabled( false );
		}
		
		String	flag = "";
		while ( true )
		{
			if ( EndGame == true )
			{
				break;
			}
			
			if ( in.hasNextLine() == true )
			{
				flag = in.nextLine();
				
				if 		( flag.equals( "p1" ) )
				{
					// Enable to play
					for ( int i = 0; i < 7; i++ )
					{
						if ( control[i].getText().equals("▼") )
						{
							control[i].setEnabled( true );
						}
					}
				}
				else if ( flag.equals( "p2" ) )
				{
					// opponent turn
					// getServer
					int index = in.nextInt();
					
					for ( int row = 5; row >= 0; row -= 1 )
					{
						if ( board[ row ][ index ] == 0 )
						{
							board[ row ][ index ] = 2;
							grids[ row ][ index ].setText( "O" );
							grids[ row ][ index ].setForeground( player2Color );
							
							// lock if full
							break;
						}
					}
					if ( board[ 0 ][ index ] != 0 )
					{
						control[ index ].setText( "▬" );
						control[ index ].setEnabled( false );
					}
					
					CheckWinner();
					
					// if win or lose Break
					if ( EndGame == true )
					{
						break;
					}
				}
			}
		}
		
		ShowResult();
		in.close(); out.close(); socket.close(); 
	}
}
