package main;

import entity.Player;
import entity.Enemy;

import javax.swing.JPanel;
import java.awt.*;
import java.sql.SQLOutput;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12; // 16:9 aspect ratio
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    //FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);
    Enemy enemy = new Enemy(this, keyH);

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // for better rendering performance
        this.addKeyListener(keyH);
        this.setFocusable(true); // to be able to receive key input

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

//    @Override
//    public void run() {
//
//        double drawInterval = (double) 1000000000 / FPS; // 1 second, in nanoseconds, divided by 60, 0.01666667 seconds
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//
//        while (gameThread != null) {
//            // 1 update: update information such as character positions
//            update();
//
//
//            // 2 draw: draw the screen with the updated information
//            repaint();
//
//
//
//            try{
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime/1000000; // convert to milliseconds
//
//                if (remainingTime < 0) {
//                    remainingTime = 0; // if the update and draw took longer than the draw interval, skip the sleep
//                }
//
//                Thread.sleep((long) remainingTime); // convert to milliseconds
//
//                nextDrawTime += drawInterval;
//
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void run() {

        double drawInterval = (double) 1000000000 / FPS; // 1 second, in nanoseconds, divided by 60, 0.01666667 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;


            if (delta >= 1) {
                // 1 update: update information such as character positions
                update();
                // 2 draw: draw the screen with the updated information
                repaint();
                delta--;

                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        // update game state
        player.update();

        enemy.update();
    }

    public void paintComponent(Graphics g) { // literally a built-in method
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Camera: translate so the player is centered on screen
        int offsetX = (screenWidth / 2) - (tileSize / 2) - player.x;
        int offsetY = (screenHeight / 2) - (tileSize / 2) - player.y;
        AffineTransform oldTx = g2.getTransform();
        g2.translate(offsetX, offsetY);

        // draw world-space entities
        player.draw(g2);
        enemy.draw(g2);

        // restore transform
        g2.setTransform(oldTx);

        g2.dispose(); // to free up memory


    }
}
