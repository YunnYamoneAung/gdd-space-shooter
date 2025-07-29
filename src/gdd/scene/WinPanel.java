package gdd.scene;

import gdd.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class WinPanel extends JPanel {

    private final Game game;
    private final Timer repaintTimer;
    private final JButton playAgain;
    private final JButton exit;
    private final Random random = new Random();

    public WinPanel(Game game) {
        this.game = game;
        setBackground(Color.BLACK);
        setLayout(null);

        // Play Again button
        playAgain = new JButton("Play Again");
        playAgain.setFont(new Font("Arial", Font.BOLD, 18));
        playAgain.setFocusPainted(false);
        playAgain.setBounds(300, 350, 200, 40);
        playAgain.addActionListener((ActionEvent e) -> {
            game.loadScene1();
        });
        add(playAgain);

        // Exit button
        exit = new JButton("Exit");
        exit.setFont(new Font("Arial", Font.BOLD, 18));
        exit.setFocusPainted(false);
        exit.setBounds(300, 410, 200, 40);
        exit.addActionListener(e -> System.exit(0));
        add(exit);

        // Repaint at 60 FPS for background animation
        repaintTimer = new Timer(1000 / 60, e -> repaint());
        repaintTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw star background
        drawStars(g);

        Graphics2D g2d = (Graphics2D) g;

        // Glowing "YOU WIN!" title
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("YOU WIN!", centerText("YOU WIN!", g2d, 60), 200);

        // Subtitle
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString("Earth is safe... for now.", centerText("Earth is safe... for now.", g2d, 24), 260);
    }

    // Random pixel star field
    private void drawStars(Graphics g) {
        g.setColor(new Color(255, 255, 255, 100));
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(getWidth());
            int y = random.nextInt(getHeight());
            g.fillRect(x, y, 2, 2);
        }
    }

    // Helper: center text
    private int centerText(String text, Graphics2D g2d, int fontSize) {
        FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.PLAIN, fontSize));
        return (getWidth() - metrics.stringWidth(text)) / 2;
    }

    @Override
    public void doLayout() {
        // Dynamically center buttons
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        playAgain.setBounds(panelWidth / 2 - 100, panelHeight / 2 + 50, 200, 40);
        exit.setBounds(panelWidth / 2 - 100, panelHeight / 2 + 110, 200, 40);
    }
}