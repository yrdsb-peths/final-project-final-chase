import greenfoot.*;

public class Ground extends WorldBlocks {
    public Ground(int width, int height) {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(50, 255, 50, 100));
        img.fill();
        setImage(img);
    }
}
