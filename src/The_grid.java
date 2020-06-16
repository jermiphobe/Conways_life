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
	
	//Adds timers and key listeners
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
					
				case KeyEvent.VK_N:
					canvas.populate_towns();
					
					break;
					
				case KeyEvent.VK_H:
					help_window();
					
					break;
					
				case KeyEvent.VK_ESCAPE:
					System.exit(0);
					break;
					
				case KeyEvent.VK_I:
					if (!canvas.is_running()) {
						canvas.get_next_generation();
						repaint();
					}
					
					break;
					
				case KeyEvent.VK_C:
					if (canvas.is_running()) {
						canvas.stop_timer();
					}
					
					
					
				}
				
			}
			
		});
		
		addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    		int mouse_x = e.getX();
		    		int mouse_y = e.getY();
		    		
		    		canvas.add_new_town(mouse_x, mouse_y);
		    }
		});
		
		
	}
	
	public void help_window() {
		canvas.stop_timer();
		
		JFrame help_frame = new JFrame();
		
		JButton button = new JButton("Close");
		JLabel message = new JLabel("N - New board \n", SwingConstants.CENTER);
		JLabel message_2 = new JLabel("P - Pause simulation \n", SwingConstants.CENTER);
		JLabel message_3 = new JLabel("H - Help menu \n", SwingConstants.CENTER);
		
		help_frame.setSize(200, 400);
		help_frame.setTitle("Help!");
		help_frame.setLocation(550, 400);
		
		JPanel label_panel = new JPanel();
		
		label_panel.setLayout(new GridLayout(0, 1));
		
		JLabel message1 = new JLabel("H - Help menu");
		message1.setHorizontalAlignment(JLabel.CENTER);
		
		String[] help_items = {"H - Help menu", "N - New board", "P - Pause simulation", "ESC - Close window", "I - Increment simulation"};
		
		for (int i = 0; i < help_items.length; i += 1) {
			String str = help_items[i];
			
			JLabel label = new JLabel(str);
			label.setHorizontalAlignment(JLabel.CENTER);
			label_panel.add(label);
		}
         
		help_frame.add(label_panel, BorderLayout.CENTER);
		help_frame.add(button, BorderLayout.SOUTH);
		
		help_frame.repaint();
		
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
	}
	
	//Start the timer
	public void start_simulation() {
		canvas.start_timer();
	}
	
}

class Grid_canvas extends JPanel {
	
	ArrayList<ArrayList<Town>> towns = new ArrayList<>();
	Random rand = new Random();
	
	int small_size = 180; //Columns - one row side to side <>
	int big_size = 90;	  //Rows - add one more small size list ^v
	
	int total_towns = small_size * big_size;
	
	Timer board_timer;
	
	Grid_canvas() {
		
		board_timer = new Timer(50, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	    		
	        	get_next_generation();
	            
	            repaint();
	            
	        }
        });
		
	}
	
	public void create_towns() {
		
		System.out.println("Created the towns");
		
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
	
	public void populate_towns() {
		int index = 0;
		
		for (int i = 0; i < total_towns; i += 1) {
			index = rand.nextInt(total_towns);
			
			int small = index / 90;
			int big = index % 90;
			
			ArrayList<Town> curr_town_list = towns.get(big);
			Town curr_town = curr_town_list.get(small);
			
			curr_town.populate_town();
			
			repaint();
			
		}
		
	}
	
	public void get_next_generation() {
		
		int curr_big = 0;
		int curr_little = 0;
		
		int curr_town_index = 1;
		
		for (int i = 0; i < towns.size(); i += 1) {
			
			ArrayList<Town> curr_towns = towns.get(i);
			
			for (int j = 0; j < curr_towns.size(); j += 1) {
				
				Town curr_town = curr_towns.get(j);
				int total_neighbors = get_neighbors(curr_big, curr_little);
				
				if (curr_town.is_empty()) {
					
					if (total_neighbors == 3) {
						curr_town.set_next_round_alive();
					} 
					
				} else {
					
					if (total_neighbors <= 1 || total_neighbors >= 4) {
						curr_town.set_next_round_dead();
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
		
		for (int i = 0; i < towns.size(); i += 1) {
			
			ArrayList<Town> curr_towns = towns.get(i);
			
			for (int j = 0; j < curr_towns.size(); j += 1) {
				
				Town curr_town = curr_towns.get(j);
				
				curr_town.set_next_round();
				
			}
			
		}
		
		
	}
	
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
	
	public ArrayList<ArrayList<Town>> get_town_list() {
		
		return towns;
	}
	
	public void add_new_town(int x, int y) {
		
		for (int i = 0; i < towns.size(); i += 1) {
			
			ArrayList<Town> curr_towns = towns.get(i);
			
			for (int j = 0; j < curr_towns.size(); j += 1) {
				
				Town curr_town = curr_towns.get(j);
				
				int town_x = curr_town.get_x();
				int town_y = curr_town.get_y();
				int town_size = curr_town.get_town_size();
				
				town_x += town_size;
				town_y += town_size * 3;
				
				if (town_x < x && town_x + town_size > x) {
					if (town_y < y && town_y + town_size > y) {
						curr_town.populate_town();
					}
				}
				
			}
			
		}
		
		repaint();
		
	}
	
	public void start_timer() {
		board_timer.start();
	}
	
	public void stop_timer() {
		board_timer.stop();
	}
	
	public boolean is_running() {
		return board_timer.isRunning();
	}
	
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
	
	public void populate_town() {
		empty = false;
	}
	
	public void kill_town() {
		empty = true;
	}
	
	public boolean is_empty() {
		return empty;
	}
	
	public int get_town_size() {
		return town_size;
	}
	
	public void set_next_round_alive() {
		next_round_alive = true;
	}
	
	public void set_next_round_dead() {
		next_round_alive = false;
	}
	
	public void set_next_round() {
		if (next_round_alive) {
			empty = false;
		} else {
			empty = true;
		}
	}
	
	public int get_x() {
		return x_origin;
	}
	
	public int get_y() {
		return y_origin;
	}
}


