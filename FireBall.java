import greenfoot.*;

//Fireball projectile fired by the player
public class FireBall extends PlayerAttacks {

    //direction of travel
    int direction = 0;

    //image/sprite
    private GreenfootImage fireBall;

    //tracking if it already hit an enemy
    boolean hit = false;

    //SFX
    public GreenfootSound damageSound;
    private GreenfootSound fireBallSound;

    //constructor
    public FireBall(int direction) {
        fireBallSound = new GreenfootSound("hero_fireball.mp3");
        damageSound = new GreenfootSound("enemy_damage.mp3");
        fireBall = ImageUtils.scale("attacks/fireBall.png", 150, 70);

        //flip sprite if going left
        if (direction < 0) {
            fireBall.mirrorHorizontally();
        }
        setImage(fireBall);

        this.direction = direction;
        fireBallSound.play();
    }

    //main loop
    public void act() {
        move(direction);
        checkHit();
        checkWalls();
    }

    //removes fireball if it goes offscreen
    private void checkWalls() {
        if (getX() < 10 || getX() > 990) {
            World world = getWorld();
            if (world != null) world.removeObject(this);
        }
    }

    //checks for enemy collision
    public void checkHit() {
        if (!hit && isTouching(Enemy.class)) {
            Enemy enemy = (Enemy) getOneIntersectingObject(Enemy.class);

            if (enemy != null) {//nullcheck
                damageSound.play();

                //def a better way to do this but whatev
                if (enemy instanceof Walker) {
                    ((Walker) enemy).decreaseHealth(30);
                }
                if (enemy instanceof Aspid) {
                    ((Aspid) enemy).decreaseHealth(30);
                }
                if (enemy instanceof FalseKnightHurtBox) {
                    ((FalseKnightHurtBox) enemy).takeDamage(10);
                }

                hit = true;//prevent multihits
            }
        }
    }
}
