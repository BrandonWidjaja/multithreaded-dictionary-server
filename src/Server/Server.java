package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
			System.out.println("Server started on port" + port);
			
			while(true) {
				Socket s = serveSocket.accept();
				System.out.println("New client");
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
			BufferedReader in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(cSocket.getOutputStream()));
			// PrintWriter out = new PrintWriter(cSocket.getOutputStream(), true);
			
			String inputString = null;
			
			while ((inputString = in.readLine()) != null) {
				System.out.println(" Sent from the client: "+ inputString);
				out.write("Server Ack " + inputString + "\n");
				out.flush();
				System.out.println("Response sent");
				
//				
//				System.out.println("Message from client " + inputString);
//				out.write("Server Ack " + inputString + "\n");
//				out.flush();
//				System.out.println("Response sent");
			
			}
			
		}catch (Exception e) {
			
		}
		try {
			cSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
