import greenfoot.*;

public class Transition extends WorldBlocks {
    public int screen = -1;
    public Transition(int width, int height, int inputScreen) {
        screen = inputScreen;
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(255, 50, 255, 100));
        img.fill();
        setImage(img);
    }
    public int getScreen(){
        return(screen);
    }

}
