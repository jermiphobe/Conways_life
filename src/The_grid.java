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
	
}

class Grid_canvas extends JPanel {
	
	ArrayList<ArrayList<Town>> towns = new ArrayList<>();
	Random rand = new Random();
	
	public void create_towns() {
		
		System.out.println("Created the towns");
		
		int curr_x = 0;
		int curr_y = 0;
		int town_size = 10;
		
		//Creates the square objects
		for (int i = 0; i < 180; i += 1) {
			
			ArrayList<Town> temp_towns = new ArrayList<>();
			
			for (int j = 0; j < 90; j += 1) {
				Town town = new Town(curr_x, curr_y, town_size);
				temp_towns.add(town);
				
				//Increment curr_x
				curr_y += town_size;
			}
			
			towns.add(temp_towns);
			
			//Add side length to the curr_y
			curr_x += town_size;
			
			//Reset curr_x
			curr_y = 0;
		}
	}
	
	public void populate_towns(int amount) {
		int total_amount = amount * 162;
		int index = 0;
		
		for (int i = 0; i < total_amount; i += 1) {
			index = rand.nextInt(16200);
			
			System.out.println(index);
			
			int x = index / towns.size();
			int y = index % towns.get(0).size();
			
			Town curr_town = towns.get(y).get(x);
			
			curr_town.populate_town();
			
		}
		
		repaint();
		
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
	Boolean next_round_empty = false;
	
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
}


