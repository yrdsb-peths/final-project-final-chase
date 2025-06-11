import greenfoot.*;
import java.util.Random;

public class Player extends PhysicsObject {

    //misc movement
    private final int jumpStrength = -15;
    private boolean onGround = false;
    public String directionFacing = "left";

    //Sprites
    private GreenfootImage[] idleImages;
    private GreenfootImage[] runImages;
    private GreenfootImage[] dashImages;
    private GreenfootImage attackImage;
    private GreenfootImage dashImage;

    //Run animation
    private int runFrame = 0;
    private int runCounter = 0;
    private final int runDelay = 5;

    //Jumping
    private boolean isJumping = false;
    private int jumpTimer = 0;
    private final int maxJumpTime = 8;

    //Dash ability
    private boolean isDashing = false;
    private int dashTimer = 0;
    private final int dashDuration = 15;
    private final int dashSpeed = 12;
    public int dashCooldown = 0;

    //attack/cooldown
    private boolean isAttacking = false;
    private int attackDisplayCounter = 0;
    private final int attackDisplayTime = 10;
    private boolean swingSpawned = false;
    private int swingCooldown = 0;
    private final int swingCooldownTime = 20;
    private final int swingDistance = 50;

    //Health/Soul
    public int health = 100;
    public int soul = 30;
    private int focusTimer = 0;

    //SFX
    private GreenfootSound dashSound;
    private GreenfootSound footstepsSound;
    private GreenfootSound focusStart;
    private GreenfootSound focus;

    //Death
    private GreenfootImage[] deathImages;
    int deathFrames = 0;
    boolean isDead = false;
    private GreenfootSound death;
    
    //misc


    //constructor
    public Player() {
        death = new GreenfootSound("hero_death.mp3");

        deathImages = new GreenfootImage[6];
        for (int i = 0; i < deathImages.length; i++) {
            deathImages[i] = new GreenfootImage(ImageUtils.scale("player/death/death" + (i + 1) + ".png", 37, 60));
        }

        focus = new GreenfootSound("focus_health_charging.mp3");
        focusStart = new GreenfootSound("focus_ready.mp3");
        dashSound = new GreenfootSound("dash.mp3");
        footstepsSound = new GreenfootSound("footsteps.mp3");

        idleImages = new GreenfootImage[1];
        idleImages[0] = ImageUtils.scale("player/idle.png", 30, 60);

        runImages = new GreenfootImage[4];
        for (int i = 0; i < runImages.length; i++) {
            runImages[i] = ImageUtils.scale("player/run/run" + (i + 1) + ".png", 37, 60);
        }

        attackImage = new GreenfootImage(ImageUtils.scale("player/attacks/swing3.png", 55, 55));

        dashImages = new GreenfootImage[3];
        dashImages[0] = ImageUtils.scale("player/dash/dash1.png", 150, 50);
        dashImages[1] = ImageUtils.scale("player/dash/dash2.png", 110, 50);
        dashImages[2] = ImageUtils.scale("player/dash/dash3.png", 80, 50);
        dashImage = dashImages[0];

        GreenfootImage start = new GreenfootImage(idleImages[0]);
        start.mirrorHorizontally();
        setImage(start);
    }

    //main loop
    public void act() {
        if (!isDead) {
            if (isDashing) {
                dash();
                checkWall();
                return;
            }

            checkKeys();
            applyGravity();
            checkGround();
            checkWall();
            checkRoof();

            if (attackDisplayCounter > 0) {
                attackDisplayCounter--;
                GreenfootImage img = new GreenfootImage(attackImage);
                if (directionFacing.equals("left")) img.mirrorHorizontally();
                setImage(img);
            } else {
                animateRunOrIdle();
            }

            if (swingCooldown > 0) swingCooldown--;
            if (dashCooldown > 0) dashCooldown--;

            checkHit();
            if(soul>100){
                soul = 100;
            }
            MyWorld world = (MyWorld) getWorld();
            world.setScore(health);
            world.setSoul(soul);

            if (health < 1) {
                death.play();
                isDead = true;
                world.deathText();
            }
        } else {
            MyWorld world = (MyWorld) getWorld();
            die();
            if (Greenfoot.isKeyDown("space")) {
                world.clearScreen();
                world.screen1();
                health = 100;
                soul = 30;
                isDead = false;
            }
            if (Greenfoot.isKeyDown("shift")) {
                world.clearAll();
                world.startMenu();
            }
        }
    }

