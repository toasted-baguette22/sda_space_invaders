import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class ResourceManager {
    
    private static ResourceManager instance;
    private Map<String, Image> images;

    // Private constructor for Singleton
    private ResourceManager() {
        images = new HashMap<>();
        loadResources();
    }

    // Thread-safe Singleton getter
    public static synchronized ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    private void loadResources() {
        // Pre-load all game assets
        images.put("background", new ImageIcon("../res/starrysky.png").getImage());
        images.put("title", new ImageIcon("../res/Title.png").getImage());
        images.put("gameover", new ImageIcon("../res/Gameover.png").getImage());
        images.put("heart", new ImageIcon("../res/Heart.png").getImage());
        images.put("player", new ImageIcon("../res/Player.png").getImage());
        images.put("alien", new ImageIcon("../res/Alien.gif").getImage());
    }

    public Image getImage(String key) {
        return images.get(key);
    }
}
