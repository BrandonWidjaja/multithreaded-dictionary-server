import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;



public class Client {
	
	public static void main(String[] args) {
		try {
			ClientGUI GUI = new ClientGUI();
			String ip = args[0];
			int port = Integer.parseInt(args[1]);
			

			ClientGUI.ClientWindow();

			
			
				Socket socket = new Socket(ip, port);
				System.out.println("Connection established");
				
				GUI.sendSocket(socket);
				//GUI.sendPort(out);
				
			
			
		}catch (UnknownHostException e) {
			e.printStackTrace();
			//output = "Unkown host exception. Please check the IP address";
			JOptionPane.showMessageDialog(null, "Unkown host exception. Please check the IP address", "Client", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			
		}catch (ConnectException e) {
			e.printStackTrace();
			//output = "Connect exception. Please check the IP address";
			JOptionPane.showMessageDialog(null, "Connect exception. Please check the IP and PORT number", "Client", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			//output = "Something went wrong trying to send the query to the server";
			JOptionPane.showMessageDialog(null, "Something went wrong trying to send the query to the server", "Client", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			
		}
			 catch (ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid IP and port number. Format: java –jar DictionaryClient.jar <server-address> <server-port>", "Client", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		catch (Exception e) {
			
			JOptionPane.showMessageDialog(null, "Something went wrong creating the client window", "Client", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
	
	}
	
	public static String sendReq(Socket socket, String word, String definition, String queryType)
	{
		try {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		String output = null;
		
		System.out.println("req");
		Map<String, String> data = new HashMap<>();
		definition = definition.replace(",", "\\,");
		definition = definition.replace("\n", "@#!?");
		System.out.println(definition);
		data.put("option", queryType);
		data.put("word", word);
		if (definition.isEmpty()) {
			definition = " ";
		}
		data.put("definition", definition);
		System.out.println("Data is ready to send to server");
		String map = data.toString();
		
		
			out.write(map + "\n");
			out.flush();
			System.out.println("Message sent");
			output = in.readLine();
			output = output.replace("@#!?", "\n ");
			System.out.println("Message received: " + output);

			return output;
		} catch (NullPointerException e) {
			return "The client was unable write to the IP and PORT, please check both and restart";
		} catch (Exception e) {
			(e).printStackTrace();
			return "The client was unable to connect to the server, try restarting the client as this could mean the server was down on startup, or that the server closed due to an error.";
		}
		
		
	}
	
}


