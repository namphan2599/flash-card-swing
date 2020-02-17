package core;

import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class QuizCardPlayer extends JFrame {

	private JPanel mainPanel;
	private JTextArea display;
	private JTextArea answer;
	private ArrayList<QuizCard> cardList;
	private QuizCard currentCard;
	private int currentCardIndex;
	 
	private JButton nextButton;
	private boolean isShowAnswer;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuizCardPlayer frame = new QuizCardPlayer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public QuizCardPlayer() {
		 super("Quiz Card Player");
		 mainPanel = new JPanel();
		 Font bigFont = new Font("sanserif", Font.BOLD, 24);
		 
		 display = new JTextArea(10,20);
		 display.setFont(bigFont);
		 
		 display.setLineWrap(true);
		 display.setEditable(false);
		 
		 JScrollPane qScroller = new JScrollPane(display);
		 qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		 qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		 nextButton = new JButton("Show Question");
		 mainPanel.add(qScroller);
		 mainPanel.add(nextButton);
		 nextButton.addActionListener(new NextCardListener());
		 
		 JMenuBar menuBar = new JMenuBar();
		 JMenu fileMenu = new JMenu("File");
		 JMenuItem loadMenuItem = new JMenuItem("Load card set");
		 loadMenuItem.addActionListener(new OpenMenuListener());
		 fileMenu.add(loadMenuItem);
		 menuBar.add(fileMenu);
		 setJMenuBar(menuBar);
		 getContentPane().add(BorderLayout.CENTER, mainPanel);
		 setSize(640,500);
		  
	}
	
	public class NextCardListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			if (isShowAnswer) {
			// show the answer because they’ve seen the question
			display.setText(currentCard.getAnswer());
			nextButton.setText("Next Card");
			isShowAnswer = false;
			} else {
				// show the next question
				if (currentCardIndex < cardList.size()) {
					showNextCard();
				} else {
					// there are no more cards!
					display.setText("That was last card");
					nextButton.setEnabled(false);
				}
			}
		}
	}
	
	public class OpenMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(null);
			loadFile(fileOpen.getSelectedFile());
		}
	}
	
	private void loadFile(File file) {
		cardList = new ArrayList<QuizCard>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				makeCard(line);
			}
			reader.close();
		} catch (Exception ex) {
			System.out.println("couldn't read the card file");
			ex.printStackTrace();
		}
	}
	
	private void makeCard(String lineToParse) {
		String[] result = lineToParse.split("/");
		QuizCard card = new QuizCard(result[0], result[1]);
		cardList.add(card);
		System.out.println("made a card");
	}
	
	private void showNextCard() {
		currentCard = cardList.get(currentCardIndex);
		currentCardIndex++;
		display.setText(currentCard.getQuestion());
		nextButton.setText("Show Answer");
		isShowAnswer = true;
	}

}
