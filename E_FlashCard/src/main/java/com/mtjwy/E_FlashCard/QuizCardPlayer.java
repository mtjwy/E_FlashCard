package com.mtjwy.E_FlashCard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class QuizCardPlayer {
	
	private JTextArea display;
    private JTextArea answer;
    private ArrayList<QuizCard> cardList;
    private QuizCard currentCard;
    private Iterator<QuizCard> cardIterator;
    private JFrame frame;
    private JButton nextButton;
    private boolean isShowAnswer;
    
    public static void main (String[] args) {
    	QuizCardPlayer qReader = new QuizCardPlayer();
        qReader.go();
     }
	
	public void go() {
		//build and display gui
		frame = new JFrame("Quiz Card Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        display = new JTextArea(9,20);
        display.setFont(bigFont);
        display.setLineWrap(true);
        display.setWrapStyleWord(true);
        display.setEditable(false);
       
        JScrollPane qScroller = new JScrollPane(display);
        qScroller.setVerticalScrollBarPolicy(
              ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      
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
        frame.setJMenuBar(menuBar);
    
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500,600);
        frame.setVisible(true);   
	}
	
	class NextCardListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			//if this is a question, show the answer, otherwise show next question
			//set a flag for whether we're viewing a question or answer
			
			if (isShowAnswer) {
				//show the answer
				display.setText(currentCard.getAnswer());
				nextButton.setText("Next Card");
				isShowAnswer = false;
			} else {
				if (cardIterator.hasNext()) {
					showNextCard();
				} else {
					display.setText("That was last card");
					nextButton.disable();
				}
			}
		}
		
	}
	
	private void showNextCard() {
        currentCard = (QuizCard) cardIterator.next();
        display.setText(currentCard.getQuestion());
        nextButton.setText("Show Answer");
        isShowAnswer = true;
   }
	
	class OpenMenuListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// bring up a file dialog box
			//let the user navigate to and choose a card set to open
			JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
		}
		
	}
	
	private void loadFile(File file) {
		//must build an ArrayList of cards, by reading them from a text file
		//called form the OpenMenuListener event handler, reads the file one line at a time
		//and tells the makeCard() method to make a new card out of the line
		//(one line in the file holds both the question and answer, separated by a "/"
		cardList = new ArrayList();
	      try {
	         BufferedReader reader = new BufferedReader(new FileReader(file));
	         String line = null;
	         while ((line = reader.readLine()) != null) {
	            makeCard(line);
	         }
	         reader.close();

	      } catch(Exception ex) {
	          System.out.println("couldn't read the card file");
	          ex.printStackTrace();
	      }

	     // now time to start
	     cardIterator = cardList.iterator();
	     showNextCard();
	}
	
	private void makeCard(String lineToParse) {
		//called by the loadFile method, takes a line form the text file
		//and parse into two pieces -question and answer -- and creates a
		//new QuizCard and adds it to the ArrayList calles CardList
		StringTokenizer parser = new StringTokenizer(lineToParse, "/");
	      if (parser.hasMoreTokens()) {
	         QuizCard card = new QuizCard(parser.nextToken(), parser.nextToken());
	         cardList.add(card);
	      }
	}
}













