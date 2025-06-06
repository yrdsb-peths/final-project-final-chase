import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class FalseKnight extends Enemy
{
    private GreenfootImage[] idleImages;
    private GreenfootImage[] attackImages;
    private GreenfootImage[] jumpImages;
    private GreenfootImage[] stutterImages;
    private int idleAnimationFrames = 0;
    private int swingFrame = 0;

    public FalseKnight() {
        // Initialize arrays
        idleImages = new GreenfootImage[3];
        attackImages = new GreenfootImage[3];

        // Load idle frames
        idleImages[0] = ImageUtils.scale("enemies/falseKnight/idle1.png", 400, 275);
        idleImages[1] = ImageUtils.scale("enemies/falseKnight/idle2.png", 400, 275);
        idleImages[2] = ImageUtils.scale("enemies/falseKnight/idle3.png", 400, 275);

        // Load attack frames
        attackImages[0] = ImageUtils.scale("enemies/falseKnight/swing1.png", 400, 275);
        attackImages[1] = ImageUtils.scale("enemies/falseKnight/swing2.png", 400, 275);
        attackImages[2] = ImageUtils.scale("enemies/falseKnight/swing3.png", 400, 275);

        setImage(idleImages[0]);
    }

    public void act() {
        applyGravity();
        checkGround();
        checkWall();
        idleAnim();
        //swing();
    }

    public void idleAnim() {
        idleAnimationFrames++;
        setImage(idleImages[(idleAnimationFrames / 10) % 3]);
    }
    public void swing(){
        swingFrame++;
        setImage(attackImages[(swingFrame / 10) % 3]);
    }
}
