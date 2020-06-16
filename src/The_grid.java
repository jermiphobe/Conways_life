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
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class The_grid {

	Grid_frame frame = new Grid_frame();
	int window_size_x =1800;
	int window_size_y = 900;
	
	The_grid() {
		//Initializes the frame 'settings'
		frame.setTitle("Conway's Game of Life");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(window_size_x + 16, window_size_y + 39);
		frame.setVisible(true);
		frame.setLocation(75, 100);
		
		frame.create_towns();
		
	}
	
	//Randomly fill squares in the grid
	public void populate_towns() {
		frame.populate_towns();
	}

	//Start the timer
	public void start_simulation() {
		frame.start_simulation();
		
	}
	
}

class Grid_frame extends JFrame {
	Grid_canvas canvas = new Grid_canvas();
	
	//Adds key listeners
	Grid_frame() {
		add(canvas);
		
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
					if (!canvas.is_running()) {
						canvas.create_towns();
						repaint();
					}
					
					break;
					
				//Will save the current board
				case KeyEvent.VK_S:
					if (!canvas.is_running()) {
						canvas.save_town();
					}
					
					break;
					
				//Will restart from saved board
				case KeyEvent.VK_R:
					canvas.restart_town();
					repaint();
					
					break;
					
				//Draws where your mouse is
				case KeyEvent.VK_D:
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
		String[] help_items = {"H - Help menu", "N - New board", "P - Pause simulation", "ESC - Close window", "I - Increment simulation", "S - Save current board", "C - Clear board", "R - Restart"};
		
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
	
	//Create the grid
	public void create_towns() {
		canvas.create_towns();
		repaint();
	}
	
	//Randomly fill square in the grid
	public void populate_towns() {
		canvas.populate_towns();
		canvas.save_town();
	}
	
	//Start the timer
	public void start_simulation() {
		canvas.start_timer();
	}
	
}

class Grid_canvas extends JPanel {
	
	ArrayList<ArrayList<Town>> orig_towns = new ArrayList<>();
	ArrayList<ArrayList<Town>> towns = new ArrayList<>();
	Random rand = new Random();
	
	int small_size = 180; //Columns - one row side to side <>
	int big_size = 90;	  //Rows - add one more small size list ^v
	
	int total_towns = small_size * big_size;
	
	Timer board_timer;
	
	//Adds board timer
	Grid_canvas() {
		
		board_timer = new Timer(50, new ActionListener() {
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
				Town town = new Town(curr_small, curr_big, town_size);
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
		orig_towns = towns;
	}
	
	//Resets the board from the saved board
	public void restart_town() {
		towns = orig_towns;
	}
	
	//Will figure out if each box will live or die
	public void get_next_generation() {
		
		int curr_big = 0;
		int curr_little = 0;
		
		int curr_town_index = 1;
		
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
				curr_town_index += 1;
				
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
						curr_town.populate_town();
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
	
	Color light_gray = new Color(204, 204, 204);
	Color dark_green = new Color(0, 102, 0);
	
	Boolean empty = true;
	Boolean next_round_alive = true;
	
	Town (int x, int y, int size) {
		x_origin = x;
		y_origin = y;
		town_size = size;
		
	}
	
	//Draw the empty square
	public void draw_town(Graphics g) {
		g.setColor(light_gray);
		g.fillRect(x_origin, y_origin, town_size, town_size);
		
		g.setColor(Color.black);
		g.drawRect(x_origin, y_origin, town_size, town_size);
	}
	
	//Draw a full square
	public void new_town(Graphics g) {
		g.setColor(dark_green);
		g.fillRect(x_origin, y_origin, town_size, town_size);
		
		g.setColor(Color.black);
		g.drawRect(x_origin, y_origin, town_size, town_size);
	}
	
	//Populates the town
	public void populate_town() {
		empty = false;
	}
	
	//Kills the town
	public void kill_town() {
		empty = true;
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
}


