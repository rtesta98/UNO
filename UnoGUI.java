import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class UnoGUI extends JFrame{
	//JFrame frame = new JFrame("Uno");
	//JPanel panel = new JPanel();
	//private JLabel drawLabel;
	private JButton drawButton;
	private JButton playAgain;
	private JLabel title;
	
	public UnoGUI(){
		setTitle("UNO");
		Container frame = getContentPane();
		frame.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		
		JButton drawButton = new JButton("Draw");
	    drawButton.addActionListener(new drawCard());
	    //panel.add(dealbutton);
	    
		//drawLabel = new JLabel("Draw from Pile", JLabel.SOUTH);
	  // panel.add(drawLabel);
	    
		title = new JLabel("UNO Championships", JLabel.CENTER);
		title.setFont(new Font("Serif", Font.BOLD, 50));
		
		frame.add(title);
		frame.add(panel);
		frame.add(drawButton);
	    
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500,700);
		setVisible(true);

	}
	private class drawCard implements ActionListener{ 
		public void actionPerformed(ActionEvent event) {
		//	draw(1,playersdeck);
			System.out.println('d');
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
}
