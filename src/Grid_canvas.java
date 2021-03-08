import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.Timer;

class Grid_canvas extends JPanel {
	
	ArrayList<ArrayList<Town>> orig_towns = new ArrayList<>();
	ArrayList<ArrayList<Town>> towns = new ArrayList<>();
	Random rand = new Random();
	Scanner input = new Scanner(System.in);
	Color_manager colors = new Color_manager();
	
	int small_size; //Columns - one row side to side <>
	int big_size;	  //Rows - add one more small size list ^v
	
	int window_x;
	int window_y;
	
	int total_towns;
	
	Timer board_timer;
	int timer_int = 40;
	
	Grid_canvas(int win_x, int win_y, int size_x, int size_y) {
		
		window_x = win_x;
		window_y = win_y;
		big_size = size_y;
		small_size = size_x;
		
		setSize(window_x, window_y);
		setBounds(0, 0, window_x, window_y);
		
		board_timer = new Timer(timer_int, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	    		
	        	get_next_generation();
	            
	            repaint();
	            
	        }
        });
		
		//Start the simulation
		create_towns();
		populate_towns();
		save_town();
		start_timer();
		
		//Looks for a mouse click then adds a new town where clicked
		addMouseListener(new MouseAdapter() {
			
		    public void mouseClicked(MouseEvent e) {
		    		int mouse_x = e.getX();
		    		int mouse_y = e.getY();
		    		
		    		add_new_town(mouse_x, mouse_y);
		    }
		    
		});
		
		addMouseMotionListener(new MouseAdapter() {

		    public void mouseDragged(MouseEvent e) {
		    	add_new_town_dragged(e.getX(), e.getY());
		    }
		    
		});
	}
	
	//Function to create the grid of towns - empty board
	public void create_towns() {
		
		towns = new ArrayList<>();
		
		int curr_small = 0;
		int curr_big = 0;
		int town_size = window_x / small_size;
		total_towns = small_size * big_size;
		
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
	
	//Clears the board
	public void clear_board() {
		ArrayList<ArrayList<Town>> new_towns = new ArrayList<>();
		
		//Creates the town objects
		for (int i = 0; i < big_size; i += 1) {
			
			ArrayList<Town> temp_towns = new ArrayList<>();
			
			for (int j = 0; j < small_size; j += 1) {
				
				//(Town copy, Boolean alive, Color color)
				Town old_town = towns.get(i).get(j);
				Town town = new Town(old_town, true, old_town.get_color());
				
				
				temp_towns.add(town);
			}
			
			new_towns.add(temp_towns);
			
		}
		
		restart_town(new_towns);
		
		repaint();
	}
	
	//Randomly fills out the grid for a random board
	public void populate_towns() {
		
		//Loops once for each box - Can fill boxes more than once
		for (int i = 0; i < total_towns; i += 1) {
			
			int small = rand.nextInt(small_size);
			int big = rand.nextInt(big_size);
			
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
	public void restart_town(ArrayList<ArrayList<Town>> old_towns) {
		//Gets the current color of the squares
		Color curr_color = towns.get(0).get(0).get_color();
		
		towns = new ArrayList<>();
		for (int i = 0; i < old_towns.size(); i += 1) {
			
			ArrayList<Town> temp_town = new ArrayList<>();
			ArrayList<Town> curr_town = old_towns.get(i);
			
			//Loop to make a copy of each town and add it to orig_town
			for (int j = 0; j < curr_town.size(); j += 1) {
				Town copy = curr_town.get(j);
				
				//Adds a copy of the town based on if it's empty or not and the current squares color
				temp_town.add(new Town(copy, copy.is_empty(), curr_color));
				
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
		int neighbors = 0;
		
		for (int i = -1; i < 2; i += 1) {
			for (int j = -1; j < 2; j += 1) {
				if (i == 0 && j == 0) {
					continue;
				}
				
				int new_x = (big + i + towns.size()) % towns.size();
				int new_y = (little + j + towns.get(0).size()) % towns.get(0).size();
				
				Town temp_town = towns.get(new_x).get(new_y);
				if (!temp_town.is_empty()) {
					neighbors += 1;
				}
			}
		}
		
		
		return neighbors;
		
	}
	
	//Returns the current towns list
	public ArrayList<ArrayList<Town>> get_saved_list() {
		
		return orig_towns;
	}
	
	//Function to add a new town when you click on squares
	public void add_new_town(int x, int y) {
		
		//Loops through big list
		for (ArrayList<Town> curr_towns: towns) {
			
			//Loops through small list
			for (Town curr_town: curr_towns) {
				
				//Gets current town coordinates and modifies the mouse coordinates
				int town_x = curr_town.get_x();
				int town_y = curr_town.get_y();
				int town_size = curr_town.get_town_size();
				
				//If the cursor is within the current town, add it as a town
				if (town_x <= x && town_x + town_size > x) {
					if (town_y <= y && town_y + town_size > y) {
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
	
	//Function to add a new town when you click on squares
	public void add_new_town_dragged(int x, int y) {
		
		//Loops through big list
		for (ArrayList<Town> curr_towns: towns) {
					
				//Loops through small list
				for (Town curr_town: curr_towns) {
				
				//Gets current town coordinates and modifies the mouse coordinates
				int town_x = curr_town.get_x();
				int town_y = curr_town.get_y();
				int town_size = curr_town.get_town_size();
				
				//If the cursor is within the current town, add it as a town
				if (town_x <= x && town_x + town_size > x) {
					if (town_y <= y && town_y + town_size > y) {
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
	
	//Increase timer wait
	public void inc_timer() {
		timer_int += 20;
		if (timer_int > 300) {
			timer_int = 300;
		}

		set_delay(timer_int);
		
	}
	
	//Decrease timer weight
	public void dec_timer() {
		timer_int -= 20;
		if (timer_int < 0) {
			timer_int = 0;
		}
		
		set_delay(timer_int);
	}
	
	//Set timer delay
	public void set_delay(int new_interval) {
		timer_int = new_interval;
		board_timer.setDelay(new_interval);
	}
	
	//Sets a random color
	public void set_random_color() {
		Integer[] new_color = colors.get_random_color();
		
		for (ArrayList<Town> temp_towns: towns) {
			
			for (Town town: temp_towns) {
				town.set_random_color(new_color);
				
			}
		}
	}
	
	//Sets a random color
	public void set_green() {
		
		for (ArrayList<Town> temp_towns: towns) {
			
			for (Town town: temp_towns) {
				town.set_green();
			}
		}
	}
	
	//Save board to file
	public void save_to_file(String filename) {
		
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			
			for (ArrayList<Town> temp_towns: towns) {
				
				for (Town town: temp_towns) {

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