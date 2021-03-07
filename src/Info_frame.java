import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Info_frame extends JFrame{
	
	JLabel main_label;
	RoundedButton got_it = new RoundedButton("Lets Go!");
	JPanel main_panel;
	
	//Text field to hold the information
	JTextPane information = new JTextPane();
	
	//Font for the text area
	Font text_font = new Font("Consolas", Font.PLAIN, 16);
	Font label_font = new Font("Consolas", Font.BOLD, 35);
	

	Color background = new Color(242, 243, 244);
	
	Info_frame(int height) {

		setLocation(900, 125);
		setSize(500, (int) (height * .75));
		setTitle("Help Desk");
		setLayout(new BorderLayout());
		
		//Add space around the edges and the main center panel that the info will go in
		JPanel border_north = new JPanel();
		border_north.setBackground(background);
		border_north.setPreferredSize(new Dimension(20, 20));
		add(border_north, BorderLayout.NORTH);
		
		JPanel border_east = new JPanel();
		border_east.setBackground(background);
		border_east.setPreferredSize(new Dimension(20, 20));
		add(border_east, BorderLayout.EAST);
		
		JPanel border_south = new JPanel();
		border_south.setBackground(background);
		border_south.setPreferredSize(new Dimension(20, 20));
		add(border_south, BorderLayout.SOUTH);
		
		JPanel border_west = new JPanel();
		border_west.setBackground(background);
		border_west.setPreferredSize(new Dimension(20, 20));
		add(border_west, BorderLayout.WEST);
		
		//Set the center panel
		main_panel = new JPanel();
		main_panel.setLayout(new BorderLayout());
		add(main_panel, BorderLayout.CENTER);
		
		//Set the main label
		main_label = new JLabel();
		main_label.setFont(label_font);
		main_label.setText("Important Notes");
		main_label.setHorizontalAlignment(JLabel.CENTER);
		main_label.setPreferredSize(new Dimension(0, 100));
		main_label.setBackground(background);
		
		//Reads in the file and adds the text to the text field
		File file = new File("Game_of_life_info.txt");
		Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String all_text = "";
		while(scan.hasNextLine()) {
			String text = scan.nextLine();
			all_text = all_text + text + "\n";
		}
		
		//Set font and alignment for the text pane
		StyledDocument doc = information.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		information.setFont(text_font);
		information.setEditable(false);
		
		//Set the read in text to the text pane
		information.setText(all_text);
		
		//Set background to button and add function
		got_it.setBackground(new Color(145, 163, 176));
		got_it.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
			
		});
		
		information.setBackground(background);
		
		//Add the components to the main panel
		main_panel.add(main_label, BorderLayout.NORTH);
		main_panel.add(information, BorderLayout.CENTER);
		main_panel.add(got_it, BorderLayout.SOUTH);
		
		setVisible(true);
		
	}
}
