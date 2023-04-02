package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		try(Socket socket = new Socket(ip, port);){
			System.out.println("Connection established");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

			Scanner scanner = new Scanner(System.in);
			String line = null;
			//While the user input differs from "exit"
			while (!(line = scanner.nextLine()).equalsIgnoreCase("exit"))
			{
				
				out.write(line + "\n");
				out.flush();
				System.out.println("Message sent");
				
				
				System.out.println("Message received: " + in.readLine());
				
			}
			scanner.close();
		}
		catch(Exception e) {
			
		}
	
	}
}
