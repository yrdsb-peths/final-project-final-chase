import greenfoot.*;
import java.util.Random;
import java.util.List;

public class FalseKnight extends PhysicsObject {
    int health;
    int hitFrames;
    
    // Animation frames
    private GreenfootImage[] idleImages;
    private GreenfootImage[] attackImages;
    private GreenfootImage[] jumpImages;
    private GreenfootImage hitImage;

    // Frame counters
    private int idleFrame = 0;
    private int swingFrame = 0;
    private int jumpFrame = 0;
    private int slamFrame = 0;
    private int mainFrame = 0;

    // Animation speed (frames per image)
    private static final int IDLE_ANIM_SPEED = 20;
    private static final int ATTACK_ANIM_SPEED = 15;
    private static final int JUMP_ANIM_SPEED = 10;
    private static final int SLAM_HOLD_FRAMES = 15;
    private static final int SLAM_FORCE = -20;

    // States
    private boolean isSwinging = false;
    private boolean isJumping = false;
    private boolean isSlamming = false;
    private boolean facingRight = true;

    private Random rand = new Random();

    //Horizontal speed midair
    private static final int MIDAIR_HORIZONTAL_SPEED = 3;

    //-1 for left, 1 for right
    private int midairDirection = 0;

    public FalseKnight() {
        loadAnimations();
        setImage(idleImages[0]);
    }

    private void loadAnimations() {
        idleImages = new GreenfootImage[3];
        attackImages = new GreenfootImage[4];
        jumpImages = new GreenfootImage[5];

        
        idleImages[0] = ImageUtils.scale("enemies/falseKnight/idle1.png", 450, 225);
        idleImages[1] = ImageUtils.scale("enemies/falseKnight/idle2.png", 450, 225);
        idleImages[2] = ImageUtils.scale("enemies/falseKnight/idle3.png", 450, 225);
        
        hitImage = ImageUtils.scale("enemies/falseKnight/hit.png", 450, 225);
        
        
        attackImages[0] = ImageUtils.scale("enemies/falseKnight/swing1.png", 450, 225);
        attackImages[1] = ImageUtils.scale("enemies/falseKnight/swing2.png", 450, 225);
        attackImages[2] = ImageUtils.scale("enemies/falseKnight/swing3.png", 400, 225);
        attackImages[3] = ImageUtils.scale("enemies/falseKnight/swing4.png", 450, 225);

        
        jumpImages[0] = ImageUtils.scale("enemies/falseKnight/jump1.png", 475, 225);
        jumpImages[1] = ImageUtils.scale("enemies/falseKnight/jump2.png", 450, 225);
        jumpImages[2] = ImageUtils.scale("enemies/falseKnight/jump3.png", 450, 250);
        jumpImages[3] = ImageUtils.scale("enemies/falseKnight/jump4.png", 400, 225);
        jumpImages[4] = ImageUtils.scale("enemies/falseKnight/jump5.png", 400, 225);
    }

    public void act() {
        mainFrame++;
        applyGravity();
        checkGround();
        checkWall();

        if (isSwinging) {
            animateSwing();
        } else if (isSlamming) {
            animateSlam();
            moveMidairHorizontally();
            checkLandingResetDirection();
        } else if (isJumping) {
            animateJump();
            moveMidairHorizontally();
            if (isOnGround()) {
                isJumping = false;
                jumpFrame = 0;
                midairDirection = 0;  // reset direction on landing
            }
        } else {
            animateIdle();

            if (mainFrame % 180 == 0) {
                doRandomAction();
            }
        }
    }

    private void doRandomAction() {
        turnTowardPlayer();

        int action = rand.nextInt(2); // 0 = swing, 1 = slam
        if (action == 0) {
            startSwing();
        } else {
            slam();
        }
    }

