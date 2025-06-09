import greenfoot.*;  //(World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;

public class Rock extends Enemy {

    //Rock image and animation delay
    private GreenfootImage rock;
    private int delay;

    //Random number generator and delay limit
    private Random rand;
    private int randomNum;

    //Constructor
    public Rock() {
        //Load and scale rock image
        rock = new GreenfootImage("enemies/rock/blob.png");
        rock.scale(50, 50);
        setImage(rock);

        //Initialize random delay so rocks fall at different times
        rand = new Random();
        randomNum = rand.nextInt(100);

        delay = 0;
    }

    //main loop
    public void act() {
        World world = getWorld();

        delay++;

        //fall after random delay
        if (delay > randomNum) {
            applyGravity();
        }

        //Remove rock if it reaches bottom of the screen
        if (getY() > 590) {
            world.removeObject(this);
        }
    }
}
