import java.util.Scanner;

public class Game_of_life {

	public static void main(String[] args) {
		
		The_grid grid = new The_grid();
		int pop_towns = 0;
		
		Scanner input = new Scanner(System.in);
		
		//Input for how many towns you want populated
		while (true) {
			System.out.print("How many towns out of 100 do you want to populate? > ");
			String num_towns = input.nextLine();
			
			
			try {
				Integer.parseInt(num_towns);
				
				pop_towns = Integer.parseInt(num_towns);
				
				if (pop_towns >= 0 && pop_towns <= 100) {
					break;
				} else {
					continue;
				}
				
			} catch (NumberFormatException e) {
				System.out.println();
				continue;
			}
		}
		
		//Populate the grid
		grid.populate_towns(pop_towns);
		
	}

}
