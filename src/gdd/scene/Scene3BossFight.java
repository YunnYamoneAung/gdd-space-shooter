package gdd.scene;

import gdd.Game;
import gdd.HUD;
import gdd.sprite.*;
import static gdd.Global.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.Timer;
import javax.swing.JPanel;

public class Scene3BossFight extends JPanel {

    private final Game game;
    private Timer timer;
    private int frame = 0;

    private Player player;
    private HUD hud;
    private Boss boss;
    private List<Enemy> minions;
    private List<Shot> playerShots;
    private List<EnemyShot> enemyShots;
    private List<BossShot> bossShots;
    private List<Explosion> explosions;

    public Scene3BossFight(Game game, Player player, HUD hud) {
        this.game = game;
        this.player = player;
        this.hud = hud;

        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);
        addKeyListener(new TAdapter());
    }

    public void start() {
        requestFocusInWindow();
        boss = new Boss(BOARD_WIDTH / 2 - 60, 50);
        minions = new ArrayList<>();
        playerShots = new ArrayList<>();
        enemyShots = new ArrayList<>();
        bossShots = new ArrayList<>();
        explosions = new ArrayList<>();

        spawnMinions();

        timer = new Timer(1000 / 60, new GameCycle());
        timer.start();
    }

    private void spawnMinions() {
        int y = 150;
        minions.add(new Enemy(100, y));
        minions.add(new Enemy(180, y + 20));
        minions.add(new Enemy(BOARD_WIDTH - 180, y));
        minions.add(new Enemy(BOARD_WIDTH - 100, y + 20));
    }

    private void update() {
        player.act();
        boss.act();

        // Boss shooting
        if (boss.isVisible() && frame % 120 == 0) {
            bossShots.add(new BossShot(boss.getX() + 50, boss.getY() + 40));
        }

        for (BossShot bs : bossShots) {
            bs.setY(bs.getY() + 5);
            if (bs.getY() > BOARD_HEIGHT) bs.die();

            if (player.isVisible()) {
                int bx = bs.getX();
                int by = bs.getY();
                int px = player.getX();
                int py = player.getY();
                int pw = player.getImage().getWidth(null);
                int ph = player.getImage().getHeight(null);

                if (bx >= px && bx <= px + pw && by >= py && by <= py + ph) {
                    player.damage();
                    bs.die();
                    explosions.add(new Explosion(px, py));
                }
            }
        }
        bossShots.removeIf(bs -> !bs.isVisible());

        for (Enemy enemy : minions) {
            if (enemy.isVisible() && frame % 90 == 0) {
                enemyShots.add(new EnemyShot(enemy.getX() + 10, enemy.getY() + 20));
            }
        }

        for (EnemyShot es : enemyShots) {
            es.setY(es.getY() + 4);
            if (es.getY() > BOARD_HEIGHT) es.die();

            if (player.isVisible()) {
                int sx = es.getX();
                int sy = es.getY();
                int px = player.getX();
                int py = player.getY();
                int pw = player.getImage().getWidth(null);
                int ph = player.getImage().getHeight(null);

                if (sx >= px && sx <= px + pw && sy >= py && sy <= py + ph) {
                    player.damage();
                    es.die();
                    explosions.add(new Explosion(px, py));
                }
            }
        }

        enemyShots.removeIf(s -> !s.isVisible());

        List<Shot> toRemove = new ArrayList<>();
        for (Shot shot : playerShots) {
            if (!shot.isVisible()) continue;

            int sx = shot.getX();
            int sy = shot.getY();

            // Hit boss
            if (boss.isVisible() &&
                sx >= boss.getX() &&
                sx <= boss.getX() + boss.getWidth() &&
                sy >= boss.getY() &&
                sy <= boss.getY() + boss.getHeight()) {

                boss.damage();
                explosions.add(new Explosion(boss.getX() + 50, boss.getY() + 20));
                shot.die();
                toRemove.add(shot);
                hud.incrementShots();     // ✅ track shot
                hud.addScore(100);        // ✅ reward score
                continue;
            }

            // Hit minions
            for (Enemy e : minions) {
                if (e.isVisible() &&
                    sx >= e.getX() &&
                    sx <= e.getX() + ALIEN_WIDTH &&
                    sy >= e.getY() &&
                    sy <= e.getY() + ALIEN_HEIGHT) {

                    e.setDying(true);
                    e.die();
                    explosions.add(new Explosion(e.getX(), e.getY()));
                    shot.die();
                    toRemove.add(shot);
                    hud.addScore(100);
                    hud.incrementShots();
                }
            }

            shot.setY(shot.getY() - 10);
            if (shot.getY() < 0) {
                shot.die();
                toRemove.add(shot);
            }
        }

        playerShots.removeAll(toRemove);

        explosions.removeIf(ex -> {
            ex.visibleCountDown();
            return !ex.isVisible();
        });

        if (!boss.isVisible()) {
            timer.stop();
            new Timer(1000, e -> game.loadWinPanel()).start();
        }

        if (!player.isVisible()) {
            timer.stop();
            new Timer(1000, e -> game.loadGameOverPanel()).start();
        }
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        // Title Center
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        String title = "FINAL BOSS FIGHT!";
        int titleWidth = fm.stringWidth(title);
        g.drawString(title, (BOARD_WIDTH - titleWidth) / 2, 20);

        if (boss.isVisible()) {
            g.drawImage(boss.getImage(), boss.getX(), boss.getY(), this);
            drawBossHealthBar(g);
        }

        for (Enemy e : minions) {
            if (e.isVisible()) {
                g.drawImage(e.getImage(), e.getX(), e.getY(), this);
            }
        }

        for (Shot s : playerShots) {
            if (s.isVisible()) {
                g.drawImage(s.getImage(), s.getX(), s.getY(), this);
            }
        }

        for (EnemyShot s : enemyShots) {
            if (s.isVisible()) {
                g.drawImage(s.getImage(), s.getX(), s.getY(), this);
            }
        }

        for (BossShot bs : bossShots) {
            if (bs.isVisible()) {
                g.drawImage(bs.getImage(), bs.getX(), bs.getY(), this);
            }
        }

        for (Explosion ex : explosions) {
            g.drawImage(ex.getImage(), ex.getX(), ex.getY(), this);
        }

        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
            drawPlayerHealthBar(g);
        }

        hud.draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawBossHealthBar(Graphics g) {
        int maxWidth = 100;
        int height = 10;
        int x = BOARD_WIDTH - maxWidth - 20;
        int y = 65;  // below player bar

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Boss", x, y - 5);

        g.setColor(Color.gray);
        g.fillRect(x, y, maxWidth, height);
        g.setColor(Color.red);
        g.fillRect(x, y, (int) (maxWidth * boss.getHealthRatio()), height);
        g.setColor(Color.white);
        g.drawRect(x, y, maxWidth, height);
    }

    private void drawPlayerHealthBar(Graphics g) {
        int maxWidth = 100;
        int height = 10;
        int x = BOARD_WIDTH - maxWidth - 20;
        int y = 20;

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Player", x, y - 5);

        g.setColor(Color.gray);
        g.fillRect(x, y, maxWidth, height);
        g.setColor(Color.green);
        g.fillRect(x, y, (int)(maxWidth * player.getHealthRatio()), height);
        g.setColor(Color.white);
        g.drawRect(x, y, maxWidth, height);
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
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
            if (e.getKeyCode() == KeyEvent.VK_SPACE && playerShots.size() < 4) {
                playerShots.add(new Shot(player.getX(), player.getY()));
            }
        }

        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }
    }
}