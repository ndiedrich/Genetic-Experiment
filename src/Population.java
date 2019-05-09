import java.util.Random;

public class Population {
	
	Member[] Members;
	Obstacle[] obs;
	int gateX, gateY, mutationRate;
	boolean done;
	
	// initialize new list of members
	public Population(char[][] input) {
		done = false;
		this.gateX = 0;
		this.gateY = 0;
		this.obs = new Obstacle[0];
		this.generateObstaclesAndGate(input);
		this.mutationRate = 30;
		this.Members = new Member[200];
		for(int i=0; i<this.Members.length; i++) {
			this.Members[i] = new Member(gateX, gateY);
		}
	}

	/**
	 * Bubblesort's array of members by fitness
	 */
	public void sortByFitness() {
		int n = this.Members.length; 
        for (int i = 0; i < n-1; i++) 
            for (int j = 0; j < n-i-1; j++) 
                if (this.Members[j].fitness < this.Members[j+1].fitness) 
                { 
                    // swap this.Members[j+1] and this.Members[i] 
                    Member temp = this.Members[j]; 
                    this.Members[j] = this.Members[j+1]; 
                    this.Members[j+1] = temp; 
                }
	}

	/**
	 * Chooses a viable parent from a sorted list
	 */
	public Member pickParent() {
		
		Random r = new Random();
		// pick from top members
		int at = r.nextInt(this.Members.length/6);
		return this.Members[at];
		
	}

	/**
	 * Reproduces a new member based on two of the most fit options available
	 * @return a new child member
	 */
	public Member reproduce(int stepsCovered) {
		// get two parents
		Member p1 = this.pickParent();
		Member p2 = this.pickParent();

		// calculate chance of p1 based on a percentage of the total fitness
		double totalFit = p1.fitness + p2.fitness;
		double chanceOfP1 = (p1.fitness / totalFit) * 100; // int from 0 to 100  
		Random r = new Random();
		
		// make a child to return 
		Member child = new Member(gateX, gateY);
		
		// for each instruction, choose using chanceOfP1 whether to make it p1 or p2's instruction
		for(int i=0; i<stepsCovered; i++) {
			int attempt = r.nextInt(100);
			if(attempt > chanceOfP1) {
				child.steps[i] = p1.steps[i];
			} else child.steps[i] = p2.steps[i];
		}
		
		// return child with updated instructions
		return child;
		
	}

	/**
	 * updates the fitness for all members, then fills new generation with children
	 */
	public void fillNewGen(int stepsCovered) {
		// update all fitnesses
		for(int i=0; i<this.Members.length; i++) {
			this.Members[i].updateFitness();
		}
		
		// sorts list of members in order of fitness
		this.sortByFitness();
		
		// make a blank array to hold newly reproduced members
		Member[] newMembers = new Member[this.Members.length];
		
		// fill new array with reproduced members 
		for(int i=0; i<this.Members.length; i++) {
			newMembers[i] = this.reproduce(stepsCovered);
		}
		
		// update this.Members with all of the new values 
		// also call squish
		for(int i=0; i<this.Members.length; i++) {
			this.Members[i] = newMembers[i];
		}
		
		// mutate some random instructions (1/1000)
		// comment this out if prioritizing processing time over quality of final path
		this.mutate();
		
		
	}
	
	/**
	 * Performs certain number step for all of the living members
	 * @param stepNum
	 */
	public void moveAll(int stepNum) {
		for(int i=0; i<this.Members.length; i++) {
			if(Members[i].isDead) {
				Members[i].steps[stepNum-1] = new Instruction(Members[i].maxVelocity);
			} else if(Members[i].atGoal) {
				Members[i].steps[stepNum].x = 0.0;
				Members[i].steps[stepNum].y = 0.0;
			} else {
				this.Members[i].roundsLived++;
				this.Members[i].updateMovement(stepNum);
			}
			if(Members[i].isInsideObstacle(obs)) {
				Members[i].isDead = true;
			}
			if(Members[i].isGoal()) {
				Members[i].atGoal = true;
			}
		}
	}
	
	/**
	 * Updates obstacles to contain a new obstacle
	 * @param o
	 */
	public void addNewOb(Obstacle o) {
		Obstacle[] save = new Obstacle[this.obs.length + 1]; // expand array
		for(int i=0; i<this.obs.length; i++) {
			save[i] = this.obs[i];
		}
		save[this.obs.length] = o;
		this.obs = save;
	}
	
	/**
	 * returns true if all dots made it to the gate
	 */
	public boolean allDone() {
		int test = 0;
		for(int i=0; i<this.Members.length; i++) {
			if(Members[i].atGoal) {
				test++;
			}
		} 
		if(test == this.Members.length) {
			
			return true;
		}
		if(test > 1) {
			this.mutationRate = 0;
		}
		return false;
	}
	
	/**
	 * moves through all available steps and mutates 
	 * @param mutationRate out of 1000
	 */
	public void mutate() {
		Random r = new Random();
		for(int i=0; i<this.Members.length; i++) {
			for(int j=0; j<Members[i].steps.length; j++) {
				if(r.nextInt(1000) < this.mutationRate) {
					this.Members[i].steps[j] = new Instruction(Members[i].maxVelocity);
				}
				if(this.Members[i].steps[j].x == 0.0 || this.Members[i].steps[j].y == 0.0) {
					this.Members[i].steps[j] = new Instruction(Members[i].maxVelocity);
				}
			}
		}
	}
	
	/**
	 * generates obstacles from a character array
	 * @param input
	 */
	public void generateObstaclesAndGate(char[][] input) {
		for(int i=0; i<17; i++) {
			for(int j=0; j<21; j++) {
				if(input[i][j]=='o') {
					this.addNewOb(new Obstacle((j*50)-25, (i*50)-25, 50, 50));
				} else if(input[i][j]=='g') {
					this.gateX = (j*50)-25; 
					this.gateY = (i*50)-25;
				}
			}
		}
	}
	
}
