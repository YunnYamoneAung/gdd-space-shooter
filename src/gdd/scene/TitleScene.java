package gdd.scene;

import gdd.AudioPlayer;
import gdd.Game;
import static gdd.Global.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TitleScene extends JPanel {

    private int frame = 0;
    private Image image;
    private AudioPlayer audioPlayer;
    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private Timer timer;
    private Game game;

    public TitleScene(Game game) {
        this.game = game;
    }

    public void start() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);

        timer = new Timer(1000 / 60, new GameCycle());
        timer.start();

        initTitle();
        initAudio();
    }

    public void stop() {
        try {
            if (timer != null) timer.stop();
            if (audioPlayer != null) audioPlayer.stop();
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
    }

    private void initTitle() {
        var ii = new ImageIcon(IMG_TITLE);
        image = ii.getImage();
    }

    private void initAudio() {
        try {
            String filePath = "src/audio/title.wav";
            audioPlayer = new AudioPlayer(filePath);
            audioPlayer.play();
        } catch (Exception e) {
            System.err.println("Error with playing sound.");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        g.drawImage(image, 0, -80, d.width, d.height, this);

        // === Team Member Box ===
        String[] members = {
            "Team Members:",
            "May Thu Khin",
            "Yunn Yamone Aung",
            "Thu Kha Nyan"
        };

        int fontSizeTitle = 18;
        int fontSizeName = 16;
        int lineSpacing = 24;
        int blockPadding = 20;
        int blockHeight = lineSpacing * members.length + blockPadding;
        int blockWidth = 260;

        int blockX = (d.width - blockWidth) / 2;
        int blockY = d.height - 260; // << moved higher up

        // Draw background + rounded green border
        g.setColor(Color.black);
        g.fillRoundRect(blockX, blockY, blockWidth, blockHeight, 10, 10);
        g.setColor(Color.GREEN);
        g.drawRoundRect(blockX, blockY, blockWidth, blockHeight, 10, 10);

        // Draw team member lines centered
        for (int i = 0; i < members.length; i++) {
            String text = members[i];
            int fontSize = (i == 0) ? fontSizeTitle : fontSizeName;
            g.setFont(g.getFont().deriveFont((float) fontSize));
            FontMetrics fm = g.getFontMetrics();

            int textWidth = fm.stringWidth(text);
            int textX = blockX + (blockWidth - textWidth) / 2;
            int textY = blockY + 28 + i * lineSpacing;

            if (i == 0) {
                g.setColor(Color.WHITE); // title
            } else {
                g.setColor(new Color(255, 204, 0)); // yellow-orange
            }

            g.drawString(text, textX, textY);
        }

        // === "Press SPACE to Start" blinking text ===
        if (frame % 60 < 30) {
            g.setColor(Color.white);
        } else {
            g.setColor(Color.lightGray);
        }

        g.setFont(g.getFont().deriveFont(24f));
        String startText = "Press SPACE to Start";
        int stringWidth = g.getFontMetrics().stringWidth(startText);
        int x = (d.width - stringWidth) / 2;
        g.drawString(startText, x, d.height - 40);

        // === Credit ===
        g.setColor(Color.gray);
        g.setFont(g.getFont().deriveFont(10f));
        g.drawString("Game by Chayapol", 10, d.height - 10);

        Toolkit.getDefaultToolkit().sync();
    }

    private void update() {
        frame++;
    }

    private void doGameCycle() {
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
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                game.loadScene1();
            }
        }
    }
}