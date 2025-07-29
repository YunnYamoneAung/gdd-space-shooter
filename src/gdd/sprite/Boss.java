package gdd.sprite;

import javax.swing.ImageIcon;
import java.awt.Image;

public class Boss extends Sprite {

    private int health;
    private static final int MAX_HEALTH = 10;

    public Boss(int x, int y) {
        this.x = x;
        this.y = y;
        this.health = MAX_HEALTH;

        ImageIcon ii = new ImageIcon(getClass().getResource("/images/boss.png"));
        Image scaled = ii.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        
        setImage(scaled);
    }

    public void damage() {
        health--;
        if (health <= 0) {
            health = 0;
            die();
        }
    }

    public float getHealthRatio() {
        return health / (float) MAX_HEALTH;
    }

    @Override
    public void act() {
        // Optional: add movement if desired
    }

    public int getWidth() {
        return getImage().getWidth(null);
    }

    public int getHeight() {
        return getImage().getHeight(null);
    }
}
