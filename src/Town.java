import java.awt.Color;
import java.awt.Graphics;

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