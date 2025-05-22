import greenfoot.*;

public class Wall extends Actor {
    public Wall(int width, int height) {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(255, 100, 100, 0)); // Transparent ground
        img.fill();
        setImage(img);
    } 
}
