import greenfoot.*;

public class FireBall extends PlayerAttacks {
    int direction = 0;
    private GreenfootImage fireBall;
    boolean hit = false;
    int frame = 5;
    public GreenfootSound damageSound;
    private GreenfootSound fireBallSound;

    public FireBall(int direction){
        fireBallSound = new GreenfootSound("hero_fireball.mp3");
        damageSound = new GreenfootSound("enemy_damage.mp3");
        fireBall = ImageUtils.scale("attacks/fireBall.png", 150, 70);
        if (direction > 0){
            setImage(fireBall);
        } else if (direction < 0){
            fireBall.mirrorHorizontally();
            setImage(fireBall);
        }
        this.direction = direction;
        fireBallSound.play();
    }

    public void act() {
        move(direction);
        checkHit();
        checkWalls();
    }

    private void checkWalls() {
        if (getX()<10||getX()>990) {
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
                damageSound.play();
                if (enemy instanceof Walker) {  // Check if the enemy is a Walker
                    Walker walker = (Walker) enemy; // Cast to Walker
                    walker.decreaseHealth(30);  // Decrease health by 4
                }
                if (enemy instanceof Aspid) {  // Check if the enemy is a Walker
                    Aspid aspid = (Aspid) enemy; // Cast to Walker
                    aspid.decreaseHealth(30);  // Decrease health by 4
                }
                if (enemy instanceof FalseKnightHurtBox) {
                        ((FalseKnightHurtBox) enemy).takeDamage(10);
                }
                hit = true;
            }
        }
    }
}
