package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	static int port;
	static String path;
	
	public static void main(String[] args) {
		try {
			port = Integer.parseInt(args[0]);
			path = args[1];
			
		}
		catch (Exception e) {
			
		}
		try {
			ServerSocket serveSocket = new ServerSocket(port);
			while(true) {
				Socket s = serveSocket.accept();
				Thread thread = new Thread(() -> {startClient(s, path);});
				System.out.println("thread created");
				thread.start();
			}
		}
		catch (IOException e) 
		{
			
		}
		
		
	}
	
	public static void startClient(Socket cSocket,String path) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(cSocket.getInputStream(), "UTF-8"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(cSocket.getOutputStream(), "UTF-8"));
			
			String inputString = null;
			
			while ((inputString = reader.readLine()) != null) {
				System.out.printf(
                        " Sent from the client: %s\n",
                        inputString);
                    
                    System.out.println("ack sent back");
			}
			
		}catch (Exception e) {
			
		}
	}
}
