import greenfoot.*;

//Sword slash effect triggered by player
public class Swing extends PlayerAttacks {

    //animation frames
    private GreenfootImage[] frames = new GreenfootImage[3];
    private int currentFrame = 0;
    private int frameDelay = 0;

    //tracking hit so it doesn’t hit more than once
    private boolean hit = false;

    //SFX
    public GreenfootSound damageSound;
    private GreenfootSound sword;

    //constructor
    public Swing() {
        for (int i = 0; i < 3; i++) {
            frames[i] = new GreenfootImage("images/attacks/swing" + (i + 1) + ".png");
        }

        //override with scaled swing image
        frames[0] = ImageUtils.scale("attacks/leftswing.png", 120, 60);
        setImage(frames[0]);

        damageSound = new GreenfootSound("enemy_damage.mp3");
        sword = new GreenfootSound("sword_1.mp3");
        sword.play();
    }

    //main loop
    public void act() {
        frameDelay++;

        if (frameDelay >= 5) {
            frameDelay = 0;
            currentFrame++;

            checkHit();

            //remove after anim ends
            if (currentFrame >= 2) {
                getWorld().removeObject(this);
            }
        }
    }

    //rotates swing direction based on input
    //shitty name though
    public void setRotationBasedOnDirection(String dir) {
        if (dir.equals("right")) setRotation(0);
        if (dir.equals("left")) setRotation(180);
        if (dir.equals("up")) setRotation(270);
        if (dir.equals("down")) setRotation(90);
    }

    //checks for enemy contact & deals damage
    public void checkHit() {
        if (!hit) {
            Actor enemy = getOneIntersectingObject(Enemy.class);
            if (enemy != null) {
                damageSound.play();

                if (enemy instanceof FalseKnightHurtBox) {
                    ((FalseKnightHurtBox) enemy).takeDamage(4);
                } else if (enemy instanceof Walker) {
                    ((Walker) enemy).decreaseHealth(4);
                } else if (enemy instanceof Aspid) {
                    ((Aspid) enemy).decreaseHealth(4);
                } else if (enemy instanceof AspidBullet) {
                    ((AspidBullet) enemy).decreaseHealth(4);
                }

                hit = true;
            }
        }
    }
}
