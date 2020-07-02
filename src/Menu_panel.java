import java.awt.Color;

import javax.swing.JPanel;

class Menu_panel extends JPanel {
	
	Menu_panel(int board_x, int board_y) {

		Color dark_grey = new Color(102, 102, 102);
		
		setBackground(dark_grey);
		setSize(100, board_x);

		setBounds(0, board_y, board_x, 100);
	}
	
}