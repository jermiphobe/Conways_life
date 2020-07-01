import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.util.Scanner;

public class The_grid {

	Grid_frame frame = new Grid_frame();
	
	The_grid() {}
	
	public static void main(String[] args) {
		The_grid grid = new The_grid();
	}
}

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
					canvas.stop_timer();
					canvas.save_to_file();
					canvas.start_timer();
					
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
		
		//Sets the frame for the help window
		help_frame.setSize(200, 400);
		help_frame.setTitle("Help!");
		help_frame.setLocation(550, 400);
		
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
		canvas.stop_timer();
		JFrame load_frame = new JFrame();
		
		JButton button = new JButton("Submit");
		
		//Sets the frame for the load window
		load_frame.setSize(200, 150);
		load_frame.setTitle("Load");
		load_frame.setLocation(550, 400);
		
		//Creates a panel for the text box and sets the layout
		JPanel label_panel = new JPanel();
		label_panel.setLayout(new GridLayout(0, 1));
		
		
		//Create a label and a text box
		JLabel label = new JLabel("Pick a file:");
		label.setHorizontalAlignment(JLabel.CENTER);
		JTextField file_to_use = new JTextField(20);
		
		//Adds label and text box
		label_panel.add(label);
		label_panel.add(file_to_use);
         
		//Adds submit button and panel
		load_frame.add(label_panel, BorderLayout.CENTER);
		load_frame.add(button, BorderLayout.SOUTH);
		
		load_frame.repaint();
		label_panel.repaint();
		
		//Watches for close button to be clicked
		button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
				
				File file;
				
				try {
					file = new File(file_to_use.getText());
					Scanner scan = new Scanner(file);
					
					canvas.load_from_file(scan);
					canvas.start_timer();
					load_frame.dispose();
					
				} catch (FileNotFoundException ex) {
					
				} 
		       
		    }
		});
		
		load_frame.setVisible(true);
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

class Menu_panel extends JPanel {
	
	Menu_panel(int board_x, int board_y) {

		Color dark_grey = new Color(102, 102, 102);
		
		setBackground(dark_grey);
		setSize(100, board_x);

		setBounds(0, board_y, board_x, 100);
	}
	
}

class Grid_canvas extends JPanel {
	
	ArrayList<ArrayList<Town>> orig_towns = new ArrayList<>();
	ArrayList<ArrayList<Town>> towns = new ArrayList<>();
	Random rand = new Random();
	Scanner input = new Scanner(System.in);
	Color_manager colors = new Color_manager();
	
	int small_size = 180; //Columns - one row side to side <>
	int big_size = 90;	  //Rows - add one more small size list ^v
	
	int window_x = 1800;
	int window_y = 900;
	
	int total_towns = small_size * big_size;
	
	Timer board_timer;
	int timer_int = 50;
	
