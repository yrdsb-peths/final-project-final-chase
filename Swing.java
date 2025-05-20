import greenfoot.*;

public class Swing extends Actor {
    private GreenfootImage[] frames = new GreenfootImage[3];
    private int currentFrame = 0;
    private int frameDelay = 0;
    public Swing() {
        for (int i = 0; i < 3; i++) {
            frames[i] = new GreenfootImage("images/attacks/swing" + (i + 1) + ".png");
        }
        frames[0] = ImageUtils.scale("attacks/leftswing.png", 120, 60);
        setImage(frames[0]);
    }

    public void act() {
        frameDelay++;
        if (frameDelay >= 5) {
            frameDelay = 0;
            currentFrame++;//change anim frame
            checkHit();
            if (currentFrame >= 2) {
                getWorld().removeObject(this);
            }
        }
    }

    public void setRotationBasedOnDirection(String dir) {
        if (dir.equals("right")) setRotation(0);
        if (dir.equals("left")) setRotation(180);
        if (dir.equals("up")) setRotation(270);
        if (dir.equals("down")) setRotation(90);
    }
    public void checkHit(){
        if (isTouching(Enemy.class)) {
            MyWorld world = (MyWorld) getWorld();
            world.increaseScore();  // Increase score
        }
    }
}
