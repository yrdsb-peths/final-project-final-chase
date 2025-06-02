import greenfoot.*;

public class Wall extends WorldBlocks {
    public Wall(int width, int height) {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(255, 100, 100, 50));
        img.fill();
        setImage(img);
    } 
}
