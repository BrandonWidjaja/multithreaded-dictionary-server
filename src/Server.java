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

import javax.swing.JOptionPane;

public class Server {
	
	static int port;
	static String path;
	
	static String option;
	static String word;
	static String definition;
	
	static Scanner scanner;
	static int counter = 0;
	public static void main(String[] args) {
		try {
			port = Integer.parseInt(args[0]);
			path = args[1];
			File f = new File(path);
			if(!f.exists() || f.isDirectory()) { 
				JOptionPane.showMessageDialog(null, "File was not found at the given path, please make sure the path is correct and that the file exists.", "Server", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		catch (Exception e) {
			
		 	JOptionPane.showMessageDialog(null, "Please enter the port number followed by the 'File path'.", "Server", JOptionPane.ERROR_MESSAGE);
	 		System.exit(0);
		 	
		}
		try {
			ServerSocket serveSocket = new ServerSocket(port);
			System.out.println("Server started on port " + port);
			
			while(true) {
				Socket s = serveSocket.accept();
				Thread thread = new Thread(() -> {clientProcess(s, path);});
				counter++;
				System.out.println("Client " + counter + " has been connected");
				System.out.println("New client thread created");
				thread.start();
			}
		}
		catch (IOException e) 
		{
			
			JOptionPane.showMessageDialog(null, "The port is either invalid, or may be in use by another program, please try another port.", "Server", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		
	}
	
	
	public static void clientProcess(Socket cSocket,String path) {
		try {
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(cSocket.getOutputStream()));
			
			File checkExist = new File(path);
			if (!checkExist.exists()) {
				out.write("There was an error trying to access the dictionary file" + "\n");
				out.newLine();
				out.flush();
				return;
			}
			
			String inputString = null;
			
			while ((inputString = in.readLine()) != null) {
				String inputVal = inputString;
				
				// convert string into a map
				inputVal = inputVal.substring(1, inputVal.length()-1); 
				// ignore delimiters
				String[] keyValuePairs = inputVal.split("(?<!\\\\)[:,|&]");  
				
				Map<String,String> map = new HashMap<>();               
				
				for(String pair : keyValuePairs)                       
				{
				    String[] entry = pair.split("=");                   
				    map.put(entry[0].trim(), entry[1].trim());  
				    
				}
				
				
				// extract data from map
				try {
					option = map.get("option");
					word = map.get("word");
					definition = map.get("definition");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "The server recieved an incompatible request.", "Server", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				
				// System.out.println(" Option: "+ map.get("option") + "\n Word: " + map.get("word") + "\n Definition: " + map.get("definition"));
				
				processReq(in, out, path, option, word, definition);
			
			}
			out.close();
			in.close();
		}catch (IOException e) {	
			JOptionPane.showMessageDialog(null, "A client was disconnected", "Server", JOptionPane.ERROR_MESSAGE);
		} 
	}
	
	public static synchronized void processReq(BufferedReader in, BufferedWriter out, String path, String option, String word, String definition) {
		try {
			try {
				
				if(option.equals("add")) {
					String output = findWord(word, path, option);
					if (output.equals("not found")) {
						try {
							PrintWriter fileWrite = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
							
							fileWrite.println(word + " = " + definition);
							fileWrite.flush();
							fileWrite.close();
							
							out.write("Word added successfully!" + "\n");
							out.flush();
							System.out.println("Response sent");
						}catch (Exception e) {
							out.write("There was an error trying to write the word to the file" + "\n");
							out.newLine();
							out.flush();
							JOptionPane.showMessageDialog(null, "Something went wrong writing a word to the dictionary", "Server", JOptionPane.ERROR_MESSAGE);
						}
					} else if (output.equals("exists")) {
						out.write("Word is already in the dictionary!" + "\n");
						out.flush();
						System.out.println("Response sent");
					}
					
					
				}
			}catch (NullPointerException e) {
				out.write("Data sent to server is missing or altered" + "\n");
				out.newLine();
				out.flush();
			}
			
			try {
				if(option.equals("query")) {
					
					String output = findWord(word, path, option);
					
					if(!output.equals("not found")){
						output = output.replace("\\,", ",");
						
						out.write(output + "\n");
						out.newLine();
						out.flush();
						System.out.println("Response sent");
					} else if(output.equals("not found")) {
						out.write("That word does not exist" + "\n");
						out.newLine();
						out.flush();
						System.out.println("Response sent");
					} else {
						out.write("something went wrong" + "\n");
						out.newLine();
						out.flush();
						System.out.println("Response sent");
					}
				}
			}catch (NullPointerException e) {
				out.write("Data sent to server is missing or altered" + "\n");
				out.newLine();
				out.flush();
			} catch (Exception e) {
				out.write("Something went wrong during query" + "\n");
				out.newLine();
				out.flush();
			}
			
			try {
				if(option.equals("remove")) {
					try {
						File inputFile = new File(path);
						File temporaryFile = new File("myTempFile.txt");
						
						BufferedReader removeReader = new BufferedReader(new FileReader(inputFile));
						PrintWriter removeWriter = new PrintWriter(new FileWriter(temporaryFile));
						
						String curr = null;
						Boolean removed = false;
						while((curr = removeReader.readLine()) != null) {
							String findWordResult =  findWord(word, path, option);
							if (!curr.substring(0, findWordResult.length()).equalsIgnoreCase(findWordResult)) {
						          removeWriter.println(curr);
						          
						    } else {
						    	removed = true;
						    }
						}
						
						removeWriter.close();
						removeReader.close();
						try {
							inputFile.delete();
						} catch (Exception e){
							System.out.println(e);
							out.write("Something went wrong trying to delete the dictionary" + "\n");
							out.newLine();
							out.flush();
						}
						try {
							temporaryFile.renameTo(inputFile);
						} catch (Exception e) {
							System.out.println(e);
							out.write("Something went wrong trying to add the temp file as the dictionary" + "\n");
							out.newLine();
							out.flush();
						}
						
						if (removed) {
							out.write("Word removed successfully!" + "\n");
						} else {
							out.write("Word doesnt exist!" + "\n");
						}
						
					} catch (Exception e){
						out.write("Something went wrong trying to write definitions to a temporary file" + "\n");
						out.newLine();
						out.flush();
					}
					
					out.newLine();
					out.flush();
					
				}
			}catch (NullPointerException e) {
				out.write("Data sent to server is missing or altered" + "\n");
				out.newLine();
				out.flush();
			}
			
			try {
				
				if (option.equals("update")) {
					try {
						File inputFile = new File(path);
						File temporaryFile = new File("myTempFile.txt");
						
						BufferedReader removeReader = new BufferedReader(new FileReader(inputFile));
						PrintWriter removeWriter = new PrintWriter(new FileWriter(temporaryFile));
						
						String curr = null;
						Boolean updated = false;
						while((curr = removeReader.readLine()) != null) {
							String findWordResult =  findWord(word, path, option);
							if (!curr.substring(0, findWordResult.length()).equalsIgnoreCase(findWordResult)) {
						          removeWriter.println(curr);
						          
						    } else {
						    	removeWriter.println(findWordResult + definition);
						    	updated = true;
						    }
						}
						
						removeWriter.close();
						removeReader.close();
						try {
							inputFile.delete();
						} catch (Exception e){
							System.out.println(e);
						}				
	
						temporaryFile.renameTo(inputFile);
						if (updated) {
							out.write("Word updated successfully!" + "\n");
						} else {
							out.write("Word doesnt exist!" + "\n");
						}
						
						out.newLine();
						out.flush();
					} catch (Exception e) {
						out.write("Something went wrong trying to write definitions to a temporary file" + "\n");
						out.newLine();
						out.flush();
					}
				}
			}catch (NullPointerException e) {
				out.write("Data sent to server is missing or altered" + "\n");
				out.newLine();
				out.flush();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Server could not process a request", "Server", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static String findWord(String word, String path, String option) {
		String sendBack = "not found";
		try {
			scanner = new Scanner(new File(path));
			scanner.useDelimiter("[=\n]");
			while(scanner.hasNext()){
				String nextWord = scanner.next();
				nextWord = nextWord.substring(0, nextWord.length() - 1);
				String skipDef = scanner.next();
				   
				if(nextWord.equalsIgnoreCase(word)){
					if (option.equals("remove")) {
				    	sendBack = word + " = ";
				    }
				    if (option.equals("query")) {
				    	sendBack = skipDef;
				   	}
					if (option.equals("add")){
						sendBack = "exists";
					}
					if (option.equals("update")){
						sendBack = word + " = ";
					}
				}
			}	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "File was not found at the given path, please make sure the path is correct and that the file exists.", "Server", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error searching for matches in dictionary file.", "Server", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

			
		
		scanner.close();
		return sendBack;
		
	}
}
