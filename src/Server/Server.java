package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
		
			
			String inputString = null;
			
			while ((inputString = in.readLine()) != null) {
				String value = inputString;
				value = value.substring(1, value.length()-1);   
				String[] keyValuePairs = value.split(",");             
				Map<String,String> map = new HashMap<>();               

				for(String pair : keyValuePairs)                       
				{
				    String[] entry = pair.split("=");                   
				    map.put(entry[0].trim(), entry[1].trim());          
				}
				
				System.out.println(" Type: "+ map.get("type") + "\n Word: " + map.get("word") + "\n Def: " + map.get("meaning"));
				out.write(inputString + "\n");
				out.flush();
				System.out.println("Response sent");

			
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
