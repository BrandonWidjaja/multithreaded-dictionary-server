import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class ClientGUI
{
	Client objClient = new Client();
	JFrame interfaceWindow;
	
	static Socket socket;
	
	
	public void sendSocket(Socket socket)
	{
		ClientGUI.socket = socket;
	}
	
	
	
	
	public static void ClientWindow(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.interfaceWindow.setResizable(false);
					window.interfaceWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public ClientGUI() 
	{
		JTextField word_input;
		interfaceWindow = new JFrame();
		interfaceWindow.setTitle("My Dictionary");
		interfaceWindow.setBounds(200, 200, 810, 530);
		interfaceWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton query_button = new JButton("Query");
		query_button.setBounds(419, 50, 85, 43);
		
		JButton add_button = new JButton("Add");
		add_button.setBounds(504, 50, 85, 43);
		
		JButton update_button = new JButton("Update");
		update_button.setBounds(589, 93, 85, 43);
		
		JButton remove_button = new JButton("Remove");
		remove_button.setBounds(589, 50, 85, 43);
		
		
		
		JLabel word_label = new JLabel("Word");
		word_label.setBounds(110, 20, 100, 25);
		interfaceWindow.getContentPane().add(word_label);
		word_input = new JTextField();
		word_input.setBounds(110, 50, 300, 45);
		
		
		
		JLabel definition_label = new JLabel("Definition");
		definition_label.setBounds(110, 115, 100, 25);
		JTextArea definition_input = new JTextArea();
		definition_input.setBounds(110, 155, 205, 60);
		JScrollPane definitionScroll = new JScrollPane(definition_input);
		definitionScroll.setBounds(110, 155, 565, 60);
		definitionScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		
		JLabel result_label = new JLabel("Result");
		result_label.setBounds(110, 225, 100, 25);
		interfaceWindow.getContentPane().add(result_label);
		final JTextArea result_output = new JTextArea();
		result_output.setLineWrap(true);
		result_output.setWrapStyleWord(true);
		result_output.setBounds(110, 265, 565, 210);
		result_output.setEditable(false);
		result_output.setBackground(Color.WHITE);
		


		interfaceWindow.getContentPane().setLayout(null);
		interfaceWindow.getContentPane().add(definition_label);
		interfaceWindow.getContentPane().add(word_input);
		interfaceWindow.getContentPane().add(result_output);
		interfaceWindow.getContentPane().add(query_button);
		interfaceWindow.getContentPane().add(add_button);
		interfaceWindow.getContentPane().add(update_button);
		interfaceWindow.getContentPane().add(remove_button);
		interfaceWindow.getContentPane().add(definitionScroll);
		
		//Query button listener
		add_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				//add event for Query button
				String word = word_input.getText();
				String meaning = definition_input.getText();
				String newlineCheck = meaning.replace("\n", "");
				if(word.isEmpty())
				{
					result_output.setText("The \"word\" field cannot be empty!");
					return;
				} else if (meaning.isEmpty()) {
					result_output.setText("Please enter a definition!");
					return;
				} else if (newlineCheck.isEmpty()) {
					result_output.setText("Please enter a definition!");
					return;
				}
				
				String displayText = Client.sendReq(socket, word, meaning, "add");
				
				
				
				word_input.setText("");
				result_output.setText(displayText);
				if (displayText.substring(0, "Word added successfully!".length()).equals("Word added successfully!")) {
					definition_input.setText("");
				}
			}
			
		});
		
		
		query_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("empty");
				//add event for Query button
				String word = word_input.getText();
				if(word.isEmpty())
				{
					
					result_output.setText("The \"word\" field cannot be empty!");
					return;
				}
				String meaning = definition_input.getText();
				
				String displayText = Client.sendReq(socket,word,meaning, "query");
				word_input.setText("");
				result_output.setText(displayText);
			}
			
		});
		
		remove_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				//add event for Query button
				String word = word_input.getText();
				if(word.isEmpty())
				{
					result_output.setText("The \"word\" field cannot be empty!");
					return;
				}
				String meaning = definition_input.getText();
				
				String displayText = Client.sendReq(socket,word,meaning, "remove");
				word_input.setText("");
				result_output.setText(displayText);
			}
			
		});
		
		update_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				//add event for Query button
				String word = word_input.getText();
				String meaning = definition_input.getText();
				if(word.isEmpty())
				{
					result_output.setText("The \"word\" field cannot be empty!");
					return;
				} else if (meaning.isEmpty()) {
					result_output.setText("Please enter a definition!");
					return;
				}
				
				String displayText = Client.sendReq(socket, word, meaning, "update");
				word_input.setText("");
				result_output.setText(displayText);
				System.out.println(displayText);
				if(displayText.substring(0, "Word updated successfully!".length()).equals("Word updated successfully!")) 
				{
					definition_input.setText("");
					
				}
			}
			
		});
		
	}

}