	Grid_canvas() {
		
		setSize(window_x, window_y);
		setBounds(0, 0, window_x, window_y);
		
		board_timer = new Timer(timer_int, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	    		
	        	get_next_generation();
	            
	            repaint();
	            
	        }
        });
	}
	
	//Function to create the grid of towns - empty board
	public void create_towns() {
		
		towns = new ArrayList<>();
		
		int curr_small = 0;
		int curr_big = 0;
		int town_size = 10;
		
		//Creates the town objects
		for (int i = 0; i < big_size; i += 1) {
			
			ArrayList<Town> temp_towns = new ArrayList<>();
			
			for (int j = 0; j < small_size; j += 1) {
				Town town = new Town(curr_small, curr_big, town_size, colors);
				temp_towns.add(town);
				
				//Increment curr_x
				curr_small += town_size;
			}
			
			towns.add(temp_towns);
			
			//Add side length to the curr_y
			curr_big += town_size;
			
			//Reset curr_x
			curr_small = 0;
		}
		
		repaint();
	}
	
	//Randomly fills out the grid for a random board
	public void populate_towns() {
		int index = 0;
		
		//Loops once for each box - Can fill boxes more than once
		for (int i = 0; i < total_towns; i += 1) {
			index = rand.nextInt(total_towns);
			
			int small = index / 90;
			int big = index % 90;
			
			ArrayList<Town> curr_town_list = towns.get(big);
			Town curr_town = curr_town_list.get(small);
			
			curr_town.populate_town();
			
		}
		
		repaint();
		
	}
	
	//Saves the current town
	public void save_town() {
		//orig_towns = towns;
		orig_towns = new ArrayList<>();
		for (int i = 0; i < towns.size(); i += 1) {
			
			ArrayList<Town> temp_town = new ArrayList<>();
			ArrayList<Town> curr_town = towns.get(i);
			
			//Loop to make a copy of each town and add it to orig_town
			for (int j = 0; j < curr_town.size(); j += 1) {
				Town copy = curr_town.get(j);
				
				temp_town.add(new Town(copy));
				
			}
			
			orig_towns.add(temp_town);
		}
	}
	
	//Resets the board from the saved board
	public void restart_town() {
		//towns = orig_towns;
		towns = new ArrayList<>();
		for (int i = 0; i < orig_towns.size(); i += 1) {
			
			ArrayList<Town> temp_town = new ArrayList<>();
			ArrayList<Town> curr_town = orig_towns.get(i);
			
			//Loop to make a copy of each town and add it to orig_town
			for (int j = 0; j < curr_town.size(); j += 1) {
				Town copy = curr_town.get(j);
				
				temp_town.add(new Town(copy));
				
			}
			
			towns.add(temp_town);
		}
		
		repaint();
	}
	
	//Will figure out if each box will live or die
	public void get_next_generation() {
		
		int curr_big = 0;
		int curr_little = 0;
		
		//Loops through big array
		for (int i = 0; i < towns.size(); i += 1) {
			
			ArrayList<Town> curr_towns = towns.get(i);
			
			//Loops through small array
			for (int j = 0; j < curr_towns.size(); j += 1) {
				
				//Gets current town and its neighbor count
				Town curr_town = curr_towns.get(j);
				int total_neighbors = get_neighbors(curr_big, curr_little);
				
				//If the town is empty, see if it can become alive
				if (curr_town.is_empty()) {
					
					//3 neighbors to become alive
					if (total_neighbors == 3) {
						curr_town.set_next_round_alive();
					} 
					
				//If the town is alive, see if it lives or dies
				} else {
					
					//1 or less, or more than 3 neighbors to die
					if (total_neighbors <= 1 || total_neighbors >= 4) {
						curr_town.set_next_round_dead();
					//2 or 3 neighbors to stay alive
					} else {
						curr_town.set_next_round_alive();
					}
					
				}
				
				curr_little += 1;
				
			}
			
			curr_big += 1;
			curr_little = 0;
			
		}
		
		//Updates the is_empty variable based on next_round_alive variable
		for (int i = 0; i < towns.size(); i += 1) {
			
			ArrayList<Town> curr_towns = towns.get(i);
			
			for (int j = 0; j < curr_towns.size(); j += 1) {
				
				Town curr_town = curr_towns.get(j);
				
				curr_town.set_next_round();
				
			}
		}
	}
	
	//Lots of if statements to count total neighbors
	public int get_neighbors(int big, int little) {
		
		int curr_big = big;
		int curr_little = little;
		
		int new_big = 0;
		int new_little = 0;
		
		int neighbors = 0;
		
		int little_size = towns.get(0).size();
		int big_size = towns.size();
		
		//First square to try
		new_big = curr_big - 1;
		new_little = curr_little - 0;
		if (new_big >= 0 && new_little >= 0 && !towns.get(new_big).get(new_little).is_empty()) {
			
			neighbors += 1;
			
		} 
		
		//Second square
		new_big = curr_big - 1;
		new_little = curr_little + 1;
		if (new_big >= 0 && new_little < little_size && !towns.get(new_big).get(new_little).is_empty()) {
			
			neighbors += 1;
			
		} 
		
		//Third square
		new_big = curr_big - 0;
		new_little = curr_little + 1;
		if (new_big >= 0 && new_little < little_size && !towns.get(new_big).get(new_little).is_empty()) {
			
			neighbors += 1;
			
		} 
		
		//Fourth square
		new_big = curr_big + 1;
		new_little = curr_little + 1;
		if (new_big < big_size && new_little < little_size && !towns.get(new_big).get(new_little).is_empty()) {
			
			neighbors += 1;
			
		} 
		
		//Fifth square
		new_big = curr_big + 1;
		new_little = curr_little + 0;
		if (new_big < big_size && new_little < little_size && !towns.get(new_big).get(new_little).is_empty()) {
			
			neighbors += 1;
			
		} 
		
		//Sixth square
		new_big = curr_big + 1;
		new_little = curr_little - 1;
		if (new_big < big_size && new_little >= 0 && !towns.get(new_big).get(new_little).is_empty()) {
			
			neighbors += 1;
			
		}
		
		//Seventh square
		new_big = curr_big + 0;
		new_little = curr_little - 1;
		if (new_big < big_size && new_little >= 0 && !towns.get(new_big).get(new_little).is_empty()) {
			
			neighbors += 1;
			
		} 
		
		//Eighth square
		new_big = curr_big - 1;
		new_little = curr_little - 1;
		if (new_big >= 0 && new_little >= 0 && !towns.get(new_big).get(new_little).is_empty()) {
			
			neighbors += 1;
			
		} 
		
		//neighbors -= empty_towns;
		
		return neighbors;
		
	}
	
	//Returns the current towns list
	public ArrayList<ArrayList<Town>> get_town_list() {
		
		return towns;
	}
	
	//Function to add a new town when you click on squares
	public void add_new_town(int x, int y) {
		
		//Loops through big list
		for (int i = 0; i < towns.size(); i += 1) {
			
			ArrayList<Town> curr_towns = towns.get(i);
			
			//Loops through small list
			for (int j = 0; j < curr_towns.size(); j += 1) {
				
				Town curr_town = curr_towns.get(j);
				
				//Gets current town coordinates and modifies the mouse coordinates
				int town_x = curr_town.get_x();
				int town_y = curr_town.get_y();
				int town_size = curr_town.get_town_size();
				
				town_x += town_size;
				town_y += town_size * 3;
				
				//If the cursor is within the current town, add it as a town
				if (town_x < x && town_x + town_size > x) {
					if (town_y < y && town_y + town_size > y) {
						if (curr_town.is_empty()) {
							curr_town.populate_town();
						} else {
							curr_town.kill_town();
							curr_town.kill_next_gen();
						}
					}
				}
			}
		}
		
		repaint();
		
	}
	
	//Start the timer
	public void start_timer() {
		board_timer.start();
	}
	
	//Stop the timer
	public void stop_timer() {
		board_timer.stop();
	}
	
	//Returns true if the timer is running
	public boolean is_running() {
		return board_timer.isRunning();
	}
	
	//Increase timer wait
	public void inc_timer() {
		timer_int += 50;
		if (timer_int > 1100) {
			timer_int = 1100;
		}
		
		set_delay();
		
	}
	
	//Decrease timer weight
	public void dec_timer() {
		timer_int -= 50;
		if (timer_int < 0) {
			timer_int = 0;
		}
		
		set_delay();
	}
	
	//Set timer delay
	public void set_delay() {
		//board_timer.stop();
		board_timer.setDelay(timer_int);
		//board_timer.start();
	}
	
	//Sets a random color
	public void set_random_color() {
		Integer[] new_color = colors.get_random_color();
		
		for (int i = 0; i < towns.size(); i += 1) {
			ArrayList<Town> temp_towns = towns.get(i);
			
			for (int j = 0; j < temp_towns.size(); j += 1) {
				Town town = temp_towns.get(j);
				town.set_random_color(new_color);
				
			}
		}
	}
	
	//Sets a random color
	public void set_green() {
		
		for (int i = 0; i < towns.size(); i += 1) {
			ArrayList<Town> temp_towns = towns.get(i);
			
			for (int j = 0; j < temp_towns.size(); j += 1) {
				Town town = temp_towns.get(j);
				town.set_green();
			}
		}
	}
	
	//Save board to file
	public void save_to_file() {
		String filename;
		
		while (true) {
			System.out.print("What do you want to call the file? > ");
			filename = input.nextLine();
			
			try {
				File new_board = new File(filename);
				if (new_board.createNewFile()) {
					break;
				} else {
					System.out.println("That file already exists");
				}
			} catch (IOException e) {
				
			}
			
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			
			for (int i = 0; i < towns.size(); i += 1) {
				ArrayList<Town> temp_towns = towns.get(i);
				
				for (int j = 0; j < temp_towns.size(); j += 1) {
					Town town = temp_towns.get(j);

					if (town.is_empty()) {
						writer.write("empty\n");
					} else {
						writer.write("full\n");
					}
					
				}
			}
			
			writer.close();
			
		} catch (IOException e) {
			System.out.println("Stuffs broken");
		}
		
		
		
	}
	
	//Load from file
	public void load_from_file(Scanner scan) {
		Boolean alive;
		
		ArrayList<ArrayList<Town>> new_towns = new ArrayList<>();
		
		for (int i = 0; i < towns.size(); i += 1) {
			ArrayList<Town> temp_towns = new ArrayList<>();
			ArrayList<Town> curr_towns = towns.get(i);
			
			for (int j = 0; j < curr_towns.size(); j += 1) {
				Town curr_town = curr_towns.get(j);
				String is_alive = scan.nextLine();
				
				if (is_alive.equals("full")) {
					alive = false;
				} else {
					alive = true;
				}
				
				//Town (Town copy, Boolean alive, Color color)
				temp_towns.add(new Town(curr_town, alive, curr_town.get_color()));

			}
			
			new_towns.add(temp_towns);
		}

		towns = new ArrayList<>();
		for (int i = 0; i < orig_towns.size(); i += 1) {
			
			ArrayList<Town> temp_town = new ArrayList<>();
			ArrayList<Town> curr_town = new_towns.get(i);
			
			//Loop to make a copy of each town and add it to orig_town
			for (int j = 0; j < curr_town.size(); j += 1) {
				Town copy = curr_town.get(j);
				
				temp_town.add(new Town(copy));
				
			}
			
			towns.add(temp_town);
		}
		
		save_town();
		repaint();
		
	}
	
	//Repaints the board
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		setBackground(Color.BLACK);
		
		for (int i = 0; i < towns.size(); i += 1) {
			
			ArrayList<Town> curr_array = towns.get(i);
			
			for (int j = 0; j < curr_array.size(); j += 1) {
				Town curr_town = curr_array.get(j);
				
				//If statements to draw the towns here
				if (curr_town.is_empty()) {
					curr_town.draw_town(g);
					continue;
				} else {
					curr_town.new_town(g);
				}
			}
		}
	}
}

