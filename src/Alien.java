import java.awt.*;
import java.util.Random;

// entity subclass

public class Alien extends Entity{
	
	private Image image;													// Image to draw on alien's position
	private boolean aggro;													// logic variable that will state if an alien can attack or no 
	private int shootingchance;												// 1/shootingchance = probability for an alien to shoot
	
	// constructor
	
	public Alien(int x, int y, int speed, Image image) {
		
		super(x, y, speed);													// inherited constructor	
		
		this.image = image;														
		
		aggro = false;														// initializing aggro as false
		
		shootingchance = 1000;
	}
	
	// drawing the alien image in its current position
	
	public void draw(Graphics g) {
		
		Graphics2D g2D = (Graphics2D)g;
			
		g2D.drawImage(image, this.x, this.y, null);
		
	}
	
	// moving right by adding the speed to the alien's x position

	public void moveRight() {

		this.x = x+speed;
	
	}
	
	// moving left by subtracting the speed to the alien's x position
	
	public void moveLeft() {
		
		this.x = x-speed;
	
	}
	
	// moving down from left by adding the speed to the players y position

	public void moveDownFromLeft() {
		
		this.y = y+speed;
		
	}
	
	// moving down from right by adding the speed to the players y position
	
	public void moveDownFromRight() {
		
		this.y = y+speed;
	
	}
	
	// checking if the alien collided with provided x and y coords
	
	public boolean checkCollision(int x2, int y2) {
		
		if (x2 >= this.x && x2 <= this.x+30 && y2 >= this.y && y2 <= this.y+20) {
			return true;
		}
		return false;
	
	}
	
	// setting the aggro value to true
	
	public void setAggro(boolean aggro) {
		this.aggro = aggro;
	}
	
	// getter
	
	public boolean getAggro() {
		return aggro;
	}
	
	// checking if alien is in aggro mode, then generating a random number with the probability range for the alien to shoot
	
	public void shoot(BombLauncher bomblauncher) {
		
		if(aggro) {
			
			Random rand = new Random();
			
			int chance = rand.nextInt(this.shootingchance);
				
			if(chance == 5) {													// if the random number is 5(i.e 1/1000 chance)
					
				bomblauncher.addBomb(EntityFactory.createBomb(this.x+15, this.y+20, 5, 8, 8));	// add a bomb to the linked list with position of the alien
			}
		}	
	}
	
	// increase the speed by to 3, this will be applied when only half of the aliens are alive
	
	public void increaseSpeedAndShootingChance() {
		this.speed = 3;
		this.shootingchance = 500;
	}

}

