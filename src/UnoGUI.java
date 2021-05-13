import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class UnoGUI extends JFrame{
    JFrame frame = new JFrame("Uno");
    private JButton drawButton;
    private JButton playAgain;
    private JTextArea cDeck;
    private JTextArea current;
    private JTextField selectNum;
    private JLabel enter;
    private JTextArea pDeck;

    public ArrayList<UnoCard> playerdeck = new ArrayList<UnoCard>();
    public ArrayList<UnoCard> compdeck = new ArrayList<UnoCard>();
    public boolean playersTurn;
    public int win;
    public int currentDeck;
    Scanner input;
    UnoCard topCard; // card on top of the "pile"
    int choiceIndex; // Index of chosen card for both player and computer
    String currentColor; // Mainly used for wild cards

    public UnoGUI() {
        setTitle("UNO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton drawButton = new JButton("Draw Card");
        JButton playAgain = new JButton("Restart");
        JTextArea cDeck = new JTextArea(" ", 11, 10);
        JTextArea current = new JTextArea(" ", 1, 21);
        JLabel enter = new JLabel("Enter number:  ");
        JTextField selectNum = new JTextField(10);
        JTextArea pDeck = new JTextArea(" ", 10, 10);

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        panel.setBorder(BorderFactory.createTitledBorder("Player"));
        panel2.setBorder(BorderFactory.createTitledBorder("Top Card"));
        panel3.setBorder(BorderFactory.createTitledBorder("Computer"));

        panel.setBackground(Color.gray);
        panel2.setBackground(Color.gray);
        panel3.setBackground(Color.gray);

        BoxLayout layout1 = new BoxLayout(panel, BoxLayout.Y_AXIS);
        BoxLayout layout2 = new BoxLayout(panel2, BoxLayout.Y_AXIS);
        BoxLayout layout3 = new BoxLayout(panel3, BoxLayout.Y_AXIS);
        panel.setLayout(layout1);
        panel2.setLayout(layout2);
        panel3.setLayout(layout3);

        pDeck.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(pDeck);
        panel.add(enter);
        panel.add(selectNum);

        current.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel2.add(current);
        panel2.add(drawButton);

        cDeck.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgain.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel3.add(cDeck);
        panel3.add(playAgain);

        frame.setLayout(new FlowLayout());
        frame.add(panel);
        frame.add(panel3);
        frame.add(panel2);
        frame.pack();

        frame.addWindowListener(new WindowAdapter() {
            public void windowOpening(WindowEvent windowEvent) {
                newGame();
            }
        });

        drawButton.addActionListener(new drawCard());
        playAgain.addActionListener(new playAgain());
        frame.setSize(500, 350);
        frame.setVisible(true);
        frame.setResizable(false);
        newGame();

    }

    private class drawCard implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (currentDeck == 1) {
                draw(1, playerdeck); //user needs to draw
                currentDeck = 0; //computers turn
            } else {//computer needs to draw
                draw(1, compdeck);
                currentDeck = 1;
            }
        }
    }

    private class playAgain implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // clear and restart;
            newGame();
        }
    }

    public static void main(String[] args) {
        UnoGUI design = new UnoGUI();
    }

    public static void draw(int cards, ArrayList<UnoCard> deck) {
        for (int i = 0; i < cards; i++)
            deck.add(new UnoCard());
    }

    public void playGame() {
        gameLoop:
        
        for (boolean playersTurn = true; win == 0; playersTurn ^= true) {
            choiceIndex = 0;
            current.setText("\nThe top card is: " + topCard.getFace());

            //PLAYERS TURN
            if (playersTurn) { // Displaying user's deck
                pDeck.setText("Your turn!");
                //TAKING TURNS
                if (choiceIndex == playerdeck.size())
                    draw(1, playerdeck); //how to implement draw button?
                else if (choiceIndex == playerdeck.size() + 1)
                    break gameLoop;
                else if (((UnoCard) playerdeck.get(choiceIndex)).canPlace(topCard, currentColor)) {
                    topCard = (UnoCard) playerdeck.get(choiceIndex);
                    playerdeck.remove(choiceIndex);
                    currentColor = topCard.color;

                    //SPECIAL CARDS
                    if (topCard.value >= 10) {
                        playersTurn = false; // Skipping turn

                        switch (topCard.value) {
                            case 12: // Draw 2
                                pDeck.setText("Drawing 2 cards...");
                                draw(2, compdeck);
                                break;

                            case 13:
                            case 14: // Wild cards
                                do { // Repeats every time the user doesn't input a valid color
                                    System.out.print("\nEnter the color you want: ");
                                    input = new Scanner(System.in);
                                } while (!input.hasNext("R..|r..|G....|g....|B...|b...|Y.....|y....."));
                                if (input.hasNext("R..|r.."))
                                    currentColor = "Red";
                                else if (input.hasNext("G....|g...."))
                                    currentColor = "Green";
                                else if (input.hasNext("B...|b..."))
                                    currentColor = "Blue";
                                else if (input.hasNext("Y.....|y....."))
                                    currentColor = "Yellow";

                                pDeck.setText("You chose " + currentColor);

                                // Wild draw 4
                                if (topCard.value == 14) {
                                    cDeck.setText("Drawing 4 cards...");
                                    draw(4, compdeck);
                                }
                                break;
                        }
                    }
                } else pDeck.setText("Invalid choice. Turn skipped.");
            }

            //COMPUTERS TURN
            else {
                // Finding a card to place
                cDeck.setText(("My turn! I have " + String.valueOf(compdeck.size()) + " cards left!" + ((compdeck.size() == 1) ? "...Uno!" : "")));
                for (choiceIndex = 0; choiceIndex < compdeck.size(); choiceIndex++) {
                    // Searching for playable cards
                    if (((UnoCard) compdeck.get(choiceIndex)).canPlace(topCard, currentColor))
                        break;
                }
                System.out.print(choiceIndex);
                System.out.print(compdeck.size());
                if (choiceIndex+1 == compdeck.size()) {
                    cDeck.setText(("I've got nothing! Drawing cards..."));
                    draw(1, compdeck);
                } else {
                    topCard = (UnoCard) compdeck.get(choiceIndex);
                    compdeck.remove(choiceIndex);
                    currentColor = topCard.color;
                    cDeck.setText(("I choose " + topCard.getFace() + " !"));

                    // Must do as part of each turn because topCard can stay the same through a round
                    if (topCard.value >= 10) {
                        playersTurn = true; // Skipping turn

                        switch (topCard.value) {
                            case 12: // Draw 2
                                cDeck.setText("Drawing 2 cards for you...");
                                draw(2, playerdeck);
                                break;

                            case 13:
                            case 14: // Wild cards

                                do { // Searching for playable cards
                                    currentColor = new UnoCard().color;
                                } while (currentColor == "none");

                                System.out.println("New color is " + currentColor);
                                if (topCard.value == 14) { // Wild draw 4
                                    cDeck.setText("Drawing 4 cards for you...");
                                    draw(4, playerdeck);
                                }
                                break;
                        }
                    }
                }
             
            }
         // If decks are empty
            if (playerdeck.size() == 0) {
            	 win = 1;
            	current.setText("Congrats! You win!");
            }
            else if (compdeck.size() == 0) {
            
            	win=-1;
            	current.setText("You lose. Try again?");
            }

        } // turns loop end

    }


    public void newGame() {
        playerdeck.clear();
        compdeck.clear();
        win = 0;
        topCard = new UnoCard();
        currentColor = topCard.color;
       // System.out.println("\nWelcome to Uno! Initialising decks...");
        draw(7, playerdeck);
        draw(7, compdeck);
        topCard = new UnoCard();
        current.setText(topCard.getFace()); //this is causing error
        //current.add(topCard); //help?
        currentColor = topCard.color;
        currentDeck = 1; //tracks who is playing
        playersTurn = true;
        choiceIndex = 1;
        playGame();
    }


    class CustomWindowListener implements WindowListener {
        public void windowOpened(WindowEvent e) {
            newGame();
        }
        public void windowClosing(WindowEvent e) {
        }
        public void windowClosed(WindowEvent e) {
        }
        public void windowIconified(WindowEvent e) {
        }
        public void windowDeiconified(WindowEvent e) {
        }
        public void windowActivated(WindowEvent e) {
        }
        public void windowDeactivated(WindowEvent e) {
        }
    }



}

