import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.concurrent.TimeUnit;


public class Screen extends JPanel {
	
	private static final long serialVersionUID = 1L;
	Population pop;
	
	public Screen(char[][] input) {
		this.pop = new Population(input);
		repaint();
	}

	/**
	 * shows dots and gate at given positions
	 */
	public void paint(Graphics g) {
		
		// paint members
		g.setColor(Color.BLACK);
		for(int i=0; i<this.pop.Members.length; i++) {
			g.fillOval((int) this.pop.Members[i].xPos+500, 
						(int) this.pop.Members[i].yPos+400, 4, 4);
		}
		
		// paint obstacles
		g.setColor(Color.blue);
		if(pop.obs.length > 0) {
			for(int i=0; i<this.pop.obs.length; i++) {
				g.drawRect(pop.obs[i].x, pop.obs[i].y, pop.obs[i].width, pop.obs[i].height);
				
			}
		}
		
		// paint gate
		g.setColor(Color.RED);
		g.fillOval(this.pop.gateX, this.pop.gateY, 50, 50);
		
		
		double xVel = 0;
		double yVel = 0;
		if(this.pop.done) {
			g.setColor(Color.GREEN);
			xVel += pop.Members[0].steps[0].x;
			yVel += pop.Members[0].steps[0].y;
			int X = 500;
			int Y = 400;
			g.drawLine(500, 400, (int) (500 + xVel), (int) (400 + yVel));
			for(int on = 1; on<pop.Members[0].steps.length; on++) {
				if(pop.Members[0].steps[on].toString().equals("{0.0,0.0}"))
					return;
				int oldX = X;
				int oldY = Y;
				xVel += pop.Members[0].steps[on].x; 
				yVel += pop.Members[0].steps[on].y;
				if(xVel < -pop.Members[0].maxVelocity)
					xVel = -pop.Members[0].maxVelocity;
				if(yVel < -pop.Members[0].maxVelocity)
					yVel = -pop.Members[0].maxVelocity;
				if(xVel > pop.Members[0].maxVelocity)
					xVel = pop.Members[0].maxVelocity;
				if(yVel > pop.Members[0].maxVelocity)
					yVel = pop.Members[0].maxVelocity;
				X = (int) (X + xVel);
				Y = (int) (Y + yVel);
				g.drawLine(X, Y, oldX, oldY);
			}
		}
		
	}
	
	/**
	 * Takes the exists population, steps through all 100 steps for all members, and shows.
	 */
	public void playGen(int stepsToTrain) {
		for(int i=0; i<stepsToTrain; i++) {
			this.pop.moveAll(i);
			this.repaint();
			
			try {
				TimeUnit.MILLISECONDS.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
}
	
	