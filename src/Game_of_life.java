import java.util.Scanner;

public class Game_of_life {

	public static void main(String[] args) {
		
		The_grid grid = new The_grid();
		int pop_towns = 0;
		
		Scanner input = new Scanner(System.in);
		
		//Populate the grid
		grid.populate_towns();
		
		//Start the simulation
		grid.start_simulation();
		
	}

}
