package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;

import javax.swing.ImageIcon;
import java.awt.Image;

public class SpeedUp extends PowerUp {

    public SpeedUp(int x, int y) {
        super(x, y);

        ImageIcon ii = new ImageIcon(IMG_POWERUP_SPEEDUP);
        Image scaledImage = ii.getImage().getScaledInstance(60, 50, Image.SCALE_SMOOTH);  // ✅ Resize to 50x50
        setImage(scaledImage);
    }

    @Override
    public void act() {
        this.y += 2;
    }

    @Override
    public void upgrade(Player player) {
        player.upgradeSpeed(); // ✅ Track speed level
        player.setSpeed(player.getSpeed() + 1); // ✅ Boost actual movement speed slightly
        this.die();
    }
}
