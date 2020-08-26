import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Color_manager {

	ArrayList<Integer[]> colors = new ArrayList<>();
	Random rand = new Random();
	
	Color_manager() {
		
		File file = new File("colors.csv"); 
		Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Oops, color file not found");
		} 
			  
		while (scan.hasNextLine()) {
			
			String[] temp_color = scan.nextLine().split(",");
			
			String r = temp_color[0];
			String g = temp_color[1];
			String b = temp_color[2];
			
			Integer[] color = {Integer.valueOf(r), Integer.valueOf(g), Integer.valueOf(b)};
			colors.add(color);
		}
		
	}
	
	public Integer[] get_random_color() {
		int index = rand.nextInt(colors.size());
		
		return colors.get(index);
	}
}
