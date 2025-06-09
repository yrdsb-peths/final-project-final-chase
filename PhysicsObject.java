import greenfoot.*;

public class PhysicsObject extends Actor {
    public int velocityY = 0;
    public final int gravity = 1;
    public final int maxFallSpeed = 10;
    public boolean onGround = false;

    //main loop
    public void act() {
        applyGravity();
        checkGround();
    }

    //applies gravity and fall speed limit
    public void applyGravity() {
        velocityY += gravity;
        if (velocityY > maxFallSpeed) {
            velocityY = maxFallSpeed;
        }
        setLocation(getX(), getY() + velocityY);
    }

    //checks for ground collision
    public void checkGround() {
        Actor ground = getOneIntersectingObject(Ground.class);

        if (ground != null) {
            while (isTouching(Ground.class)) {
                setLocation(getX(), getY() - 1);
            }
            velocityY = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        if (getY() + getImage().getHeight() / 2 >= getWorld().getHeight()) {
            setLocation(getX(), getWorld().getHeight() - getImage().getHeight() / 2);
            velocityY = 0;
            onGround = true;
        }
    }

    //checks for ceiling collision
    public void checkRoof() {
        Actor roof = getOneIntersectingObject(Roof.class);
        if (roof != null) {
            while (isTouching(Ground.class)) {
                setLocation(getX(), getY() + 1);
            }
            velocityY = 2;
        }
    }

    //checks for left/right wall collisions
    public void checkWall() {
        Actor wall = getOneIntersectingObject(Wall.class);
        if (wall != null) {
            if (Math.abs(getX() - wall.getX()) <= wall.getImage().getWidth() / 2 + getImage().getWidth() / 2) {
                if (getX() < wall.getX()) {
                    setLocation(wall.getX() - wall.getImage().getWidth() / 2 - getImage().getWidth() / 2, getY());
                } else {
                    setLocation(wall.getX() + wall.getImage().getWidth() / 2 + getImage().getWidth() / 2, getY());
                }
            }
        }
    }

    //returns true if touching ground
    public boolean isOnGround() {
        return onGround;
    }

    //sets vertical speed (neg is up)
    public void setVelocityY(int value) {
        this.velocityY = value;
    }

    //gets vertical speed (neg is up)
    public int getVelocityY() {
        return velocityY;
    }
}
