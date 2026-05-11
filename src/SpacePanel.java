import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

// panel class that inherits panel methods and implements the event key listener class, as well as the runnable class to create a thread

public class SpacePanel extends JPanel implements Runnable, KeyListener, GameObserver, MouseListener, MouseMotionListener{
	
	private static final int PANEL_WIDTH = 600;											// panel
	private static final int PANEL_HEIGHT = 700;										// dimensions
	
	private boolean gameOver;															// logic variables
	private boolean gameStart;															// that will determine the current state of the game
	
	// declaring game variables
	
	Thread gameThread;
	Graphics graphics;
	Player player;
	LaserGun lasergun;
	AlienHorde alienhorde;
	Image backgroundImage;
	Image title;
	Image gameover;
	Image image;
	BombLauncher bomblauncher;
	Score score;
	Lives lives;
	
	// constructor
	
	public SpacePanel() {
		
		gameOver = false;																// initializing
		gameStart = false;																// logic variables as false
		
		setSize(PANEL_WIDTH, PANEL_HEIGHT);												// setting the size of the panel
	    addKeyListener(this);															// adding a key listener
	    addMouseListener(this);
	    addMouseMotionListener(this);
	    setFocusable(true);																// setting focusable to true to receive keyboard input
	    
	    backgroundImage = ResourceManager.getInstance().getImage("background");				// getting background image from ResourceManager
	    title = ResourceManager.getInstance().getImage("title");							// getting title image from ResourceManager
	    gameover = ResourceManager.getInstance().getImage("gameover");						// getting game over image from ResourceManager
	    
	    score = new Score();															// initializing new score object
	    lives = new Lives(ResourceManager.getInstance().getImage("heart"));				// getting heart image from ResourceManager
	    
	    player = EntityFactory.createPlayer((PANEL_WIDTH/2) - 30, (PANEL_HEIGHT - 65), 10);			// creating a new player using Factory
	    lasergun = new LaserGun();														// creating new laser gun
	    bomblauncher = new BombLauncher();												// creating new bomb launcher
	    alienhorde = new AlienHorde();		// creating new alien horde
	   
	    
	    gameThread = new Thread(this);													// create and
	    gameThread.start();																// start thread
	    
	}
	
	// paint function that paints bigger image into the panel
	
	public void paint(Graphics g) {
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);																	// calling draw method
		g.drawImage(image,0,0,this);
	}
	
	// draw method that varies depending on game state
	
	public void draw(Graphics g) {
		
		g.drawImage(backgroundImage, 0, 0, null);														// always drawing the background
		
		if (!gameStart) {																				// if game started
			
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.PLAIN, 20));
			g.drawString("Press 's' to start", (PANEL_WIDTH/2)-110, (PANEL_HEIGHT/2)+50);				// draw "press s message"
			
			Graphics2D g2D = (Graphics2D)g;
			
			g2D.drawImage(title , (PANEL_WIDTH/2)-210, (PANEL_HEIGHT/2)-180, null);						// draw space invaders title
			
		}else if (gameOver){																			// if the game finished
				
			Graphics2D g2D = (Graphics2D)g;
			
			g2D.drawImage(gameover , (PANEL_WIDTH/2)-210, (PANEL_HEIGHT/2)-180, null);
			
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.PLAIN, 20));
			g.drawString("Final score: ", (PANEL_WIDTH/2)-110, (PANEL_HEIGHT/2)+50);					// drawing "Final score:" message
			
			g.drawString(String.valueOf(score.getScore()), (PANEL_WIDTH/2)+40, (PANEL_HEIGHT/2)+50);	// drawing the score
					
		}else {																							// if game is running
			
			// calling draw methods for each object
			
			score.draw(g);
			player.draw(g);
			lasergun.draw(g);
			alienhorde.draw(g);
			bomblauncher.draw(g);
			lives.draw(g);
		}
		
	}
		
	public void move(){
		
		// calling move methods for every object that requires moving
		
		player.move();
		lasergun.move();
		alienhorde.move(bomblauncher);
		bomblauncher.move();
		
	}
	
	public void checkCollision() {
		
		// checking if both player and aliens have collided
		
		alienhorde.checkCollision(lasergun, score);
		player.checkCollision(alienhorde, bomblauncher, this, PANEL_WIDTH, PANEL_HEIGHT);
	}
	
	// run method that handles 60 frames per second
	
	public void run() {
		
		// setting up thread
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;				
		
		// thread loop
		
		while(true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			// if condition met for every frame
			
			if (delta >= 1) {
				
				if(!gameStart) {												// if game didn't start, just repaint
					
				}else if (gameOver){											// if game is over, just repaint

				}else {
					checkCollision();											// if game is running check collision
					move();														// move objects
					if(alienhorde.getAlive() == 0 || lives.getLives() == 0) {	// check if there are no more aliens and if lives is equal to 0
						gameOver = true;
					}

				}
				
				repaint();														// repaint inherited method
				delta--;														// decrease delta
			}
		}
	}

	//Key Listener methods
	
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		
		player.keyPressed(e);													// call key pressed method from player
		
	}

	public void keyReleased(KeyEvent e) {
		player.keyReleased(e);
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {								// if released space, shoot
			
			lasergun.addLaser(EntityFactory.createLaser(player.x+15, player.y-5, 5, 3, 5));		// add laser to linked list
		}
		
		if (e.getKeyCode() == KeyEvent.VK_S) {
			
			gameStart = true;													// if pressed s, start the game
		}
	}
	
	// getters and setters
	
	public int getPanelWidth() {
		return PANEL_WIDTH;
	}
	
	public int getPanelHeight() {
		return PANEL_HEIGHT;
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	// Mouse Listener and Motion Listener methods

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			lasergun.addLaser(EntityFactory.createLaser(player.x+15, player.y-5, 5, 3, 5));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		player.setX(e.getX() - 15);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		player.setX(e.getX() - 15);
	}

	// GameObserver methods
	@Override
	public void onGameOver() {
		this.gameOver = true;
	}

	@Override
	public void onPlayerHit() {
		lives.takeLife();
	}
}
