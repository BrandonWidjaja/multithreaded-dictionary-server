package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Server {
	static int port;
	static String path;
	public static void main(String[] args) {
		try {
			ServerSocket serveSocket = new ServerSocket(port);
			while(true) {
				Socket s = serveSocket.accept();
				Thread thread = new Thread(() -> {startClient(s, path);});
				thread.start();
			}
		}
		catch (IOException e) 
		{
			
		}
		
		
	}
	
	public static void startClient(Socket clientSocket,String path) {
		
	}
}