    private void turnTowardPlayer() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null) return;

        Player player = world.getPlayer();
        if (player == null) return;

        boolean shouldFaceRight = player.getX() > getX();
        if (shouldFaceRight != facingRight) {
            facingRight = shouldFaceRight;
            flipAllImages();
        }
    }

    private void flipAllImages() {
        for (int i = 0; i < idleImages.length; i++) {
            idleImages[i].mirrorHorizontally();
        }
        for (int i = 0; i < attackImages.length; i++) {
            attackImages[i].mirrorHorizontally();
        }
        for (int i = 0; i < jumpImages.length; i++) {
            jumpImages[i].mirrorHorizontally();
        }
    }

    private void animateIdle() {
        idleFrame++;
        int index = (idleFrame / IDLE_ANIM_SPEED) % idleImages.length;
        setImage(idleImages[index]);
    }

    private void startSwing() {
        isSwinging = true;
        swingFrame = 0;
    }

    private void animateSwing() {
        int index = swingFrame / ATTACK_ANIM_SPEED;

        if (index < attackImages.length) {
            setImage(attackImages[index]);
            swingFrame++;
        } else {
            isSwinging = false;
            swingFrame = 0;
            MyWorld world = (MyWorld) getWorld();
            if (world != null) {
                int waveDirection = facingRight ? 5 : -5;
                world.spawnWave(waveDirection, getX(), getY() + 60);
            }
        }
    }

    private void jump() {
        if (isOnGround()) {
            MyWorld world = (MyWorld) getWorld();
            if (world == null) return;
            Player player = world.getPlayer();
            if (player == null) return;

            // Pick direction once when jump starts
            if (player.getX() > getX()) {
                midairDirection = 1;
                if (!facingRight) {
                    facingRight = true;
                    flipAllImages();
                }
            } else {
                midairDirection = -1;
                if (facingRight) {
                    facingRight = false;
                    flipAllImages();
                }
            }

            setVelocityY(-20);
            isJumping = true;
            jumpFrame = 0;
        }
    }

    private void animateJump() {
        if (mainFrame % JUMP_ANIM_SPEED == 0) {
            jumpFrame = (jumpFrame + 1) % jumpImages.length;
        }
        setImage(jumpImages[jumpFrame]);
    }

    private void slam() {
        if (isOnGround()) {
            MyWorld world = (MyWorld) getWorld();
            if (world == null) return;
            Player player = world.getPlayer();
            if (player == null) return;

            // Pick direction once when slam starts
            if (player.getX() > getX()) {
                midairDirection = 1;
                if (!facingRight) {
                    facingRight = true;
                    flipAllImages();
                }
            } else {
                midairDirection = -1;
                if (facingRight) {
                    facingRight = false;
                    flipAllImages();
                }
            }

            setVelocityY(SLAM_FORCE);
            isSlamming = true;
            jumpFrame = 0;
            slamFrame = 0;
        }
    }

    private void animateSlam() {
        if (jumpFrame < jumpImages.length - 1) {
            if (mainFrame % JUMP_ANIM_SPEED == 0) {
                jumpFrame++;
            }
        }

        setImage(jumpImages[jumpFrame]);

        if (jumpFrame == jumpImages.length - 1) {
            if (slamFrame < SLAM_HOLD_FRAMES) {
                slamFrame++;
            } else if (isOnGround()) {
                isSlamming = false;
                jumpFrame = 0;
                midairDirection = 0;  // reset direction on landing
            }
        }
    }

    private void moveMidairHorizontally() {
        if (midairDirection == 0) return; // no movement direction set

        setLocation(getX() + (midairDirection * MIDAIR_HORIZONTAL_SPEED), getY());
    }

    private void checkLandingResetDirection() {
        if (isOnGround()) {
            midairDirection = 0;
        }
    }
    public void decreaseHealth(int amount) {
        health -= amount;
        hitFrames = 5;
        setImage(hitImage);

        List<Player> players = getWorld().getObjects(Player.class);
        if (!players.isEmpty()) {
            Player player = players.get(0);
            player.checkPogo();
            player.addSoul(15);
        }
    }
}
