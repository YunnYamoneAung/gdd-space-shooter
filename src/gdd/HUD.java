package gdd;

import gdd.sprite.Player;
import java.awt.*;

public class HUD {

    private int score = 0;
    private int shots = 0;
    private Player player;

    public HUD(Player player) {
        this.player = player;
    }

    public void incrementShots() {
        shots++;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Speed Lvl: " + player.getSpeedLevel(), 10, 40);
        g.drawString("Shots Lv: " + (player.getMultiShotLevel() + 1), 10, 60);
    }

    public void update() {
        // Not used (optional to implement)
    }
}
