package gdd;

import gdd.scene.GameOverPanel;
import gdd.scene.Scene1;
import gdd.scene.Scene2;
import gdd.scene.Scene3BossFight;
import gdd.scene.TitleScene;
import gdd.scene.WinPanel;
import gdd.sprite.Player;

import javax.swing.JFrame;

public class Game extends JFrame {

    private Player player;
    private HUD hud;

    private TitleScene titleScene;
    private Scene1 scene1;

    public Game() {
        initUI();
        loadTitle();  // Start with title screen
    }

    private void initUI() {
        setTitle("Space Invaders");
        setSize(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void loadTitle() {
        titleScene = new TitleScene(this);
        getContentPane().removeAll();
        add(titleScene);
        titleScene.start();
        revalidate();
        repaint();
    }

    public void loadScene1() {
        // ✅ Initialize Player and HUD here
        player = new Player();
        hud = new HUD(player);

        scene1 = new Scene1(this); // Construct with Game reference
        getContentPane().removeAll();  // Clear titleScene
        add(scene1);
        scene1.start();
        revalidate();
        repaint();
    }

    public void loadScene2() {
        // ✅ Reuse existing player and hud
        Scene2 scene2 = new Scene2(this, player, hud);
        getContentPane().removeAll();
        if (scene1 != null) scene1.stop();  // Stop Scene1 loop if running
        add(scene2);
        scene2.start();
        revalidate();
        repaint();
    }

    public void loadScene3() {
        Scene3BossFight bossScene = new Scene3BossFight(this, player, hud);
        getContentPane().removeAll();
        setContentPane(bossScene);
        revalidate();
        repaint();
        bossScene.start(); // ✅ Must be called after panel is shown!
    }         

    public void loadGameOverPanel() {
        GameOverPanel panel = new GameOverPanel(this);
        getContentPane().removeAll();
        setContentPane(panel);
        revalidate();
        panel.requestFocusInWindow();
    }

    public void loadWinPanel() {
        WinPanel winPanel = new WinPanel(this);
        getContentPane().removeAll();
        setContentPane(winPanel);
        revalidate();
        winPanel.requestFocusInWindow();
    }
}