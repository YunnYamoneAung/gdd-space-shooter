package gdd.scene;

import gdd.Game;
import gdd.HUD;
import gdd.sprite.Alien2;
import gdd.sprite.Enemy;
import gdd.sprite.Explosion;
import gdd.sprite.Player;
import gdd.sprite.Shot;
import gdd.powerup.PowerUp;
import gdd.powerup.SpeedUp;
import gdd.powerup.MultiShotUp;

import static gdd.Global.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Scene2 extends JPanel {

    private HUD hud;
    private final Game game;
    private Timer timer;
    private int frame = 0;

    private Player player;
    private List<Enemy> enemies;
    private List<Shot> shots;
    private List<Explosion> explosions;
    private List<PowerUp> powerups;  // ✅ Added

    public Scene2(Game game, Player player, HUD hud) {
        this.game = game;
        this.player = player;
        this.hud = hud;
        init();
    }

    private void init() {
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);

        shots = new ArrayList<>();
        enemies = new ArrayList<>();
        explosions = new ArrayList<>();
        powerups = new ArrayList<>();  // ✅ Init powerups

        spawnZigzagEnemies();

        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
            addKeyListener(new TAdapter());
        });
    }

    public void start() {
        timer = new Timer(1000 / 60, new GameCycle());
        timer.start();
    }

    private void spawnZigzagEnemies() {
        for (int i = 0; i < 10; i++) {
            enemies.add(new Alien2(50 + i * 60, 30));   // Top row
            enemies.add(new Alien2(50 + i * 60, 80));   // Middle row
            enemies.add(new Alien2(50 + i * 60, 130));  // Bottom row
        }
    }

    private void update() {
        player.act();

        for (Enemy enemy : enemies) {
            enemy.act(0);
        }

        // ✅ PowerUp logic
        List<PowerUp> toRemovePowerUps = new ArrayList<>();
        for (PowerUp p : powerups) {
            p.act();
            if (p.collidesWith(player)) {
                p.upgrade(player);
                toRemovePowerUps.add(p);
            }
        }
        powerups.removeAll(toRemovePowerUps);

        // ✅ Spawn powerups every 400 frames
        if (frame % 400 == 0) {
            int rand = (int) (Math.random() * 2);
            int x = 50 + (int) (Math.random() * (BOARD_WIDTH - 100));
            if (rand == 0) {
                powerups.add(new SpeedUp(x, 0));
            } else {
                powerups.add(new MultiShotUp(x, 0));
            }
        }

        List<Shot> toRemoveShots = new ArrayList<>();
        List<Enemy> toRemoveEnemies = new ArrayList<>();

        for (Shot shot : shots) {
            if (!shot.isVisible()) continue;

            int sx = shot.getX();
            int sy = shot.getY();

            for (Enemy enemy : enemies) {
                if (!enemy.isVisible()) continue;

                int ex = enemy.getX();
                int ey = enemy.getY();
                int ew = enemy.getImage().getWidth(null);
                int eh = enemy.getImage().getHeight(null);

                if (sx >= ex && sx <= ex + ew && sy >= ey && sy <= ey + eh) {
                    enemy.setDying(true);
                    enemy.die();
                    explosions.add(new Explosion(ex, ey));
                    shot.die();
                    toRemoveShots.add(shot);
                    toRemoveEnemies.add(enemy);

                    hud.addScore(100);
                    hud.incrementShots();
                    break;
                }
            }

            shot.setY(shot.getY() - 10);
            if (shot.getY() < 0) {
                shot.die();
                toRemoveShots.add(shot);
            }
        }

        shots.removeAll(toRemoveShots);
        enemies.removeAll(toRemoveEnemies);

        List<Explosion> toPurge = new ArrayList<>();
        for (Explosion e : explosions) {
            e.visibleCountDown();
            if (!e.isVisible()) {
                toPurge.add(e);
            }
        }
        explosions.removeAll(toPurge);

        if (enemies.stream().noneMatch(Enemy::isVisible)) {
            timer.stop();
            SwingUtilities.invokeLater(() -> game.loadScene3());
        }
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        String title = "ZIG ZAG INVASION!";
        int titleWidth = fm.stringWidth(title);
        g.drawString(title, (BOARD_WIDTH - titleWidth) / 2, 20);

        for (PowerUp p : powerups) {   // ✅ Draw powerups
            if (p.isVisible()) {
                g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            }
        }

        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        for (Enemy e : enemies) {
            if (e.isVisible()) {
                g.drawImage(e.getImage(), e.getX(), e.getY(), this);
            }
        }

        for (Shot s : shots) {
            if (s.isVisible()) {
                g.drawImage(s.getImage(), s.getX(), s.getY(), this);
            }
        }

        for (Explosion ex : explosions) {
            g.drawImage(ex.getImage(), ex.getX(), ex.getY(), this);
        }

        if (hud != null) {
            hud.draw(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doGameCycle() {
        frame++;
        update();
        repaint();
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
public void keyPressed(KeyEvent e) {
    player.keyPressed(e);

    if (e.getKeyCode() == KeyEvent.VK_SPACE && shots.size() < 4) {
        int x = player.getX();
        int y = player.getY();
        int level = player.getShotLevel();  // ✅ use multishot level

        if (level == 0) {
            shots.add(new Shot(x, y));  // 1 bullet
        } else if (level == 1) {
            shots.add(new Shot(x - 10, y));
            shots.add(new Shot(x + 10, y));
        } else if (level == 2) {
            shots.add(new Shot(x - 10, y));
            shots.add(new Shot(x, y));
            shots.add(new Shot(x + 10, y));
        } else {
            // level 3 or more (max)
            shots.add(new Shot(x - 15, y));
            shots.add(new Shot(x - 5, y));
            shots.add(new Shot(x + 5, y));
            shots.add(new Shot(x + 15, y));
        }
    }
}

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }
    }
}