import greenfoot.*;

public class Walker extends Enemy {
    private int velocityY = 0;
    private final int gravity = 1;
    private final int maxFallSpeed = 10;
    private boolean onGround = false;
    private GreenfootImage idleImage;
    
    public Walker(){
        idleImage = (ImageUtils.scale("enemies/walker/walkerIdle.png", 50, 60));
        setImage(idleImage);
    }

    public void act() {
        applyGravity();
        checkGround();
        //move(-1);
    }

    private void applyGravity() {
        velocityY += gravity;
        if (velocityY > maxFallSpeed) {
            velocityY = maxFallSpeed;
        }
        setLocation(getX(), getY() + velocityY);
    }

    private void checkGround() {
        World world = getWorld();
        if (isTouching(Ground.class)) {
            while (isTouching(Ground.class)) {
                setLocation(getX(), getY() - 1);
            }
            velocityY = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        if (getY() + getImage().getHeight() / 2 >= world.getHeight()) {
            setLocation(getX(), world.getHeight() - getImage().getHeight() / 2);
            velocityY = 0;
            onGround = true;
        }
    }
}
