import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class UnoGUI extends JFrame{
	JFrame frame = new JFrame("Uno");
	private JButton drawButton;
	private JButton placeButton;
	private JButton playAgain;
	private JComboBox p1;
	private JTextField compdeck;
	private JTextField current;
	private JLabel cardholder;

	public UnoGUI(){
		setTitle("UNO");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton drawButton = new JButton("Draw");
		JButton placeButton = new JButton("Place");
		JButton playAgain = new JButton("Restart");
		JTextField compdeck = new JTextField(5);
		JTextField current = new JTextField(10);
		JLabel cardholder = new JLabel("Drop Down Here");
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

		cardholder.setAlignmentX(Component.CENTER_ALIGNMENT);
		placeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(cardholder);
		panel.add(placeButton);

		current.setAlignmentX(Component.CENTER_ALIGNMENT);
		drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel2.add(current);
		panel2.add(drawButton);

		compdeck.setAlignmentX(Component.CENTER_ALIGNMENT);
		playAgain.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel3.add(compdeck);
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

	}
	private class drawCard implements ActionListener{
		public void actionPerformed(ActionEvent event) {
		//	draw(1,playersdeck);
			System.out.println('d');
		}
	}
	private class placeCard implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			//	place card;
			System.out.println('p');
		}
	}
	private class playAgain implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			// clear and restart;
			System.out.println('r');
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
