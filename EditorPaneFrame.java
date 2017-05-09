package web;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


public class EditorPaneFrame extends JFrame {

	private JTextField url;
	private JCheckBox editButton;
	private JButton goButton;
	private JButton backButton;
	private JButton forwardButton;
	private JButton homeButton;
	private JButton reloadButton;
	private JButton historyButton;
	private JButton bookmarkButton;
	private JButton bookmarkHistoryButton;
	private JEditorPane editorPane;
	private Stack<String> urlStack = new Stack<String>();
	private Stack<String> urlStack2 = new Stack<String>();

	public EditorPaneFrame() {
		setTitle("Java Web Browser");
		setSize(600, 400);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		editorPane = new JEditorPane();

		// Set up file menu and make an exit as a menu item.
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		fileExitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionExit();
			}
		});

		fileMenu.add(fileExitMenuItem);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

		// set up text field and load button for typing in URL.

		url = new JTextField("https://www.google.co.uk/", 20);

		try {
			urlStack.push(url.getText());
			editorPane.setPage(url.getText());
			write(url.getText());
		} catch (IOException e1) {
			// TODO Auto-generated catch block e1.printStackTrace();
		}

		goButton = new JButton("Go");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					urlStack.push(url.getText());
					editorPane.setPage(url.getText());
					write(url.getText());

				} catch (Exception e) {
					showError("Invalid URL. Try again.");
					editorPane.setText("Error: " + e + "\n It seems that you did something wrong. Exit and start again");
				}
			}

		});
		// Set up reload button to refresh whenever you press it.
		reloadButton = new JButton("Reload");
		reloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					// remember URL for back button
					editorPane.setPage(url.getText());
				} catch (Exception e) {
					showError("Invalid URL");
					editorPane.setText("Error: " + e);
				}
			}
		});
		// Set up Home button.
		homeButton = new JButton("Home");
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					url.setText("https://www.google.co.uk/");
					editorPane.setPage("https://www.google.co.uk/");
					write("https://www.google.co.uk/");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// Set up Back button 
		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
				if (urlStack.size() < 1)
					return;
				try {

					String urls = (String) urlStack.pop();
					urlStack2.push(urls);
					String urlString = (String) urlStack.peek();
					url.setText(urlString);
					editorPane.setPage(urlString);
					write(urlString);
				} catch (IOException e) {
					showError("Invalid URL. Try again.");
					editorPane.setText("Error: " + e + "\n It seems that you did something wrong");
				} catch (EmptyStackException e){
					showError("There is no more back you can go");
					editorPane.setText("Error: " + e + "\n It seems that you did something wrong. Exit and start again");
				}
			}
		});
		// Set up Forward button 
		forwardButton = new JButton("Forward");
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (urlStack2.size() < 1)
					return;
				try {
					String urls = (String) urlStack2.pop();
					urlStack.push(urls);
					url.setText(urls);
					editorPane.setPage(urls);
					write(urls);
				} catch (IOException e) {
					editorPane.setText("Error : " + e);
				}
			}
		});
		// Set up Bookmark button.
		bookmarkButton = new JButton("BookMark");
		bookmarkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				try {
					writeBookmark(url.getText().toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		// Set up a button to find all of your bookmarks in a frame.
		bookmarkHistoryButton = new JButton("Favourites");
		bookmarkHistoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					FileReader fr = new FileReader("BookMark.txt");
					BufferedReader br = new BufferedReader(fr);
					String line = null;
					JPanel panel4 = new JPanel();
					JFrame historyFrame = new JFrame("HistoryFrame");
					historyFrame.add(panel4, "North");
					do {
						JLabel label = new JLabel(line);
						historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						setPreferredSize(new Dimension(450, 310));
						historyFrame.pack();
						historyFrame.setVisible(true);
						panel4.add(label);

					} while (line != null);
					
					br.close();
				} 
				 catch (FileNotFoundException e){
					showError("There are no bookmarks available");
					editorPane.setText("Error: " + e + "\n It seems that you did something wrong. Exit and start again");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		// Set up a history button. 
		historyButton = new JButton("History");
		historyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					FileReader fr = new FileReader("HistoryBr.txt");
					BufferedReader br = new BufferedReader(fr);
					String line = null;
					JPanel panel3 = new JPanel();
					JFrame historyFrame = new JFrame("HistoryFrame");
					historyFrame.add(panel3, "North");
					do {

						line = br.readLine();
						JLabel label = new JLabel(line);
						historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						historyFrame.getContentPane().add(label);
						setPreferredSize(new Dimension(450, 310));
						historyFrame.pack();
						historyFrame.setVisible(true);
						panel3.add(label);

					} while (line != null);
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(new HyperlinkListener() {
			// Set up so you can press on Hyperlinks.
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						urlStack.push(event.getURL().toString());
						url.setText(event.getURL().toString());
						editorPane.setPage(event.getURL());
						write(event.getURL().toString());
					} catch (IOException e) {
						editorPane.setText("Error: " + e);
					}
				}
			}
		});
		// Check the box so you can edit the page.
		editButton = new JCheckBox();
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editorPane.setEditable(editButton.isSelected());
			}
		});

		Container contentPane = getContentPane();
		contentPane.add(new JScrollPane(editorPane), "Center");
		
		//Adding the buttons and labels into the panel.
		JPanel panel = new JPanel();
		panel.add(new JLabel("URL:"));
		panel.add(url);
		panel.add(goButton);
		panel.add(new JLabel("Edit"));
		panel.add(editButton);
		panel.add(reloadButton);
		panel.add(homeButton);
		contentPane.add(panel, "South");

		JPanel panel2 = new JPanel();
		panel2.add(backButton);
		panel2.add(forwardButton);
		panel2.add(historyButton);
		panel2.add(bookmarkButton);
		panel2.add(bookmarkHistoryButton);
		contentPane.add(panel2, "North");

	}
	// Method the exits the application.
	private void actionExit() {
		System.exit(0);
	}

	// Show dialog box with error message.
	private void showError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}
	// Method that writes in the HistoryBr.txt, as you exit it the fileis deleted.
	private void write(String text) throws IOException {
		File file = new File("HistoryBr.txt");
		file.deleteOnExit();
		FileWriter fw = new FileWriter(file, true);
		fw.write(url.getText().toString());
		fw.append(System.getProperty("line.separator"));
		fw.close();

	}
	// Method that writes in the BookMark.txt, as you exit it the fileis deleted.
	private void writeBookmark(String text) throws IOException {
		File file = new File("BookMark.txt");
		file.deleteOnExit();
		FileWriter fw = new FileWriter(file, true);
		fw.write(url.getText().toString());
		fw.append(System.getProperty("line.separator"));
		fw.close();

	
	}
}