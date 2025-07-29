package gdd.sprite;

import javax.swing.ImageIcon;
import java.awt.Image;

public class BossShot extends Sprite {

    public BossShot(int x, int y) {
        try {
            // ✅ Correct filename and classpath loading
            ImageIcon ii = new ImageIcon(getClass().getResource("/images/enemy_shot.png"));
            Image image = ii.getImage().getScaledInstance(10, 20, Image.SCALE_SMOOTH);
            setImage(image);
        } catch (Exception e) {
            System.err.println("❌ Failed to load boss shot image: " + e.getMessage());
        }

        setX(x);
        setY(y);
    }

    public void act() {
        y += 5;
        if (y > 800) {
            die();
        }
    }
}