class Town {
	
	int x_origin = 0;
	int y_origin = 0;
	int town_size = 0;
	
	Color_manager colors;
	Integer[] curr_color;
	
	Color light_grey = new Color(153, 153, 153);
	Color dark_grey = new Color(102, 102, 102);
	Color green = new Color(0, 255, 0);
	Color alive_color;
	
	Boolean empty = true;
	Boolean next_round_alive = false;
	
	Town (int x, int y, int size, Color_manager color) {
		x_origin = x;
		y_origin = y;
		town_size = size;
		
		colors = color;
		curr_color = colors.get_random_color();
		alive_color = green;
		
	}
	
	Town (Town copy) {
		this.x_origin = copy.x_origin;
		this.y_origin = copy.y_origin;
		this.town_size = copy.town_size;
		
		this.empty = copy.empty;
		this.next_round_alive = copy.next_round_alive;
		
		this.colors = copy.colors;
		this.curr_color = copy.curr_color;
		this.alive_color = copy.alive_color;
	}
	
	Town (Town copy, Boolean alive, Color color) {
		this.x_origin = copy.x_origin;
		this.y_origin = copy.y_origin;
		this.town_size = copy.town_size;
		
		this.empty = alive;
		
		this.colors = copy.colors;
		this.curr_color = copy.curr_color;
		this.alive_color = color;
	}
	
