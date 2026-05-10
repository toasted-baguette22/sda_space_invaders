public class EntityFactory {

    // Factory method for Player
    public static Player createPlayer(int x, int y, int speed) {
        return new Player(x, y, speed, ResourceManager.getInstance().getImage("player"));
    }

    // Factory method for Alien
    public static Alien createAlien(int x, int y, int speed) {
        return new Alien(x, y, speed, ResourceManager.getInstance().getImage("alien"));
    }

    // Factory method for Laser
    public static Laser createLaser(int x, int y, int speed, int width, int height) {
        return new Laser(x, y, speed, width, height);
    }

    // Factory method for Bomb
    public static Bomb createBomb(int x, int y, int speed, int width, int height) {
        return new Bomb(x, y, speed, width, height);
    }
}
