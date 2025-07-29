package gdd.sprite;

import static gdd.Global.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Player extends Sprite {

    private static final int START_X = 270;
    private static final int START_Y = 540;

    private static final int MAX_HEALTH = 5;
    private int health = MAX_HEALTH;

    private int width;
    private int height;
    private int dx;
    private int dy;
    private int currentSpeed = 2;
    private int speedLevel = 0;
    private int shotLevel = 0;

    private int multiShotLevel = 0;

    private int shots = 0;

    private BufferedImage sprite;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream("/images/player.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load player.png: " + e.getMessage());
            return;
        }

        setImage(sprite);
        width = sprite.getWidth();
        height = sprite.getHeight();

        setX(START_X);
        setY(START_Y);
    }

    public void act() {
        x += dx;
        y += dy;

        if (x < 0) x = 0;
        if (x > BOARD_WIDTH - width) x = BOARD_WIDTH - width;
        if (y < 0) y = 0;
        if (y > BOARD_HEIGHT - height) y = BOARD_HEIGHT - height;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) dx = -currentSpeed;
        else if (key == KeyEvent.VK_RIGHT) dx = currentSpeed;
        else if (key == KeyEvent.VK_UP) dy = -currentSpeed;
        else if (key == KeyEvent.VK_DOWN) dy = currentSpeed;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) dx = 0;
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) dy = 0;
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public int setSpeed(int speed) {
        this.currentSpeed = Math.max(1, speed);
        return currentSpeed;
    }

    public void upgradeSpeed() {
        if (speedLevel < 4) {
            speedLevel++;
            currentSpeed = 2 + speedLevel;
        }
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public int getMultiShotLevel() {
        return multiShotLevel;
    }

    public void upgradeShots() {
        if (shotLevel < 3) {
            shotLevel++;
            System.out.println("MultiShot Level upgraded to: " + shotLevel);
        } else {
            System.out.println("MultiShot already maxed.");
        }
    }

    public int getShotLevel() {
        return shotLevel;
    }

    public void incrementShots() {
        shots++;
    }

    public int getShots() {
        return shots;
    }

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        initPlayer();
    }

    public void damage() {
        health--;
        if (health <= 0) {
            health = 0;
            die();
        }
    }

    public void multiShotUp() {
        if (multiShotLevel < 4) {
            multiShotLevel++;
        }
    }

    public boolean isMultiShotEnabled() {
        return multiShotLevel > 0;
    }

    public float getHealthRatio() {
        return health / (float) MAX_HEALTH;
    }

    public int getHealth() {
        return health;
    }
}