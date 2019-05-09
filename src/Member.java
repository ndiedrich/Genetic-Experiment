import java.util.Random;
import java.math.*;

public class Member {

	boolean isDead, atGoal;
	int gateX, gateY, roundsLived;
	double xPos, yPos, xVel, yVel, fitness, maxVelocity;;
	Instruction[] steps;

	// generates new members with 200 instructions and maxVelocity = 10
	public Member(int x, int y) {

		this.steps = new Instruction[1000];
		this.maxVelocity = 30;
		this.xPos = 0;
		this.yPos = 0;
		this.xVel = 0;
		this.yVel = 0;

		// remember gate position
		this.gateX = x-500;
		this.gateY = y-400;

		// remember if you've died or not, and if you've reached the end
		this.isDead = false;
		
		// keep track of rounds alive
		this.roundsLived = 1;

		// generate new instructions 
		for(int i=0; i<this.steps.length; i++) {
			steps[i] = new Instruction(this.maxVelocity);
		}
	}

	/**
	 * updates fitness based on distance to a certain point
	 */
	public void updateFitness() {
		// set fitness equal to however we want to calculate it.
		double disToPoint = Math.sqrt((this.gateX-xPos)*(this.gateX-xPos) + (this.gateY-yPos)*(this.gateY-yPos));
		if(atGoal) { 
			this.fitness = 50000 - roundsLived*10;
		} else if(this.isDead) {
			this.fitness = -400/roundsLived+1000/(disToPoint+0.1);
		} else {
			this.fitness = 800 / disToPoint;
		}
	}

	/**
	 * Updates velocity based on instruction and interates instruction index, 
	 * then updates position accordingly. 
	 */
	public void updateMovement(int insIndex) {
		// update velocities
		this.xVel += this.steps[insIndex].x;
		this.yVel += this.steps[insIndex].y;
		if(xVel > this.maxVelocity)
			xVel = this.maxVelocity;
		if(xVel < -this.maxVelocity)
			xVel = -this.maxVelocity;
		if(yVel > this.maxVelocity)
			yVel = this.maxVelocity;
		if(yVel < -this.maxVelocity)
			yVel = -this.maxVelocity;

		// udate position
		this.xPos += this.xVel;
		this.yPos += this.yVel;


	}

	/**
	 * Checks if this member is inside any of a given array of obstacles
	 * @param obs
	 * @return
	 */
	public boolean isInsideObstacle(Obstacle[] obs) {

		if(obs.length == 0) {
			return false;
		} else {
			for(int i=0; i<obs.length; i++) {

				// check bounds of each obstacle, return true if this member is inside them
				if(this.xPos > obs[i].x-500 && this.xPos < obs[i].x+obs[i].width-500 &&
						this.yPos > obs[i].y-400 && this.yPos < obs[i].y+obs[i].height-400) {
					return true;
				}

			}
		}

		return false;

	}

	/**
	 * check if the given member is the goal bounds
	 */
	public boolean isGoal() {
		if(this.xPos > gateX && this.xPos < gateX + 50 &&
				this.yPos > gateY && this.yPos < gateY + 50) {
			return true;
		}
		return false;
	}
	
}




class Instruction {
	double x, y;

	// creates a new instruction with random x and y values (-maxVelocity/2 < x and y < maxVelocity/2)
	public Instruction(double maxVelocity) {
		Random r = new Random();
		this.x = (r.nextDouble()*maxVelocity)-maxVelocity/2;
		this.y = (r.nextDouble()*maxVelocity)-maxVelocity/2;
	}
	
	public String toString() {
		return "{" + x + "," + y + "}";
	}
	
	public void add(Instruction ins2) {
		this.x += ins2.x;
		this.y += ins2.y;
	}


}
