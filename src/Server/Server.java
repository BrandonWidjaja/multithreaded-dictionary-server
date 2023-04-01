import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Server {
	static int port;
	public static void main(String[] args) {
		try {
			ServerSocket serveSocket = new ServerSocket(port);
			while(true) {
				Socket s = serveSocket.accept();
				Thread thread = new Thread(() -> 
				{
	
						serveClient(s,filepath);
					
				});
				t.start();
			}
		}
		catch (IOException e) 
		{
			
		}
		
		
	}

}
