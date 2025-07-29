package gdd.powerup;

import gdd.sprite.Player;

import javax.swing.ImageIcon;
import java.awt.Image;

public class MultiShotUp extends PowerUp {

    public MultiShotUp(int x, int y) {
        super(x, y);

        ImageIcon ii = new ImageIcon("src/images/multishot.png");  // Use dedicated bullet power-up icon
        Image image = ii.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        setImage(image);
    }

    @Override
    public void act() {
        y += 2;
        if (y > 800) {
            die(); // remove if out of screen
        }
    }

    @Override
    public void upgrade(Player player) {
        player.upgradeShots();  // Increment by one per power-up  // ✅ Increment multishot level one step at a time
        player.multiShotUp();
        die();                 // ✅ Remove after collecting
    }
}