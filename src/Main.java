import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		char[][] lines = new char[17][21];
		try {
			sc = new Scanner(new File("./m1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int l = 0;
		while (sc.hasNextLine()) {
			lines[l] = sc.nextLine().toCharArray();
			l++;
		}

		Frame f = new Frame(lines);

		f.s.playGen(100);

		int numRounds = 0;

		while(!f.s.pop.allDone()) {
			numRounds++;
			int use = (int)Math.ceil(numRounds/20);
			use *= 100;
			if(use > f.s.pop.Members[0].steps.length) {
				use = f.s.pop.Members[0].steps.length;
			}
			f.s.pop.fillNewGen(use);
			f.s.playGen(use);
			
			if(numRounds > 1000) {
				System.out.println("No path found in 1000 generations.");
				return;
			}
		}
		
		f.s.pop.done = true;
		f.s.repaint();

		int numSteps = 0;

		for(int i=0; i<f.s.pop.Members[0].steps.length; i++) {
			if(!f.s.pop.Members[0].steps[i].toString().equals("{0.0,0.0}")) {
				numSteps++;
			}
		}

		System.out.println("Number of Generations to Find Path: " + numRounds);
		System.out.println("Number of Steps in Final Path: " + numSteps);

	}

}
