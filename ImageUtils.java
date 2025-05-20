import greenfoot.*;
public class ImageUtils {
    //ImageUtils.scale(imageName,x,y);
    //idleImages[0] = ImageUtils.scale("player/idle.png", 30, 60);
    public static GreenfootImage scale(String imageName, int sizeX, int sizeY) {
        GreenfootImage img = new GreenfootImage(imageName);
        img.scale(sizeX, sizeY);
        return img;
    }
}
