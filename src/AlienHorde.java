import java.awt.Graphics;
import java.awt.Image;

public class AlienHorde {
	
	private static final int ROWS = 13;							// dimensions for the matrix
	private static final int COLUMNS = 5;						// dimensions for the matrix
	
	private int alive;											// keeping track of how many aliens are alive with a variable to decrease time complexity of the program
	
	// logic variables to control the moving directions of the horde
	
	private boolean rightEnable;								
	private boolean leftEnable;									
	private boolean downFromRightEnable;						
	private boolean downFromLeftEnable;		
	
	private int	downTrack;										// keeping track of how far down the aliens have traversed for each moving sequence
	
	Alien[][] aliens = new Alien[ROWS][COLUMNS];				// matrix holding the alien horde
	
	Alien TempAlien;											// temporary alien object
	
	// alien horde constructor
	
	public AlienHorde() {
		
		// aliens will start by moving right
		
		rightEnable = true;										
		leftEnable = false;
		downFromRightEnable = false;
		downFromLeftEnable = false;
				
		alive = ROWS*COLUMNS;									// number of aliens alive is equal to rows of our matrix by the columns
		
		downTrack = 0;											// this is initialized as 0, because the aliens will start moving right, not down
		
		// iterating through the matrix and adding a new alien object for each cell in said matrix
		
		for(int i = 0, x = 15; i < ROWS; i++, x+=40) {
			
			for(int j = 0, y = 60; j < COLUMNS; j++, y+=40) {
				
				aliens[i][j] = EntityFactory.createAlien(x, y, 1);
				
				if (j == COLUMNS - 1) {
					aliens[i][j].setAggro(true);				// we want to make the aliens from the last row aggressive, meaning they can launch bombs
				}
				
			}
		}
	}
	
	// drawing function that iterates through our alien matrix and calls the drawing function from each object
	
	public void draw(Graphics g) {
		
		for(int i = 0; i < ROWS; i++) {
			
			for(int j = 0; j < COLUMNS; j++) {
				
				if(checkIfNull(i,j)) {
					
				}else {
					aliens[i][j].draw(g);						// calling drawing function
				}
			}
		}
	}
	
	
	// iterating through our matrix, checking the direction the aliens should be moving towards, and calling the respective alien moving function
	
	public void move(BombLauncher bomblauncher) {
		
		setDirection();											// updating the direction the horde is moving towards
		
		for(int i = 0; i < ROWS; i++) {
			
			for(int j = 0; j < COLUMNS; j++) {
				
				// if direction -> move direction
				
				if(!checkIfNull(i,j)) {
					if(rightEnable) {
						aliens[i][j].moveRight();
					}else if(downFromRightEnable){
						aliens[i][j].moveDownFromRight();
						downTrack++;
					}else if(leftEnable){
						aliens[i][j].moveLeft();
					}else if(downFromLeftEnable){
						aliens[i][j].moveDownFromLeft();
						downTrack++;
					}
					
					
					
					if (alive == (ROWS*COLUMNS)/2) {
						increaseSpeedAndShootingChance(i,j);					// increasing the speed after half of the aliens died
					}
				}
				
				// calling this methods inside the move function to optimize the program (not having to iterate through the function again)
				// *this could be generalized in a broader function (i.e. updateAlienLogic())
				
				setAggro(i,j);								// checking if there are no aliens below the current object, and setting aggro accordingly
				shoot(bomblauncher, i, j);					// random chance of alien shooting if it is set to aggro
				
			}
		}
	}
	
	// iterating through the matrix, checking position of each alien, and updating the direction accordingly
	
	public void setDirection() {
		
		for(int i = 0; i < ROWS; i++) {
			
			for(int j = 0; j < COLUMNS; j++) {
				
				if(!checkIfNull(i,j)) {
					
	// main direction sequence
					
					if(rightEnable && aliens[i][j].x >= 600-60) {
						
						// if the aliens reached the maximum right position, turn off rightEnabled, and turn on downFromRightEnable
						
						leftEnable = false;
						rightEnable = false;
						downFromRightEnable = true;
						downFromLeftEnable = false;

					}else if (downFromRightEnable && downTrack/alive >= 6) {
						
						// if the aliens reached the maximum down position, turn off downFromRightEnable, and turn on leftEnable
						
						leftEnable = true;
						rightEnable = false;
						downFromRightEnable = false;
						downFromLeftEnable = false;
						downTrack = 0;

					}else if (leftEnable && aliens[i][j].x <= 15) {
						
						// if the aliens reached the maximum left position, turn off leftEnable, and turn on downFromLeftEnable
						
						leftEnable = false;
						rightEnable = false;
						downFromRightEnable = false;
						downFromLeftEnable = true;

					}else if (downFromLeftEnable && downTrack/alive >= 6) {
						
						// return to the starting direction
						
						leftEnable = false;
						rightEnable = true;
						downFromRightEnable = false;
						downFromLeftEnable = false;
						downTrack = 0;
						
					}
				}
			}
		}
	}
	
	// iterating through the alien matrix and laser linked list and checking if alien has collided with laser
	
	public void checkCollision(LaserGun lasers, Score score) {
		
		for(int i = 0; i < ROWS; i++) {
			
			for(int j = 0; j < COLUMNS; j++) {
				
				for(int k = 0; k < lasers.size(); k++) {
					
					if(!checkIfNull(i,j)) {
						
	// calling checkCollision function from the alien object
						
						if(aliens[i][j].checkCollision((lasers.getLaser(k)).x, (lasers.getLaser(k)).y)) {
							
							aliens[i][j] = null;				// if collided, delete current alien by setting it to null, the garbage collector will take care of the rest
							alive--;							// 1 less alien alive
							
							// removing the laser that collided
							
							lasers.removeLaser(lasers.getLaser(k));
							
							// increasing the score
							
							score.increaseScore();
						}
					}
				}
			}
		}
	}
	
	// check if a cell is null
	
	public boolean checkIfNull(int i, int j) {
		
		if (aliens[i][j] == null) {
			return true;
		}
		return false;
	}
	
	// checking if alien is available for aggro and setting it accordingly

	public void setAggro(int i, int j) {
		
		if(!checkIfNull(i,j)) {
			
			if(!(aliens[i][j].getAggro())) {
				
				for(int k = j+1; k < COLUMNS; k++) {	
					
					// checking if there are aliens below the current cell
					
					if(!checkIfNull(i,k)) {
						return;
					}
				}
				
				// if there are not, set the aggro for that alien to true
				
				aliens[i][j].setAggro(true);
			}
		}
		
	}
	
	// calling shoot function of a particular alien of matrix
	
	public void shoot(BombLauncher bomblauncher, int i, int j) {
		
				
		if(!checkIfNull(i,j)) {
			aliens[i][j].shoot(bomblauncher);
		}
					
	}
	
	// increasing the speed for a determined alien
	
	public void increaseSpeedAndShootingChance(int i, int j) {
		
		aliens[i][j].increaseSpeedAndShootingChance();
		
	}
	
	// getters
	
	public int getRows() {
		return ROWS;
	}
	
	public int getColumns() {
		return COLUMNS;
	}
	
	public int getAlive() {
		return alive;
	}
}

