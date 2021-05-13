import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class UnoGUI extends JFrame{
    JFrame frame = new JFrame("Uno");
    public JButton drawButton;
    public JButton playAgain;
    public JTextArea cDeck;
    public JTextArea current;
    public JTextField selectNum;
    public JLabel enter;
    public JTextArea pDeck;

    public ArrayList<UnoCard> playerdeck = new ArrayList<UnoCard>();
    public ArrayList<UnoCard> compdeck = new ArrayList<UnoCard>();
    public boolean playersTurn;
    public int win;
    //public int currentDeck;
    Scanner input;
    UnoCard topCard; // card on top of the "pile"
    int choiceIndex; // Index of chosen card for both player and computer
    String currentColor; // Mainly used for wild cards
    public String savedStr;
    public int savedNum;

    public UnoGUI() {
        setTitle("UNO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawButton = new JButton("Draw Card");
        playAgain = new JButton("Restart");
        cDeck = new JTextArea(" ", 11, 10);
        cDeck.setText("\nWelcome to Uno! Initialising decks...");
        current = new JTextArea("[Yellow 3] ", 1, 21);
        enter = new JLabel("Enter number:  ");
        selectNum = new JTextField(10);
        pDeck = new JTextArea(" ", 10, 10);
        pDeck.setText("Not your turn");

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

      //  frame.addWindowListener(new WindowAdapter() {
         //   public void windowOpening(WindowEvent e) {
       //         playGame();
       //     }
       // });

        selectNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savedStr = selectNum.getText();
            }
        });
        drawButton.addActionListener(new drawCard());
        playAgain.addActionListener(new playAgain());
        frame.setSize(600, 350);
        frame.setVisible(true);
        playGame();


    }

    private class drawCard implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (playersTurn) {
                draw(1, playerdeck); //user needs to draw
                //currentDeck = 0; //computers turn
            } else {//computer needs to draw
                draw(1, compdeck);
              //  currentDeck = 1;
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

    public void playGame(){
        gameLoop:
        while (true) {
            playerdeck.clear();
            compdeck.clear();
            win = 0;
            topCard = new UnoCard();
            currentColor = topCard.color;


            //cDeck.setText("\nWelcome to Uno! Initialising decks...");
            draw(7, playerdeck);
            draw(7, compdeck);

            //TURNS
            for (boolean playersTurn = true; win == 0; playersTurn ^= true) {
                choiceIndex = 0;
                //current.setText("The top card is: " + topCard.getFace() ); //changed above to initailize for now


                //PLAYERS TURN
                if (playersTurn) { // Displaying user's deck
                	System.out.println("d");
                    pDeck.setText("Your turn! Your choices:"); //causes error !!!!!!!!
                    for (int i = 0; i < playerdeck.size(); i++) {
                        pDeck.setText(String.valueOf(i + 1) + ". " +
                                ((UnoCard) playerdeck.get(i)).getFace() + "\n");
                    }
                    pDeck.setText(String.valueOf(playerdeck.size() + 1) + ". " + "Draw card" + "\n" +
                            String.valueOf(playerdeck.size() + 2) + ". " + "Quit");
                    // Repeats every time the user doesn't input a number

                    pDeck.setText("\nPlease input the number of your choice: ");
                    savedNum = Integer.parseInt(savedStr);
                    choiceIndex = savedNum - 1;

                //TAKING TURNS
                if (choiceIndex == playerdeck.size())
                    draw(1, playerdeck);
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
                                    pDeck.setText("\nEnter the color you want: ");
                                } while (savedStr.indexOf("R..|r..|G....|g....|B...|b...|Y.....|y.....") == 1);
                                if (savedStr.indexOf("R..|r..") == 1)
                                    currentColor = "Red";
                                else if (savedStr.indexOf("G....|g....") == 1)
                                    currentColor = "Green";
                                else if (savedStr.indexOf("B...|b...") == 1)
                                    currentColor = "Blue";
                                else if (savedStr.indexOf("Y.....|y.....") == 1)
                                    currentColor = "Yellow";

                                pDeck.setText("You chose " + currentColor);

                                // Wild draw 4
                                if (topCard.value == 14) {
                                    pDeck.setText("Drawing 4 cards...");
                                    draw(4, compdeck);
                                }
                                break;
                            }
                        }
                    } else pDeck.setText("Invalid choice. Turn skipped.");
                playersTurn=false;
                }

                //COMPUTERS TURN
                else{
                    cDeck.setText("My turn! I have " + String.valueOf(compdeck.size() )
                            + " cards left!" + ((compdeck.size() == 1) ? "...Uno!":"") );
                    // Finding a card to place
                    for (choiceIndex = 0; choiceIndex < compdeck.size(); choiceIndex++){
                        // Searching for playable cards
                        if ( ((UnoCard) compdeck.get(choiceIndex)).canPlace(topCard, currentColor) )
                            break;
                    }

                    if (choiceIndex == compdeck.size() ) {
                        cDeck.setText("I've got nothing! Drawing cards...");
                        draw(1,compdeck);
                    }
                    else{
                        topCard = (UnoCard) compdeck.get(choiceIndex);
                        compdeck.remove(choiceIndex);
                        currentColor = topCard.color;
                        cDeck.setText("I choose " + topCard.getFace() + " !");

                        // Must do as part of each turn because topCard can stay the same through a round
                        if (topCard.value >= 10) {
                            playersTurn = true; // Skipping turn

                            switch (topCard.value) {
                                case 12: // Draw 2
                                    cDeck.setText("Drawing 2 cards for you...");
                                    draw(2,playerdeck);
                                    break;

                                case 13: case 14: // Wild cards

                                    do{ // Searching for playable cards
                                        currentColor = new UnoCard().color;
                                    } while (currentColor == "none");

                                    cDeck.setText("New color is " + currentColor);
                                    if (topCard.value == 14){ // Wild draw 4
                                        cDeck.setText("Drawing 4 cards for you...");
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

            //RESULTS
            if (win == 1)
                pDeck.setText("You win :)");
            else
                pDeck.setText("You lose :(");

        } // game loop end

        pDeck.setText("New Game?");
    }


    public void newGame() { //worth using? go straight to play?
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
      //  currentDeck = 1; //tracks who is playing
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
