import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

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
		frame.setLocation(400, 100);
		
		frame.create_towns();
		
	}
	
}

class Grid_frame extends JFrame {
	Grid_canvas canvas = new Grid_canvas();
	
	Grid_frame() {}
	
	public void create_towns() {
		canvas.create_towns();
		repaint();
	}
	
}

class Grid_canvas extends JPanel {
	
	ArrayList<ArrayList<Town>> towns = new ArrayList<>();
	
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
				curr_x += town_size;
			}
			
			//Add side length to the curr_y
			curr_y += town_size;
			
			//Reset curr_x
			curr_x = 0;
		}
		
		System.out.println("Repaint");
		repaint();
	}
	
	public ArrayList<ArrayList<Town>> get_town_list() {
		
		return towns;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		setBackground(Color.BLACK);
		
		System.out.println("Painting the towns");
		
		for (int i = 0; i < towns.size(); i += 1) {
			
			ArrayList<Town> curr_array = towns.get(i);
			
			for (int j = 0; j < curr_array.size(); j += 1) {
				
				Town curr_town = curr_array.get(j);
				
				//If statements to draw the towns here
				if (curr_town.is_empty()) {
					curr_town.draw_town(g);
					continue;
				}
				
			}
		}
		
	}
	
}

class Town {
	
	int x_origin = 0;
	int y_origin = 0;
	int town_size = 0;
	
	Boolean empty = true;
	Boolean next_round_empty = true;
	
	Town (int x, int y, int size) {
		x_origin = x;
		y_origin = y;
		town_size = size;
		
	}
	
	//Draw the empty square
	public void draw_town(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x_origin, y_origin, town_size, town_size);
		
		g.setColor(Color.black);
		g.drawRect(x_origin, y_origin, town_size, town_size);
	}
	
	public boolean is_empty() {
		return empty;
	}
}


