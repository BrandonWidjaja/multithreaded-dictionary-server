import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ServerGUI {
	Server objServer = new Server();
	JFrame interfaceWindow;
	
	static String path;
	public void sendPath(String path)
	{
		ServerGUI.path = path;
	}
	public static void ServerWindow(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI();
					window.interfaceWindow.setResizable(false);
					
					window.interfaceWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public ServerGUI() 
	{
		
		interfaceWindow = new JFrame();
		interfaceWindow.setTitle("Dictionary");
		interfaceWindow.setBounds(180, 180, 955, 650);
		interfaceWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JTextArea dictBox= new JTextArea();
		dictBox.setBounds(115, 50, 680, 550);
		dictBox.setEditable(false);
		dictBox.setBackground(Color.WHITE);
		dictBox.setLineWrap(true);
		dictBox.setWrapStyleWord(true);
		JScrollPane dictScroll = new JScrollPane(dictBox);
		dictScroll.setBounds(115, 50, 700, 550);
		dictScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		interfaceWindow.getContentPane().add(dictScroll);
		interfaceWindow.getContentPane().add(dictBox);
		
		
		
		JLabel dictLabel = new JLabel("Dictionary Content");
		dictLabel.setBounds(115, 25, 140, 25);
		interfaceWindow.getContentPane().add(dictLabel);
		
		JButton refreshButton = new JButton("Refresh");
		refreshButton.setBounds(715, 25, 100, 25);
		interfaceWindow.getContentPane().add(refreshButton);
		
		
		
		
		interfaceWindow.getContentPane().setLayout(null);
		
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				dictBox.setText(Server.sendDict(path));
				
			}
		});
		
		
		
	}
}

