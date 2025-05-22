import greenfoot.*;

public class Ground extends Actor {
    public Ground(int width, int height) {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(50, 255, 50, 0)); // Transparent ground
        img.fill();
        setImage(img);
    }
}
