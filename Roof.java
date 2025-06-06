import greenfoot.*;

public class Roof extends WorldBlocks {
    public Roof(int width, int height) {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(50, 50, 255, 100));
        img.fill();
        setImage(img);
    }
}
