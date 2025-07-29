package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Alien2 extends Enemy {

    private int direction = 1;
    private boolean zigzag = false;  // New flag

    public Alien2(int x, int y) {
        super(x, y);
        initAlien();
    }

    private void initAlien() {
        var ii = new ImageIcon(IMG_ENEMY2); // or IMG_ALIEN2 if defined
    
        var scaledImage = ii.getImage().getScaledInstance(
            ii.getIconWidth() * SCALE_FACTOR, 
            ii.getIconHeight() * SCALE_FACTOR, 
            java.awt.Image.SCALE_SMOOTH
        );
    
        setImage(scaledImage);
    }
    

    public void setZigzag(boolean zigzag) {
        this.zigzag = zigzag;
    }

    public boolean isZigzag() {
        return zigzag;
    }

    @Override
    public void act(int unused) {
        if (zigzag) {
            // zig-zag movement
            int offset = (int)(10 * Math.sin((y + x) / 15.0));
            x += offset;
        } else {
            x += direction * 3;
            y += (int)(Math.sin(x / 30.0) * 2);  // default zigzag
        }

        // bounce off walls
        if (x < 0 || x > BOARD_WIDTH - ALIEN_WIDTH) {
            direction *= -1;
        }
    }
}