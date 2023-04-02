package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
	
	static int port;
	static String path;
	
	static String option;
	static String word;
	static String definition;
	
	static Scanner scanner;
	
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
				Thread thread = new Thread(() -> {clientProcess(s, path);});
				System.out.println("thread created");
				thread.start();
			}
		}
		catch (IOException e) 
		{
			
		}
		
		
	}
	
	
	public static void clientProcess(Socket cSocket,String path) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(cSocket.getOutputStream()));
		
			
			String inputString = null;
			
			while ((inputString = in.readLine()) != null) {
				String inputVal = inputString;
				// convert string into a map
				inputVal = inputVal.substring(1, inputVal.length()-1);   
				String[] keyValuePairs = inputVal.split(",");             
				Map<String,String> map = new HashMap<>();               

				for(String pair : keyValuePairs)                       
				{
				    String[] entry = pair.split("=");                   
				    map.put(entry[0].trim(), entry[1].trim());          
				}
				
				System.out.println(" Option: "+ map.get("option") + "\n Word: " + map.get("word") + "\n Def: " + map.get("definition"));
				
				// extract data from map
				option = map.get("option");
				word = map.get("word");
				definition = map.get("definition");
				
				
				
				if(option.equals("add")) {
					
					PrintWriter fileWrite = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
					
					fileWrite.println(word + " = " + definition);
					fileWrite.flush();
					fileWrite.close();
					
					out.write("word added successfully!" + "\n");
					out.flush();
					System.out.println("Response sent");
					
				}
				if(option.equals("query")) {
					if(findWord(word, path, option).equals("found")){
						out.write("word found!" + "\n");
						out.flush();
						System.out.println("Response sent");
					} else if(findWord(word, path, option).equals("not found")) {
						out.write("word does not exist" + "\n");
						out.flush();
						System.out.println("Response sent");
					} else {
						out.write("something went wrong" + "\n");
						out.flush();
						System.out.println("Response sent");
					}
				}
				if(option.equals("remove")) {
					File inputFile = new File(path);
					File temporaryFile = new File("myTempFile.txt");
					
					BufferedReader removeReader = new BufferedReader(new FileReader(inputFile));
					PrintWriter removeWriter = new PrintWriter(new FileWriter(temporaryFile));
					
					String curr = null;
					
					while((curr = removeReader.readLine()) != null) {
						
						if (!curr.trim().equals(findWord(word, path, option))) {

					          removeWriter.println(curr);
					          
					    }
						
						
						
					}
					
					
					removeWriter.close();
					removeReader.close();
					try {
						inputFile.delete();
					} catch (SecurityException e){
						System.out.println(e);
					}				
					
					temporaryFile.renameTo(inputFile);
					
					
					out.write("word removed successfully!" + "\n");
					out.flush();
					//System.out.println("Response sent (success)");
					
				}
				
			
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
	
	public static String findWord(String word, String path, String option) {
		try {
			scanner = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (option.equals("query")) {
			while(scanner.hasNextLine()){
			    String str = scanner.nextLine();
			    if(str.indexOf(word) != -1){
			        return "found";
			    }
			}
		} else if(option.equals("remove")) {
			while(scanner.hasNextLine()){
			    String str = scanner.nextLine();
			    if(str.indexOf(word) != -1){
			        return word + " = " + definition;
			    }
			}
		}
		
		return "not found";
		
	}
}
