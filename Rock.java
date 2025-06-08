import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;

public class Rock extends Enemy {
    private GreenfootImage rock;
    private int delay;
    private Random rand;
    private int randomNum;

    public Rock() {
        rand = new Random();
        rock = new GreenfootImage("enemies/rock/blob.png");
        rock.scale(50, 50);
        setImage(rock);
        randomNum = rand.nextInt(100);
        delay = 0;
    }

    public void act() {
        World world = getWorld();
        delay++;
        if (delay > randomNum) {
            applyGravity();
        }
        if(getY()>590){
            world.removeObject(this);
        }
    }
}
