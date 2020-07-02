import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Grid_frame extends JFrame {
	Boolean was_paused = true;
	
	int board_x = 1800;
	int board_y = 900;
	int window_y = board_y + 100;
	
	Grid_canvas canvas = new Grid_canvas();
	Menu_panel menu = new Menu_panel(board_x, board_y);
	
	//Adds key listeners
	Grid_frame() {
		
		//Initializes the frame 'settings'
		setTitle("Conway's Game of Life");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(board_x + 16, window_y + 39);
		setLocation(75, 75);
		setVisible(true);
		setLayout(null);
		setResizable(false);
		
		
		//Create and add the buttons
		//add_buttons(menu);
		
		//Add board and menu panels
		add(canvas);
		add(menu);
		
		canvas.create_towns();
		canvas.populate_towns();
		canvas.save_town();
		canvas.start_timer();

		addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				
				//Pause the simulation
				case KeyEvent.VK_P:
					
					if (canvas.is_running()) {
						canvas.stop_timer();
						
					} else {
						canvas.start_timer();
						
					}
					
					break;
					
				//Randomly fill grid to make a new board
				case KeyEvent.VK_N:
					canvas.populate_towns();
					canvas.save_town();
					canvas.start_timer();
					
					break;
				
				//A help window pops up with key commands
				case KeyEvent.VK_H:
					help_window();
					
					break;
				
				//Closes the simulation
				case KeyEvent.VK_ESCAPE:
					System.exit(0);
					break;
					
				//If paused, it increments the simulation one generation
				case KeyEvent.VK_I:
					if (!canvas.is_running()) {
						canvas.get_next_generation();
						repaint();
					}
					
					break;
					
				//Will create a blank board and pause the simulation
				case KeyEvent.VK_C:
					canvas.stop_timer();
					canvas.create_towns();
					repaint();
					
					break;
					
				//Will save the current board
				case KeyEvent.VK_S:
					was_paused = true;
					
					if (canvas.is_running()) {
						canvas.stop_timer();
						was_paused = false;
					}
					
					canvas.save_town();
					
					if (!was_paused) {
						canvas.start_timer();
					}
					
					break;
					
				//Will restart from saved board and pause if it was paused before
				case KeyEvent.VK_R:
					was_paused = true;
					
					if (canvas.is_running()) {
						canvas.stop_timer();
						was_paused = false;
					}
					
					canvas.restart_town();
					repaint();
					
					if (!was_paused) {
						canvas.start_timer();
					}
					
					break;
				
				//Increase simulation speed
				case KeyEvent.VK_RIGHT:
					canvas.dec_timer();
					
					break;
					
				//Decrease simulation timer
				case KeyEvent.VK_LEFT:
					canvas.inc_timer();
					
					break;
					
				//Set a new random color
				case KeyEvent.VK_D:
					canvas.set_random_color();
					
					break;
					
				//Set color back to green
				case KeyEvent.VK_G:
					canvas.set_green();
					
					break;
					
				//Save the board to a file
				case KeyEvent.VK_K:
					save_file();
					
					break;
				
				//Load a file
				case KeyEvent.VK_L:
					load_file();
					
					break;
					
				}
			}
		});
		
		//Looks for a mouse click then adds a new town where clicked
		addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    		int mouse_x = e.getX();
		    		int mouse_y = e.getY();
		    		
		    		canvas.add_new_town(mouse_x, mouse_y);
		    }
		});
		
		
		
	}
	
	//Function to create the help window
	public void help_window() {
		canvas.stop_timer();
		
		JFrame help_frame = new JFrame();
		
		JButton button = new JButton("Close");
		
		//Creates a panel for the help messages and sets the layout
		JPanel label_panel = new JPanel();
		label_panel.setLayout(new GridLayout(0, 1));
		
		//An array holding the help messages
		String[] help_items = {"H - Help menu", "N - New board", "P - Pause simulation", "I - Increment simulation", "S - Save current board", 
								"C - Clear board", "R - Restart", "R Arrow - Increase speed", "L Arrow - Decrease speed", "ESC - Close window",
								"D - Toggle random color", "G - Color to green", "K - Save board to file", "L - Load from file"};
		
		//Loops through to create labels and adds them to the panel
		for (int i = 0; i < help_items.length; i += 1) {
			String str = help_items[i];
			
			JLabel label = new JLabel(str);
			label.setHorizontalAlignment(JLabel.CENTER);
			label_panel.add(label);
		}
         
		//Adds close button and panel
		help_frame.add(label_panel, BorderLayout.CENTER);
		help_frame.add(button, BorderLayout.SOUTH);
		
		help_frame.repaint();
		
		//Watches for close button to be clicked
		button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		       help_frame.dispose();
		       canvas.start_timer();
		    }
		});
		
		help_frame.setVisible(true);
	}
	
	//Load board window
	public void load_file() {
		Boolean was_running = canvas.is_running();
		
		canvas.stop_timer();
		JFrame load_frame = new JFrame();
		
		File file = new File("C:\\Users\\jeremiah.koenig\\eclipse-workspace\\Conways_life");
		String[] files = file.list();
		ArrayList<String> good_files = new ArrayList<>();
		
		//Finds all files that end in .txt
		for (String f: files) {
			if (f.indexOf(".txt") > -1) {
				good_files.add(f);
			}
		}
		
		//Sets the frame for the load window
		int window_height_base = 75;
		int window_height = window_height_base + (25 * good_files.size());
		
		load_frame.setSize(200, window_height);
		load_frame.setTitle("Load");
		load_frame.setLocation(550, 400);
		
		//Creates a panel for the text box and sets the layout
		JPanel label_panel = new JPanel();
		label_panel.setLayout(new GridLayout(0, 1));	
		
		//Create a label and a text box
		JLabel label = new JLabel("Pick a file:");
		label.setHorizontalAlignment(JLabel.CENTER);
		
		//Adds label and text box
		label_panel.add(label);	
		
		//Loop through available files and then create/add the buttons
		for (String board: good_files) {
			JButton button = new JButton(board);
			label_panel.add(button);
			
			//Watches for close button to be clicked
			button.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e)
			    {
					
					File file;
					
					try {
						file = new File(button.getText());
						Scanner scan = new Scanner(file);
						
						canvas.load_from_file(scan);
						if (was_running) {
							canvas.start_timer();
						}
						
						load_frame.dispose();
						
					} catch (FileNotFoundException ex) {
						
					} 
			       
			    }
			    
			});
			
		}
         
		//Adds submit button and panel
		load_frame.add(label_panel, BorderLayout.CENTER);
		
		load_frame.repaint();
		label_panel.repaint();
		
		load_frame.setVisible(true);
	}
	
	//Frame to save a board to file
	public void save_file() {
		Boolean was_running = canvas.is_running();
		canvas.stop_timer();
		
		//Creates the frame and panel
		JFrame save_frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		
		//Sets the frame for the save window
		save_frame.setSize(200, 125);
		save_frame.setTitle("Save");
		save_frame.setLocation(550, 400);
		
		//Creates the label and text box
		JLabel label = new JLabel("Enter the file name");
		label.setHorizontalAlignment(JLabel.CENTER);
		JTextField filename = new JTextField();
		
		//Create button and button listener
		JButton button = new JButton("Submit");
		button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
				
		    	String name = filename.getText();
				
				try {
					File new_board = new File(name);
					if (new_board.createNewFile()) {
						canvas.save_to_file(name);
						
						if (was_running) {
							canvas.start_timer();
						}
						
						save_frame.dispose();
					} else {
						System.out.println("That file already exists");
					}
				} catch (IOException ex) {
					
				}
		    }
		});
		
		//Add the label and text box to the frame
		panel.add(label);
		panel.add(filename);
		
		//Adds submit button and panel
		save_frame.add(panel, BorderLayout.CENTER);
		save_frame.add(button, BorderLayout.SOUTH);		
		
		save_frame.repaint();
		save_frame.repaint();
				
		save_frame.setVisible(true);
		
	}
	
	//Create the grid
	public void create_towns() {
		canvas.create_towns();
	}
	
	//Randomly fill square in the grid
	public void populate_towns() {
		canvas.populate_towns();
		canvas.save_town();
	}
	
	public void add_buttons(Menu_panel panel) {
		JButton pause_button = new JButton("Pause");
		pause_button.setVisible(true);
		
		pause_button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {

				if (canvas.is_running()) {
					canvas.stop_timer();
					pause_button.setText("  Play  ");
					
				} else {
					canvas.start_timer();
					pause_button.setText("Pause");
					
				}
		    }
		});
		
		panel.add(pause_button);
		panel.repaint();
	}
	
}