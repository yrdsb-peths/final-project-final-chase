import greenfoot.*;
import java.util.Random;
import java.util.List;

public class FalseKnight extends PhysicsObject {
    int health = 200;
    int hitFrames;

    // Animation frames
    private GreenfootImage[] idleImages;
    private GreenfootImage[] attackImages;
    private GreenfootImage[] jumpImages;
    private GreenfootImage hitImage;
    private GreenfootImage[] staggerImages;
    private GreenfootImage[] unstaggerImages;
    private GreenfootImage[] deadImages;

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

    // Horizontal speed midair
    private static final int MIDAIR_HORIZONTAL_SPEED = 5;

    // -1 for left, 1 for right
    private int midairDirection = 0;
    private boolean dead = false;

    private boolean hasSpawnedRock = false; // NEW: prevent multiple spawns per slam

    private GreenfootSound deathSound;
    private GreenfootSound slam;
    private GreenfootSound land;

    public FalseKnight() {
        deathSound = new GreenfootSound("Boss Defeat.mp3");
        slam = new GreenfootSound("false_knight_strike_ground.mp3");
        land = new GreenfootSound("false_knight_land.mp3");
        loadAnimations();
        setImage(idleImages[0]);
    }

    private void loadAnimations() {
        idleImages = new GreenfootImage[3];
        attackImages = new GreenfootImage[4];
        jumpImages = new GreenfootImage[5];
        deadImages = new GreenfootImage[3];

        deadImages[0] = ImageUtils.scale("enemies/falseKnight/die1.png", 255, 250);
        deadImages[1] = ImageUtils.scale("enemies/falseKnight/die2.png", 255, 250);
        deadImages[2] = ImageUtils.scale("enemies/falseKnight/die3.png", 255, 275);

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
        if (!dead) {
            if (hitFrames > 0) {
                hitFrames--;

                if (health <= 0 && hitFrames == 0) {
                    World world = getWorld();
                    if (world != null) {
                        die();
                        List<FalseKnightHurtBox> boxes = world.getObjects(FalseKnightHurtBox.class);
                        for (int i = 0; i < boxes.size(); i++) {
                            world.removeObject(boxes.get(i));
                        }
                    }
                }
                return;
            }

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
                    midairDirection = 0;
                }
            } else {
                animateIdle();
                if (mainFrame % (health+1 / 10) == 0) {
                    doRandomAction();
                }
            }
        } else {
            applyGravity();
            checkGround();
            checkWall();
            if (mainFrame < 20) {
                mainFrame++;
            }
            setImage(deadImages[mainFrame / 10]);
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
        for (GreenfootImage img : idleImages) img.mirrorHorizontally();
        for (GreenfootImage img : attackImages) img.mirrorHorizontally();
        for (GreenfootImage img : jumpImages) img.mirrorHorizontally();
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
                slam.play();
            }
        }
    }

    private void jump() {
        if (isOnGround()) {
            MyWorld world = (MyWorld) getWorld();
            if (world == null) return;
            Player player = world.getPlayer();
            if (player == null) return;

            midairDirection = player.getX() > getX() ? 1 : -1;
            if ((midairDirection == 1 && !facingRight) || (midairDirection == -1 && facingRight)) {
                facingRight = !facingRight;
                flipAllImages();
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

            midairDirection = player.getX() > getX() ? 1 : -1;
            if ((midairDirection == 1 && !facingRight) || (midairDirection == -1 && facingRight)) {
                facingRight = !facingRight;
                flipAllImages();
            }

            setVelocityY(SLAM_FORCE);
            isSlamming = true;
            jumpFrame = 0;
            slamFrame = 0;
            hasSpawnedRock = false; // reset rock spawn flag on new slam
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
                midairDirection = 0;
            }
        }
    }

    private void moveMidairHorizontally() {
        if (midairDirection == 0) return;
        setLocation(getX() + (midairDirection * MIDAIR_HORIZONTAL_SPEED), getY());
    }

    private void checkLandingResetDirection() {
        if (isOnGround() && !hasSpawnedRock) {
            MyWorld world = (MyWorld) getWorld();
            land.play();
            // Spawn 3 rocks
            world.spawnRock();
            world.spawnRock();
            world.spawnRock();
            hasSpawnedRock = true;
            midairDirection = 0;
        }
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        hitFrames = 2;

        List<Player> players = getWorld().getObjects(Player.class);
        if (!players.isEmpty()) {
            Player player = players.get(0);
            player.checkPogo();
            player.addSoul(5);
        }
    }

    public void die() {
        deathSound.play();
        World world = getWorld();
        setVelocityY(-15);
        dead = true;
        mainFrame = 0;

        if (world != null) {
            ((MyWorld) world).killedFK();
        }
    }
}
