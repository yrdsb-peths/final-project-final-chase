import greenfoot.*;

public class PhysicsObject extends Actor {
    public int velocityY = 0;
    public final int gravity = 1;
    public final int maxFallSpeed = 10;
    public boolean onGround = false;

    public void act() {
        applyGravity();
        checkGround();
    }

    public void applyGravity() {
        velocityY += gravity;
        if (velocityY > maxFallSpeed) {
            velocityY = maxFallSpeed;
        }
        setLocation(getX(), getY() + velocityY);
    }

    public void checkGround() {
        Actor ground = getOneIntersectingObject(Ground.class);

        if (ground != null) {
            while (isTouching(Ground.class)) {
                setLocation(getX(), getY() - 1); // move out of ground
            }
            velocityY = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        // Failsafe for falling through world
        if (getY() + getImage().getHeight() / 2 >= getWorld().getHeight()) {
            setLocation(getX(), getWorld().getHeight() - getImage().getHeight() / 2);
            velocityY = 0;
            onGround = true;
        }

    }

    public void checkRoof(){
        Actor roof = getOneIntersectingObject(Roof.class);
        if (roof != null) {
            while (isTouching(Ground.class)) {
                setLocation(getX(), getY() + 1);
            }
            velocityY = 2;
            
        }
    }
    public void checkWall() {
        Actor wall = getOneIntersectingObject(Wall.class);
    
        if (wall != null) {
            // Leeway for side collisions]
            if (Math.abs(getX() - wall.getX()) <= wall.getImage().getWidth() / 2 + getImage().getWidth() / 2) {
                if (getX() < wall.getX()) {
                    // Touching the left side of the wall
                    setLocation(wall.getX() - wall.getImage().getWidth() / 2 - getImage().getWidth() / 2, getY());
                } else {
                    // Touching the right side of the wall
                    setLocation(wall.getX() + wall.getImage().getWidth() / 2 + getImage().getWidth() / 2, getY());
                }
            }
        }
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setVelocityY(int value) {
        this.velocityY = value;
    }

    public int getVelocityY() {
        return velocityY;
    }
}
