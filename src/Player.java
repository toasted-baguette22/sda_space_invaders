import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

// child of entity super class

public class Player extends Entity {

	private Image image; // image that is going to be drawn on the current player's position

	// constructor

	public Player(int x, int y, int speed, Image image) {

		super(x, y, speed); // inherited constructor

		this.image = image;

	}

	// moving the player right with the speed offset

	public void moveRight() {

		if (x >= (600 - 40)) { // testing if player is in bounds of the panel
			return;
		} else {
			x = x + speed; // incrementing the x position by the speed
		}
	}

	// moving the player left with the speed offset

	public void moveLeft() {

		if (x <= 5) { // testing if player is in bounds of the panel
			return;
		} else {
			x = x - speed; // incrementing the x position by the speed
		}

	}

	// drawing the player image in its current position

	public void draw(Graphics g) {

		Graphics2D g2D = (Graphics2D) g; // cast g as a Graphics2D

		g2D.drawImage(image, this.x, this.y, null); // drawing the image in the player's position

	}

	private boolean leftPressed = false;
	private boolean rightPressed = false;

	// overriding the KeyPressed method from the panel to class to satisfy the
	// player logic

	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {

			case 37:
				leftPressed = true;
				break;
			case 39:
				rightPressed = true;
				break;

		}

	}

	public void keyReleased(KeyEvent e) {

		switch (e.getKeyCode()) {

			case 37:
				leftPressed = false;
				break;
			case 39:
				rightPressed = false;
				break;

		}

	}

	public void move() {
		if (leftPressed) {
			this.moveLeft();
		}
		if (rightPressed) {
			this.moveRight();
		}
	}

	public void setX(int x) {
		if (x >= (600 - 40)) { // testing if player is in bounds of the panel
			this.x = 600 - 40;
		} else if (x <= 5) {
			this.x = 5;
		} else {
			this.x = x;
		}
	}

	// iterating through the bomblauncher linked list and checking if player
	// collided with a bomb

	public boolean checkBombCollision(BombLauncher bomblauncher, int panelWidth) {

		for (int k = 0; k < bomblauncher.size(); k++) {

			if ((bomblauncher.getBomb(k)).x >= this.x && (bomblauncher.getBomb(k)).x <= this.x + 30
					&& (bomblauncher.getBomb(k)).y >= this.y) {

				bomblauncher.removeBomb(bomblauncher.getBomb(k)); // remove the collided bomb

				this.x = panelWidth / 2; // setting the player in the center of the panel

				return true;
			}
		}
		return false;
	}

	// iterating through alien matrix and checking if player collided with matrix or
	// if alien reached the bottom of the screen

	public boolean checkAlienCollision(AlienHorde alienhorde, int panelHeight) {

		for (int i = 0; i < alienhorde.getRows(); i++) {

			for (int j = 0; j < alienhorde.getColumns(); j++) {

				if (!alienhorde.checkIfNull(i, j)) { // checking if current alien is not null

					if (alienhorde.aliens[i][j].checkCollision(this.x, this.y)) {

						return true; // pass the check collision method from the alien with the player position as
										// the parameter

					} else if ((alienhorde.aliens[i][j]).y >= panelHeight - 20) {

						return true; // check if alien y coordinate has reached bottom of the screen

					}
				}
			}
		}
		return false;
	}

	// checking if alien has collided with bomb or alien

	public void checkCollision(AlienHorde alienhorde, BombLauncher bomblauncher, GameObserver observer, int panelWidth,
			int panelHeight) {

		if (this.checkBombCollision(bomblauncher, panelWidth)) {

			observer.onPlayerHit(); // if collided with bomb, notify observer
		}

		if (this.checkAlienCollision(alienhorde, panelHeight)) {

			observer.onGameOver(); // if collided with alien, end the game

		}

	}

}