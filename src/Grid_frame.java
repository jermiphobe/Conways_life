import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Grid_frame extends JFrame {
	Boolean was_paused = true;
	
	int board_x;
	int board_y;
	int menu_height = 280;
	
	int x_size = 120;
	int y_size = 60;
	
	JPanel main_canvas = new JPanel();
	Grid_canvas canvas;
	JPanel menu;

	Color button_color = new Color(132, 132, 130);
	Color background = new Color(242, 243, 244);
	
	//Adds key listeners
	Grid_frame() {
		//Get the size of the screen and set the size of the frame accordingly
		set_sizes();
		
		//Create the canvas
		canvas = new Grid_canvas(board_x, board_y, x_size, y_size);
		canvas.setPreferredSize(new Dimension(board_x,  board_y));
		
		//Add borders to the main canvas panel
		main_canvas = new JPanel();
		main_canvas.setBackground(background);
		main_canvas.setSize(board_x + 20, board_y + 10);
		main_canvas.setLayout(new BorderLayout());
		add_borders(main_canvas, false);
		main_canvas.add(canvas, BorderLayout.CENTER);
		
		//Initializes the frame 'settings'
		setTitle("Conway's Game of Life");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(board_x + 20, + board_y + menu_height + 40); // + 16, + 39
		setLocation(75, 75);
		setLayout(new BorderLayout());
		
		//Add board and menu panels
		add(main_canvas, BorderLayout.CENTER);
		
		//Set up the menu panel
		menu_window();
		add(menu, BorderLayout.SOUTH);
		
		setVisible(true);
		
		Info_frame info = new Info_frame(board_y + 30 + menu_height);
		
	}
	
	//Get the size of the screen and set the size of the frame accordingly
	public void set_sizes() {
		//Get the screen height to determine the frame size
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		double screen_width = screenSize.getWidth();
		
		//Calculate frame size
		board_x = (int) (screen_width * .8);
		int box = board_x / x_size; //Should get the necessary box side length
		board_x = box * x_size;
		board_y = box * y_size;
	}
	
	//Function to create a menu panel (button controls as opposed to keyboard controls)
	public void menu_window() {
		
		menu = new JPanel();
		menu.setBackground(background);
		
		//Sets the frame for the help window
		menu.setPreferredSize(new Dimension(600, menu_height));
		menu.setLayout(new BorderLayout());
		
		//Creat's the proper text for the button when you first open the menu
		String pause_button_text = "";
		if (canvas.is_running()) {
			pause_button_text = "Pause";
		} else {
			pause_button_text = "Play";
		}
		
		//Add borders to the menu panel
		add_borders(menu, true);
		
		//Create the main center panel
		JPanel center_menu = new JPanel();
		center_menu.setPreferredSize(new Dimension(100, menu_height));
		center_menu.setBackground(background);
		center_menu.setLayout(new GridLayout(0, 6, 10, 10));
		menu.add(center_menu, BorderLayout.CENTER);
		
		//Create and adds the pause/play button
		RoundedButton pause_button = new RoundedButton(pause_button_text);
		pause_button.setBackground(button_color);
		center_menu.add(pause_button);
		
		//Adds function to the play/pause button
		pause_button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	if (canvas.is_running()) {
					canvas.stop_timer();
					pause_button.setLabel("Play");
		    	} else {
		    		canvas.start_timer();
		    		pause_button.setLabel("Pause");
		    	}
		    	
		    }
		});
		
		//Create and add the increment simulation button
		RoundedButton increment_button = new RoundedButton("Increment");
		increment_button.setBackground(button_color);
		center_menu.add(increment_button);
		
		//Add functionality to the increment button
		increment_button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	if (!canvas.is_running()) {
					canvas.get_next_generation();
					repaint();
				}
		    	
		    }
		});
		
		//Create and add the change color button
		RoundedButton color_button = new RoundedButton("New Color");
		color_button.setBackground(button_color);
		center_menu.add(color_button);
		
		//Adds functionality to the color button
		color_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.set_random_color();
			}
			
		});
		
		//Create and add the reset to green button
		RoundedButton green_button = new RoundedButton("Color -> Yellow");
		green_button.setBackground(button_color);
		center_menu.add(green_button);
		
		//Add functionality to the green button
		green_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.set_green();
			}
			
		});
		
		//Create and add save file button
		RoundedButton save_button = new RoundedButton("Save to File");
		save_button.setBackground(button_color);
		center_menu.add(save_button);
		
		//Add functionality to the save button
		save_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				save_file();
			}
			
		});
		
		//Create and add load file button
		RoundedButton load_button = new RoundedButton("Load from File");
		load_button.setBackground(button_color);
		center_menu.add(load_button);
		
		//Add functionality to the load button
		load_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				load_file();
			}
			
		});
		
		//Create and add the clear board button
		RoundedButton clear_button = new RoundedButton("Clear Board");
		clear_button.setBackground(button_color);
		center_menu.add(clear_button);
		
		//Add functionality to the clear board button
		clear_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				pause_button.setLabel("Play");
				canvas.stop_timer();
				canvas.clear_board();
				repaint();
			}
			
		});
		
		//Create and add the new board button
		RoundedButton new_button = new RoundedButton("New Board");
		new_button.setBackground(button_color);
		center_menu.add(new_button);
		
		//Add functionality to the new board button
		new_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.populate_towns();
				canvas.save_town();
			}
			
		});
		
		//Create and add the save board button
		RoundedButton save_board_button = new RoundedButton("Save Board");
		save_board_button.setBackground(button_color);
		center_menu.add(save_board_button);
		
		//Add functionality to the save board button
		save_board_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				was_paused = true;
				
				if (canvas.is_running()) {
					canvas.stop_timer();
					was_paused = false;
				}
				
				canvas.save_town();
				
				if (!was_paused) {
					canvas.start_timer();
				}
			}
			
		});
		
		//Create and add the reset board button
		RoundedButton reset_button = new RoundedButton("Reset Board");
		reset_button.setBackground(button_color);
		center_menu.add(reset_button);
		
		//Add functionality to the reset board button
		reset_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				was_paused = true;
				
				if (canvas.is_running()) {
					canvas.stop_timer();
					was_paused = false;
				}
				
				canvas.restart_town(canvas.get_saved_list());
				repaint();
				
				if (!was_paused) {
					canvas.start_timer();
				}
			}
			
		});
		
		//Create a slider to control the simulation speed
		JSlider speed_slider = new JSlider(0, 300, 30);
		JLabel speed_label = new JLabel("Select Simulation Speed");
		JLabel speed_description = new JLabel("You're Zoomin!");
		
		//The panel to hold them both
		JPanel speed_panel = new JPanel();
		speed_panel.setBackground(button_color);
		speed_panel.setLayout(new GridLayout(0, 1, 0, 0));
		speed_slider.setBackground(button_color);
		
		//Border for the JPanel
		Border blackline = BorderFactory.createLineBorder(Color.black);
		speed_panel.setBorder(blackline);
		
		speed_slider.setPaintTrack(true);
		speed_slider.setMajorTickSpacing(50);
		speed_slider.setPaintLabels(true);
		
		speed_label.setHorizontalAlignment(JLabel.CENTER);
		speed_description.setHorizontalAlignment(JLabel.CENTER);
		
		speed_panel.add(speed_label);
		speed_panel.add(speed_slider);
		speed_panel.add(speed_description);
		
		//Initializes the change speed change listener
		speed_slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int interval = speed_slider.getValue();
				
				canvas.set_delay(interval);
				
				if (interval < 100) {
					speed_description.setText("You're Zoomin!");
				} else if (interval < 200) {
					speed_description.setText("You're Walkin");
				} else {
					speed_description.setText("You're Crawlin");
				}
				
			}
			
		});
		
		center_menu.add(speed_panel);
		
		//Create and add the speed up button
		RoundedButton exit = new RoundedButton("Exit");
		exit.setBackground(button_color);
		center_menu.add(exit);
		
		//Add functionality to the fast button
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
			}
			
		});
	}
	
	//Function to create the help window
	public void help_window() {
		Boolean was_running = canvas.is_running();
		canvas.stop_timer();
		
		JFrame help_frame = new JFrame();
		
		//Sets the frame for the help window
		help_frame.setSize(200, 400);
		help_frame.setTitle("Help");
		help_frame.setLocation(550, 400);
		
		JButton button = new JButton("Close");
		
		//Creates a panel for the help messages and sets the layout
		JPanel label_panel = new JPanel();
		label_panel.setLayout(new GridLayout(0, 1));
		
		//An array holding the help messages
		String[] help_items = {"H - Help menu", "N - New board", "P - Pause simulation", "I - Increment simulation", "S - Save current board", 
								"C - Clear board", "R - Restart", "R Arrow - Increase speed", "L Arrow - Decrease speed", "ESC - Close window",
								"D - Set to random color", "G - Color to green", "K - Save board to file", "L - Load from file", "M - Button Menu"};
		
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
		       if (was_running) {
					canvas.start_timer();
				}
		    }
		});
		
		button.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				
				//Pause the simulation
				case KeyEvent.VK_ESCAPE:
					
					help_frame.dispose();
					if (was_running) {
						canvas.start_timer();
					}
					
					break;
					
				}
			}
		});
		
		help_frame.setVisible(true);
	}

	//Add borders to a border layout jpanel
	public void add_borders(JPanel panel, boolean bottom) {
		//Add a small border around the menu panel
		JPanel border_north = new JPanel();
		border_north.setBackground(background);
		border_north.setPreferredSize(new Dimension(10, 10));
		panel.add(border_north, BorderLayout.NORTH);
		
		JPanel border_east = new JPanel();
		border_east.setBackground(background);
		border_east.setPreferredSize(new Dimension(10, 10));
		panel.add(border_east, BorderLayout.EAST);
		
		if (bottom) {
			JPanel border_south = new JPanel();
			border_south.setBackground(background);
			border_south.setPreferredSize(new Dimension(10, 10));
			panel.add(border_south, BorderLayout.SOUTH);
		}
		
		JPanel border_west = new JPanel();
		border_west.setBackground(background);
		border_west.setPreferredSize(new Dimension(10, 10));
		panel.add(border_west, BorderLayout.WEST);
	}
	
	//Load board window
	public void load_file() {
		Boolean was_running = canvas.is_running();
		
		canvas.stop_timer();
		JFrame load_frame = new JFrame();
		
		File file = new File(System.getProperty("user.dir"));	//Finds PWD for loading boards
		String[] files = file.list();
		ArrayList<String> good_files = new ArrayList<>();
		
		//Finds all files that end in .txt
		for (String f: files) {
			if (f.indexOf(".txt") > -1) {
				//Skip the info file
				if (!f.equals("Game_of_life_info.txt")) {
					good_files.add(f);
				}
				
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
				
		    	String name = filename.getText() + ".txt";
				
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
		
		filename.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				
				//Pause the simulation
				case KeyEvent.VK_ENTER:
					
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
					
					break;
					
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
	
}