    //General input handling
    private void checkKeys() {
        if (Greenfoot.isKeyDown("a")) {
            focusStart.play();
            focusTimer++;
            if (focusTimer > 20) {
                focus();
            } else {
                focus.stop();
            }
        } else {
            if (focusTimer > 0 && focusTimer < 20) {
                if (soul >= 30) {
                    spawnFireBall();
                }
            }
            focusTimer = 0;
        }

        if (Greenfoot.isKeyDown("c") && !isDashing && dashCooldown <= 0) {
            isDashing = true;
            dashTimer = dashDuration;
            swingSpawned = false;
            dashCooldown = 20;
            dashSound.play();
            return;
        }

        if ((Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("right")) && velocityY < 2) {
            if (!footstepsSound.isPlaying()) footstepsSound.playLoop();
        } else {
            if (footstepsSound.isPlaying()) footstepsSound.stop();
        }

        if (Greenfoot.isKeyDown("left")) {
            move(-5);
            directionFacing = "left";
            swingSpawned = false;
        }

        if (Greenfoot.isKeyDown("right")) {
            move(5);
            directionFacing = "right";
            swingSpawned = false;
        }

        if (isOnGround() && Greenfoot.isKeyDown("space")) {
            isJumping = true;
            jumpTimer = 0;
            onGround = false;
            setVelocityY(20);
            setImage(ImageUtils.scale("player/jump.png", 40, 60));
        }

        if (isJumping && Greenfoot.isKeyDown("space")) {
            if (jumpTimer < maxJumpTime) {
                setVelocityY(-14);
                jumpTimer++;
            } else {
                isJumping = false;
            }
        }

        if (!Greenfoot.isKeyDown("space") || isTouching(Roof.class)) {
            isJumping = false;
        }

        if (Greenfoot.isKeyDown("x") && !swingSpawned && swingCooldown == 0) {
            attackDisplayCounter = attackDisplayTime;
            spawnSwing();
            swingSpawned = true;
            swingCooldown = swingCooldownTime;
        }

        if (!Greenfoot.isKeyDown("x")) {
            swingSpawned = false;
        }
    }

    //Switches between running or idle animations
    private void animateRunOrIdle() {
        if (Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("right")) {
            runCounter++;
            if (runCounter >= runDelay) {
                runCounter = 0;
                runFrame = (runFrame + 1) % runImages.length;
            }
            GreenfootImage img = new GreenfootImage(runImages[runFrame]);
            if (directionFacing.equals("left")) img.mirrorHorizontally();
            setImage(img);
        } else {
            GreenfootImage idle = new GreenfootImage(idleImages[0]);
            if (directionFacing.equals("left")) idle.mirrorHorizontally();
            setImage(idle);
        }
    }

    //dash movement/animation
    private void dash() {
        dashTimer--;
        int dx = directionFacing.equals("right") ? dashSpeed : -dashSpeed;
        setLocation(getX() + dx, getY());

        int dashFrame = (dashDuration - dashTimer) * dashImages.length / dashDuration;
        dashFrame = Math.min(dashFrame, dashImages.length - 1);

        GreenfootImage img = new GreenfootImage(dashImages[dashFrame]);
        if (directionFacing.equals("left")) img.mirrorHorizontally();
        setImage(img);

        if (dashTimer <= 0) {
            isDashing = false;
            setVelocityY(0);
        }
    }

    //spawns a sword swing
    private void spawnSwing() {
        String dir = directionFacing;
        if (Greenfoot.isKeyDown("up")) dir = "up";
        else if (Greenfoot.isKeyDown("down") && !onGround) dir = "down";

        Swing swing = new Swing();
        int spawnX = getX();
        int spawnY = getY();

        switch (dir) {
            case "right": spawnX += swingDistance; break;
            case "left": spawnX -= swingDistance; break;
            case "up": spawnY -= (swingDistance + 20); break;//extra range up
            case "down": spawnY += (swingDistance + 20); break;//extra range down
        }

        swing.setRotationBasedOnDirection(dir);
        getWorld().addObject(swing, spawnX, spawnY);
    }

    //spawns a fireball
    private void spawnFireBall() {
        soul -= 30;
        MyWorld world = (MyWorld) getWorld();
        world.spawnFireBall(directionFacing, this);
    }

    //exchanges soul for health
    private void focus() {
        if (soul > 0 && health < 100) {
            focus.playLoop();
            soul--;
            health++;
        } else {
            focus.stop();
        }
    }

    //Checks if touching an enemy & subtracts health
    //extra if its an aspidBullet
    private void checkHit() {
        if (isTouching(Enemy.class)) {
            health--;
            Enemy enemy = (Enemy) getOneIntersectingObject(Enemy.class);
            MyWorld world = (MyWorld) getWorld();
            world.setScore(health);

            if (enemy instanceof AspidBullet) {
                health -= 2;
            }
        }
    }

    //Knockback when hit (not fully used)
    private void applyKnockBack() {
        move(-1);
    }

    //adds x soul
    public void addSoul(int addSoul) {
        soul += addSoul;
    }

    //self explanatory
    public void checkPogo() {
        if (Greenfoot.isKeyDown("down")) {
            setVelocityY(-15);
        }
    }

    //returns current health (for screenDim)
    public int getHealth() {
        return health;
    }

    //Handles death animation
    public void die() {
        if (deathFrames < 79) {
            deathFrames++;
        }
        setImage(deathImages[deathFrames / 20]);
    }
}
