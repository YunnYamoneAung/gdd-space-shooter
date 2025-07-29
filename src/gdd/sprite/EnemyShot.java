package gdd.sprite;

import static gdd.Global.*;

import javax.swing.ImageIcon;
import java.awt.Image;

public class EnemyShot extends Sprite {

    private final int SPEED = 5;

    public EnemyShot(int x, int y) {
        initShot(x, y);
    }

    private void initShot(int x, int y) {
        var ii = new ImageIcon(IMG_ENEMY_SHOT);  // Define IMG_ENEMY_SHOT in Global.java
        Image scaled = ii.getImage().getScaledInstance(10, 20, Image.SCALE_SMOOTH);
        setImage(scaled);

        setX(x);
        setY(y);
    }

    @Override
    public void act() {
        y += SPEED;
        if (y > BOARD_HEIGHT) {
            die();
        }
    }
}