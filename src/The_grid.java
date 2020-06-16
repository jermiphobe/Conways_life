import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
	
	public void populate_towns(int amount) {
		frame.populate_towns(amount);
	}
	
	public void start_simulation() {
		
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			frame.next_round();
			
		}
	}
	
}

class Grid_frame extends JFrame {
	Grid_canvas canvas = new Grid_canvas();
	
	Grid_frame() {
		add(canvas);
	}
	
	public void create_towns() {
		canvas.create_towns();
		repaint();
	}
	
	public void populate_towns(int amount) {
		canvas.populate_towns(amount);
	}
	
	public void next_round() {
		canvas.get_next_generation();
	}
	
}

class Grid_canvas extends JPanel {
	
	ArrayList<ArrayList<Town>> towns = new ArrayList<>();
	Random rand = new Random();
	
	int small_size = 180; //Columns - one row side to side <>
	int big_size = 90;	  //Rows - add one more small size list ^v
	
	int total_towns = small_size * big_size;
	
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
	
	public void populate_towns(int amount) {
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
		
		repaint();
		
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
}


