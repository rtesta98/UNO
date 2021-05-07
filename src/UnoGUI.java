import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class UnoGUI extends JFrame{
	JFrame frame = new JFrame("Uno");
	private JButton drawButton;
	private JButton placeButton;
	private JButton playAgain;
	private JComboBox p1;
	private JTextField compdeck1;
	private JTextField current;
	private JTextField txtUpdate;
	//private JLabel cardholder;
	public ArrayList<UnoCard> playerdeck = new ArrayList<UnoCard>();
	public ArrayList<UnoCard> compdeck = new ArrayList<UnoCard>();
	public boolean playersTurn;
	public int win;
	public int currentDeck;
	Scanner input;
    UnoCard topCard; // card on top of the "pile"
    int choiceIndex; // Index of chosen card for both player and computer
    String currentColor; // Mainly used for wild cards
	

	public UnoGUI(){
		setTitle("UNO");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //ArrayList<UnoCard> playerdeck = new ArrayList<UnoCard>();
        //ArrayList<UnoCard> compdeck = new ArrayList<UnoCard>();
		JButton drawButton = new JButton("Draw");
		JButton placeButton = new JButton("Place");
		JButton playAgain = new JButton("Restart");
		JTextField compdeck1 = new JTextField(15);
		JTextField current = new JTextField(10);
		//JLabel cardholder = new JLabel("Drop Down Here");
		JTextField txtUpdate = new JTextField(20);
		
		
		
		///HERE - Unsure how to initialize
		ArrayList<UnoCard> usersCards = playerdeck;
		JComboBox p1 = new JComboBox((ComboBoxModel) usersCards);
		
		/*
		ArrayList cardlist= new ArrayList();
		cardlist.add(null);
		JComboBox p1 = (JComboBox)(cardlist);
		 */
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();

		panel.setBorder(BorderFactory.createTitledBorder("Player"));
		panel2.setBorder(BorderFactory.createTitledBorder("UNO"));
		panel3.setBorder(BorderFactory.createTitledBorder("Computer"));

		panel.setBackground(Color.RED);
		panel2.setBackground(Color.RED);
		panel3.setBackground(Color.RED);

		BoxLayout layout1 = new BoxLayout(panel, BoxLayout.Y_AXIS);
		BoxLayout layout2 = new BoxLayout(panel2, BoxLayout.Y_AXIS);
		BoxLayout layout3 = new BoxLayout(panel3, BoxLayout.Y_AXIS);
		panel.setLayout(layout1);
		panel2.setLayout(layout2);
		panel3.setLayout(layout3);

		p1.setAlignmentX(Component.CENTER_ALIGNMENT);
		placeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(p1);
		panel.add(placeButton);

		current.setAlignmentX(Component.CENTER_ALIGNMENT);
		drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel2.add(current);
		panel2.add(drawButton);
		panel2.add(txtUpdate);

		compdeck1.setAlignmentX(Component.CENTER_ALIGNMENT);
		playAgain.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel3.add(compdeck1);
		panel3.add(playAgain);

		frame.setLayout(new FlowLayout());
		frame.add(panel);
		frame.add(panel2);
		frame.add(panel3);
		frame.pack();

		drawButton.addActionListener(new drawCard());
		placeButton.addActionListener(new placeCard());
		playAgain.addActionListener(new playAgain());
		frame.setSize(400,150);
		frame.setVisible(true);
		
        System.out.println("\nWelcome to Uno! Initialising decks...");
        newGame();
	}
	private class drawCard implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			if (currentDeck ==1) {
				draw(1, playerdeck); //user needs to draw
				currentDeck = 0; //computers turn
			}
			else {//computer needs to draw
				draw(1, compdeck);
				currentDeck=1;
			}
		}
	}
	private class placeCard implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			//	place card;
			System.out.println('p');
			//currentDeck = 1;
		}
	}
	private class playAgain implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			// clear and restart;
			newGame();
		}
	}
	public static void main(String[] args) {
		UnoGUI game = new UnoGUI();
	}
	public static void draw(int cards, ArrayList<UnoCard> deck)
    {
        for (int i = 0; i < cards; i++)
            deck.add(new UnoCard() );
    }
	public void playGame() {
		gameLoop:
		 for (boolean playersTurn = true; win == 0; playersTurn ^= true) {
             choiceIndex = 0;
             txtUpdate.setText("\nThe top card is: " + topCard.getFace());

             //PLAYERS TURN
             if (playersTurn) { // Displaying user's deck
                 txtUpdate.setText("Your turn!");
                 //TAKING TURNS
                 if (choiceIndex == playerdeck.size() )
                     draw(1, playerdeck); //how to implement draw button?
                 else if (choiceIndex == playerdeck.size() + 1)
                     break gameLoop;
                 else if ( ((UnoCard) playerdeck.get(choiceIndex)).canPlace(topCard, currentColor) ) {
                     topCard = (UnoCard) playerdeck.get(choiceIndex);
                     playerdeck.remove(choiceIndex);
                     currentColor = topCard.color;

                     //SPECIAL CARDS
                     if (topCard.value >= 10)
                     {
                         playersTurn = false; // Skipping turn

                         switch (topCard.value)
                         {
                             case 12: // Draw 2
                                 txtUpdate.setText("Drawing 2 cards...");
                                 draw(2,compdeck);
                                 break;

                             case 13: case 14: // Wild cards
                             do{ // Repeats every time the user doesn't input a valid color
                                 System.out.print("\nEnter the color you want: ");
                                 input = new Scanner(System.in);
                             } while (!input.hasNext("R..|r..|G....|g....|B...|b...|Y.....|y.....") );
                             if (input.hasNext("R..|r..") )
                                 currentColor = "Red";
                             else if (input.hasNext("G....|g....") )
                                 currentColor = "Green";
                             else if (input.hasNext("B...|b...") )
                                 currentColor = "Blue";
                             else if (input.hasNext("Y.....|y.....") )
                                 currentColor = "Yellow";

                             txtUpdate.setText("You chose " + currentColor);

                             // Wild draw 4
                             if (topCard.value == 14){
                                 compdeck1.setText("Drawing 4 cards...");
                                 draw(4,compdeck);
                             }
                             break;
                         }
                     }
                 } else txtUpdate.setText("Invalid choice. Turn skipped.");
             }

             //COMPUTERS TURN
             else{
                 // Finding a card to place
                 compdeck1.setText(("My turn! I have " + String.valueOf(compdeck.size() ) + " cards left!" + ((compdeck.size() == 1) ? "...Uno!":"") ));
         		for (choiceIndex = 0; choiceIndex < compdeck.size(); choiceIndex++){
                     // Searching for playable cards
                     if ( ((UnoCard) compdeck.get(choiceIndex)).canPlace(topCard, currentColor) )
                         break;
                 }

                 if (choiceIndex == compdeck.size() ) {
                 	compdeck1.setText(("I've got nothing! Drawing cards..."));
                     draw(1,compdeck);
                 }
                 else{
                     topCard = (UnoCard) compdeck.get(choiceIndex);
                     compdeck.remove(choiceIndex);
                     currentColor = topCard.color;
                     compdeck1.setText(("I choose " + topCard.getFace() + " !"));

                     // Must do as part of each turn because topCard can stay the same through a round
                     if (topCard.value >= 10) {
                         playersTurn = true; // Skipping turn

                         switch (topCard.value) {
                             case 12: // Draw 2
                             	compdeck1.setText("Drawing 2 cards for you...");
                                 draw(2,playerdeck);
                                 break;

                             case 13: case 14: // Wild cards

                             do{ // Searching for playable cards
                                 currentColor = new UnoCard().color;
                             } while (currentColor == "none");

                             System.out.println("New color is " + currentColor);
                             if (topCard.value == 14){ // Wild draw 4
                             	compdeck1.setText("Drawing 4 cards for you...");
                                 draw(4,playerdeck);
                             }
                             break;
                         }
                     }
                 }

                 // If decks are empty
                 if (playerdeck.size() == 0)
                     win = 1;
                 else if (compdeck.size() == 0)
                     win = -1;
             }

         } // turns loop end
	}
	public void compPlays() {
		compdeck1.setText(("My turn! I have " + String.valueOf(compdeck.size() ) + " cards left!" + ((compdeck.size() == 1) ? "...Uno!":"") ));
		for (choiceIndex = 0; choiceIndex < compdeck.size(); choiceIndex++){
            // Searching for playable cards
            if ( ((UnoCard) compdeck.get(choiceIndex)).canPlace(topCard, currentColor) )
                break;
        }

        if (choiceIndex == compdeck.size() ) {
        	compdeck1.setText(("I've got nothing! Drawing cards..."));
            draw(1,compdeck);
        }
        else{
            topCard = (UnoCard) compdeck.get(choiceIndex);
            compdeck.remove(choiceIndex);
            currentColor = topCard.color;
            compdeck1.setText(("I choose " + topCard.getFace() + " !"));

            // Must do as part of each turn because topCard can stay the same through a round
            if (topCard.value >= 10) {
                playersTurn = true; // Skipping turn

                switch (topCard.value) {
                    case 12: // Draw 2
                    	compdeck1.setText("Drawing 2 cards for you...");
                        draw(2,playerdeck);
                        break;

                    case 13: case 14: // Wild cards

                    do{ // Searching for playable cards
                        currentColor = new UnoCard().color;
                    } while (currentColor == "none");

                    System.out.println("New color is " + currentColor);
                    if (topCard.value == 14){ // Wild draw 4
                    	compdeck1.setText("Drawing 4 cards for you...");
                        draw(4,playerdeck);
                    }
                    break;
                }
            }
        }
	}
	public void endGame(){
		 // If decks are empty
        if (playerdeck.size() == 0)
            win = 1;
        else if (compdeck.size() == 0)
            win = -1;
	}
	public void newGame() {
		playerdeck.clear();
		compdeck.clear();
		win = 0;
		topCard = new UnoCard();
		currentColor = topCard.color;
		System.out.println("\nWelcome to Uno! Initialising decks...");
		draw(7, playerdeck);
		draw(7, compdeck);
        topCard = new UnoCard();
        current.setText(topCard.getFace()); //this is causing error
        //current.add(topCard); //help?
        currentColor = topCard.color;
        currentDeck = 1; //tracks who is playing
        playersTurn = true;
        choiceIndex=1;
        playGame();
	}
	
	public static void userPlays(){
		//
	}
}

