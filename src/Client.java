import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) {
		// read stdIn
		ClientGUI GUI = new ClientGUI();
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		
		// start the client GUI
		ClientGUI.ClientWindow();
		
		// tell the GUI what port and ip to call for senReq
		GUI.sendIP(ip);
		GUI.sendPort(port);
	
	}
	
	public static String sendReq(String ip, int port, String word, String definition, String queryType)
	{
		String output = null;
		try(Socket socket = new Socket(ip, port);){
			
			System.out.println("Connection established");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

			Scanner scanner = new Scanner(System.in);
			String line = null;
			
			
			Map<String, String> data = new HashMap<>();
			data.put("option", queryType);
			data.put("word", word);
			data.put("definition", definition);
				
				
			String map = data.toString();
			System.out.println("about to send");
			out.write(map + "\n");
			out.flush();
			System.out.println("Message sent");
			output = in.readLine();
			System.out.println("Message received: " + output);
			in.close();
			out.close();
			socket.close();
			System.out.println("all good!");
			return output;
		}
		catch (Exception e) 
		{
			System.out.println(e);
		} 
		
		return "nope";
		
	}
	
	
}


