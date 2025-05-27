import greenfoot.*;

public class FireBall extends PlayerAttacks {
    int direction = 0;
    private GreenfootImage fireBall;
    boolean hit = false;
    public FireBall(int direction){
        fireBall = ImageUtils.scale("attacks/fireBall.png", 150, 70);
        if (direction > 0){
            setImage(fireBall);
        } else if (direction < 0){
            fireBall.mirrorHorizontally();
            setImage(fireBall);
        }
        this.direction = direction;
    }

    public void act() {
        move(direction);
        checkHit();
        checkWalls();
    }

    private void checkWalls() {
        if (isAtEdge()) {
            World world = getWorld();
            if (world != null) {
                world.removeObject(this);
            }
        }
    }

    public void checkHit() {
        if (isTouching(Enemy.class) && hit == false) {
            // Get the enemy object that was touched
            Enemy enemy = (Enemy) getOneIntersectingObject(Enemy.class);
            
            if (enemy != null) {
                // Reduce the health of the touched enemy
                if (enemy instanceof Walker) {  // Check if the enemy is a Walker
                    Walker walker = (Walker) enemy; // Cast to Walker
                    walker.decreaseHealth(30);  // Decrease health by 4
                }
            }
            hit = true;
        }
    }
}