	//Draw the empty square
	public void draw_town(Graphics g) {
		g.setColor(dark_grey);
		g.fillRect(x_origin, y_origin, town_size, town_size);
		
		g.setColor(light_grey);
		g.drawRect(x_origin, y_origin, town_size, town_size);
	}
	
	//Draw a full square
	public void new_town(Graphics g) {
		g.setColor(alive_color);
		g.fillRect(x_origin, y_origin, town_size, town_size);
		
		g.setColor(light_grey);
		g.drawRect(x_origin, y_origin, town_size, town_size);
	}
	
	//Populates the town
	public void populate_town() {
		empty = false;
	}
	
	//Sets a random color
	public void set_random_color(Integer[] new_color) {
		alive_color = new Color(new_color[0], new_color[1], new_color[2]);
	}
	
	//Kills the town
	public void kill_town() {
		empty = true;
	}
	
	public void kill_next_gen() {
		next_round_alive = false;
	}
	
	//Returns true if the town is empty
	public boolean is_empty() {
		return empty;
	}
	
	//Returns the size of the town
	public int get_town_size() {
		return town_size;
	}
	
	//Sets next round alive to true
	public void set_next_round_alive() {
		next_round_alive = true;
	}
	
	//Sets next round alive to false
	public void set_next_round_dead() {
		next_round_alive = false;
	}
	
	//Sets the current empty based on next round variable
	public void set_next_round() {
		if (next_round_alive) {
			empty = false;
		} else {
			empty = true;
		}
	}
	
	//Returns x origin
	public int get_x() {
		return x_origin;
	}
	
	//Returns y origin
	public int get_y() {
		return y_origin;
	}
	
	//Get the current color
	public Color get_color() {
		return alive_color;
	}
	
	//Set the color to green
	public void set_green() {
		alive_color = green;
	}
}


