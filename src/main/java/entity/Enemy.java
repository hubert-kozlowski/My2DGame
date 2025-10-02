package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Enemy extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    // Simple state for longer, natural movement
    private int stepsRemaining = 0;
    private int idleTicks = 0;

    public Enemy(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getEnemyImage();

    }
    public void setDefaultValues() {
        x = 200;
        y = 200;
        speed = 4;
        direction = "down";
        // initialize movement state
        stepsRemaining = 0;
        idleTicks = 0;
    }
    public void getEnemyImage() {

        try {
            // old man enemy
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/oldman_up_1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/oldman_up_2.png")));
            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/oldman_down_1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/oldman_down_2.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/oldman_left_1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/oldman_left_2.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/oldman_right_1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/enemy/oldman_right_2.png")));
        } catch (Exception e) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, "Failed to load enemy images", e);
        }
    }
    public void update() {

        // Idle occasionally for a short time to look more natural
        if (idleTicks > 0) {
            idleTicks--;
            return;
        }

        // If no steps left, pick a new direction and a longer movement segment
        if (stepsRemaining <= 0) {
            // ~15% chance to pause briefly
            int roll = (int) (Math.random() * 100);
            if (roll < 15) {
                idleTicks = 15 + (int) (Math.random() * 30); // 15..44 frames
                return;
            }

            // Choose a direction and commit to it for multiple frames
            int dir = (int) (Math.random() * 4);
            switch (dir) {
                case 0: direction = "up"; break;
                case 1: direction = "down"; break;
                case 2: direction = "left"; break;
                default: direction = "right"; break;
            }

            stepsRemaining = 20 + (int) (Math.random() * 40); // 20..59 frames
        }

        // Move in the current direction
        switch (direction) {
            case "up":    y -= speed; break;
            case "down":  y += speed; break;
            case "left":  x -= speed; break;
            case "right": x += speed; break;
        }
        stepsRemaining--;

    }
    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

        // sprite animation
        spriteCounter++;
        if (spriteCounter > 12) { // change sprite every 12 frames
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
}
