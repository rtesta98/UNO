import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class UnoGUI extends JFrame{
    JFrame frame = new JFrame("Uno");
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
    Scanner input;
    UnoCard topCard; // card on top of the "pile"
    int choiceIndex; // Index of chosen card for both player and computer
    String currentColor; // Mainly used for wild cards
    public String savedStr;
    public int savedNum;

    public UnoGUI() {
        setTitle("UNO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.RED);
        playAgain = new JButton("Restart");
        cDeck = new JTextArea("", 11, 10);
        cDeck.setText("\nWelcome to Uno! You go first.");
        current = new JTextArea("", 1, 21);
        enter = new JLabel("Enter number:         ");
        selectNum = new JTextField(10);
        pDeck = new JTextArea("", 10, 10);
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
        panel2.add(current);

        cDeck.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgain.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel3.add(cDeck);
        panel3.add(playAgain);

        frame.setLayout(new FlowLayout());
        frame.add(panel);
        frame.add(panel3);
        frame.add(panel2);
        frame.pack();


        selectNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                savedStr = selectNum.getText();
                savedNum = Integer.parseInt(savedStr);
                choiceIndex = savedNum - 1;
                //TAKING TURNS
                if (choiceIndex == playerdeck.size())
                    draw(1, playerdeck);
                else if (choiceIndex == playerdeck.size() + 1)
                    System.exit(0);
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
                playersTurn = false;


                //COMPUTERS TURN
                String res2 = "";
                res2 += ("My turn! I have " + String.valueOf(compdeck.size())
                        + " cards left! \n" + ((compdeck.size() == 2) ? "...Uno!" : ""));
                // Finding a card to place
                for (choiceIndex = 0; choiceIndex < compdeck.size(); choiceIndex++) {
                    // Searching for playable cards
                    if (((UnoCard) compdeck.get(choiceIndex)).canPlace(topCard, currentColor))
                        break;
                }

                if (choiceIndex == compdeck.size()) {
                    res2 += ("I've got nothing! Drawing cards...");
                    draw(1, compdeck);
                } else {
                    topCard = (UnoCard) compdeck.get(choiceIndex);
                    compdeck.remove(choiceIndex);
                    currentColor = topCard.color;
                    res2 += ("I choose " + topCard.getFace() + " !");

                    // Must do as part of each turn because topCard can stay the same through a round
                    if (topCard.value >= 10) {
                        playersTurn = true; // Skipping turn

                        switch (topCard.value) {
                            case 12: // Draw 2
                                res2 += ("Drawing 2 cards for you...");
                                draw(2, playerdeck);
                                break;

                            case 13:
                            case 14: // Wild cards

                                do { // Searching for playable cards
                                    currentColor = new UnoCard().color;
                                } while (currentColor == "none");

                                res2 += ("New color is " + currentColor);
                                if (topCard.value == 14) { // Wild draw 4
                                    cDeck.setText("Drawing 4 cards for you...");
                                    draw(4, playerdeck);
                                }
                        }
                    }
                    cDeck.setText(res2);
                }
                // If decks are empty
                if (playerdeck.size() == 0)
                    win = 1;
                else if (compdeck.size() == 0)
                    win = -1;

                choiceIndex = 0;
                current.setText(topCard.getFace() ); //changed above to initailize for now


                //PLAYERS TURN
                String res = "";
                res += ("Your turn! Your choices:\n");
                for (int i = 0; i < playerdeck.size(); i++) {
                    res += (String.valueOf(i + 1) + ". " +
                            ((UnoCard) playerdeck.get(i)).getFace() + "\n");
                }
                res += (String.valueOf(playerdeck.size() + 1) + ". " + "Draw card" + "\n" +
                        String.valueOf(playerdeck.size() + 2) + ". " + "Quit");
                // Repeats every time the user doesn't input a number
                res += ("\nPlease input the number of your choice: ");
                pDeck.setText(res);

                //RESULTS
                if (win == 1)
                    pDeck.setText("You win :)");
                if (win == -1)
                    pDeck.setText("You lose :(");


                selectNum.setText("");
            }
        });

        playAgain.addActionListener(new playAgain());
        frame.setSize(700, 400);
        frame.setVisible(true);
        frame.setResizable(false);
        playGame();


    }

    class playAgain implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // clear and restart;
            playGame();
            cDeck.setText("I am drawing. It's your turn!");

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
        playerdeck.clear();
        compdeck.clear();
        win = 0;
        topCard = new UnoCard();
        currentColor = topCard.color;
        current.setText(topCard.getFace());

        draw(7, playerdeck);
        draw(7, compdeck);

        //TURNS
        choiceIndex = 0;


        //PLAYERS TURN
        String res = "";
        res += ("Your turn! Your choices:\n");
        for (int i = 0; i < playerdeck.size(); i++) {
            res += (String.valueOf(i + 1) + ". " +
                    ((UnoCard) playerdeck.get(i)).getFace() + "\n");
        }
        res += (String.valueOf(playerdeck.size() + 1) + ". " + "Draw card" + "\n" +
                String.valueOf(playerdeck.size() + 2) + ". " + "Quit");
        // Repeats every time the user doesn't input a number

        res += ("\nPlease input the number of your choice: ");
        pDeck.setText(res);


    } // game loop end

}
