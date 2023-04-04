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

			GUI.sendIP(ip);
			GUI.sendPort(port);
		} catch (ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid IP and port number. Format: java –jar DictionaryClient.jar <server-address> <server-port>", "Client", JOptionPane.ERROR_MESSAGE);
			
		}
		catch (Exception e) {
			
			JOptionPane.showMessageDialog(null, "Something went wrong creating the client window", "Client", JOptionPane.ERROR_MESSAGE);
			
		}
		
	
	}
	
	public static String sendReq(String ip, int port, String word, String definition, String queryType)
	{
		String output = null;
		try(Socket socket = new Socket(ip, port);){
			
			System.out.println("Connection established");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

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
			in.close();
			out.close();
			socket.close();
			
			
		}catch (UnknownHostException e) {
			e.printStackTrace();
			output = "Unkown host exception. Please check the IP address";
			JOptionPane.showMessageDialog(null, "Unkown host exception. Please check the IP address", "Client", JOptionPane.ERROR_MESSAGE);
			
		}catch (ConnectException e) {
			e.printStackTrace();
			output = "Connect exception. Please check the IP address";
			JOptionPane.showMessageDialog(null, "Connect exception. Please check the IP address", "Client", JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			output = "Something went wrong trying to send the query to the server";
			JOptionPane.showMessageDialog(null, "Something went wrong trying to send the query to the server", "Client", JOptionPane.ERROR_MESSAGE);
			
		}catch (Exception e) {
			e.printStackTrace();
			output = "Something went wrong trying to send the query to the server";
			JOptionPane.showMessageDialog(null, "Something went wrong trying to send the query to the server", "Client", JOptionPane.ERROR_MESSAGE);
			
		}
		
		return output;
		
	}
	
}


