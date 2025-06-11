import greenfoot.*;
public class BlackBox extends Actor
{
    public BlackBox(int width, int height) {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(0, 0, 0, 255));
        img.fill();
        setImage(img);
    }
}
