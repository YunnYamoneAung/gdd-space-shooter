package gdd.scene;

import gdd.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class GameOverPanel extends JPanel {

    private final Game game;
    private final Timer repaintTimer;
    private final JButton retryButton;
    private final JButton exitButton;
    private final Random random = new Random();

    public GameOverPanel(Game game) {
        this.game = game;
        setBackground(Color.BLACK);
        setLayout(null);

        retryButton = new JButton("Retry");
        retryButton.setFont(new Font("Arial", Font.BOLD, 18));
        retryButton.setFocusPainted(false);
        retryButton.addActionListener((ActionEvent e) -> game.loadScene1());
        add(retryButton);

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        repaintTimer = new Timer(1000 / 60, e -> repaint());
        repaintTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawStars(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        g2d.setColor(Color.RED);
        g2d.drawString("GAME OVER", centerText("GAME OVER", g2d, 60), 200);

        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString("Better luck next time.", centerText("Better luck next time.", g2d, 24), 260);
    }

    private void drawStars(Graphics g) {
        g.setColor(new Color(255, 255, 255, 100));
        for (int i = 0; i < 80; i++) {
            int x = random.nextInt(getWidth());
            int y = random.nextInt(getHeight());
            g.fillRect(x, y, 2, 2);
        }
    }

    private int centerText(String text, Graphics2D g2d, int fontSize) {
        FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.PLAIN, fontSize));
        return (getWidth() - metrics.stringWidth(text)) / 2;
    }

    @Override
    public void doLayout() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        retryButton.setBounds(panelWidth / 2 - 100, panelHeight / 2 + 50, 200, 40);
        exitButton.setBounds(panelWidth / 2 - 100, panelHeight / 2 + 110, 200, 40);
    }
}