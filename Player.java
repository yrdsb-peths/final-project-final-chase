import greenfoot.*;
import java.util.Random;

public class Player extends PhysicsObject {
    private final int gravity = 1;
    private final int jumpStrength = -15;
    private final int maxFallSpeed = 10;
    private boolean onGround = false;
    public String directionFacing = "left";

    private GreenfootImage[] idleImages;
    private GreenfootImage[] runImages;
    private GreenfootImage[] dashImages;
    private GreenfootImage attackImage;  // single attack frame
    private GreenfootImage dashImage;

    private int runFrame = 0;
    private int runCounter = 0;
    private final int runDelay = 5;

    private boolean isJumping = false;
    private int jumpTimer = 0;
    private final int maxJumpTime = 8;

    private boolean isDashing = false;
    private int dashTimer = 0;
    private final int dashDuration = 15;
    private final int dashSpeed = 12;
    public int dashCooldown = 0;

    private boolean isAttacking = false;
    private int attackDisplayCounter = 0;
    private final int attackDisplayTime = 10;

    private boolean swingSpawned = false;
    private int swingCooldown = 0;
    private final int swingCooldownTime = 20;
    private final int swingDistance = 50;

    public int health = 100;
    public int soul = 30;
    private int focusTimer = 0;

    private GreenfootSound dashSound;
    private GreenfootSound footstepsSound;
    private GreenfootSound focusStart;
    private GreenfootSound focus;

    private GreenfootImage[] deathImages;
    int deathFrames = 0;
    boolean isDead = false;
    
    private GreenfootSound death;
    int FXVolume;
    
    private boolean volumeInitialized = false;

    public Player() {
        death = new GreenfootSound("hero_death.mp3");
        deathImages = new GreenfootImage[6];
        deathImages[0] = new GreenfootImage(ImageUtils.scale("player/death/death1.png", 37, 60));
        deathImages[1] = new GreenfootImage(ImageUtils.scale("player/death/death2.png", 37, 60));
        deathImages[2] = new GreenfootImage(ImageUtils.scale("player/death/death3.png", 37, 60));
        deathImages[3] = new GreenfootImage(ImageUtils.scale("player/death/death4.png", 37, 60));
        deathImages[4] = new GreenfootImage(ImageUtils.scale("player/death/death5.png", 37, 60));
        deathImages[5] = new GreenfootImage(ImageUtils.scale("player/death/death6.png", 37, 60));
        
        focus = new GreenfootSound("focus_health_charging.mp3");
        focusStart = new GreenfootSound("focus_ready.mp3");
        dashSound = new GreenfootSound("dash.mp3");
        footstepsSound = new GreenfootSound("footsteps.mp3");  // Initialize footsteps sound

        idleImages = new GreenfootImage[1];
        idleImages[0] = ImageUtils.scale("player/idle.png", 30, 60);

        runImages = new GreenfootImage[4];
        runImages[0] = ImageUtils.scale("player/run/run1.png", 37, 60);
        runImages[1] = ImageUtils.scale("player/run/run2.png", 37, 60);
        runImages[2] = ImageUtils.scale("player/run/run3.png", 37, 60);
        runImages[3] = ImageUtils.scale("player/run/run4.png", 37, 60);

        attackImage = new GreenfootImage(ImageUtils.scale("player/attacks/swing3.png", 55, 55));

        dashImages = new GreenfootImage[3];
        dashImages[0] = new GreenfootImage(ImageUtils.scale("player/dash/dash1.png", 150, 50));
        dashImages[1] = new GreenfootImage(ImageUtils.scale("player/dash/dash2.png", 110, 50)); // stretchy
        dashImages[2] = new GreenfootImage(ImageUtils.scale("player/dash/dash3.png", 80, 50));
        dashImage = dashImages[0];

        GreenfootImage start = new GreenfootImage(idleImages[0]);
        start.mirrorHorizontally();
        setImage(start);
    }

    public void act() {
        if(!isDead){
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
    
            if (swingCooldown > 0) {
                swingCooldown--;
            }
            if (dashCooldown > 0) {
                dashCooldown--;
            }
            checkHit();
            MyWorld world = (MyWorld) getWorld();
            world.setScore(health);
            world.setSoul(soul);
            if(health<1){
                death.play();
                isDead = true;
            }
        } else{
            MyWorld world = (MyWorld) getWorld();
            die();
            if(Greenfoot.isKeyDown("space")){
                world.clearScreen();
                world.screen1();
                health = 100;
                soul = 30;
                isDead = false;
            }
            if(Greenfoot.isKeyDown("shift")){
                world.clearAll();
                world.startMenu();
            }
        }
    }
    
    private void checkKeys() {
        if (Greenfoot.isKeyDown("a")) {
            focusStart.play();
            focusTimer++;
            if (focusTimer > 20) {
                focus();
            }else{
                focus.stop(); //the sound 
            }
        } else if (!Greenfoot.isKeyDown("a")) {
            if (focusTimer < 20 && focusTimer > 0) {
                if (soul > 29) {
                    spawnFireBall();
                }
            }
            focusTimer = 0;
        }
        if (Greenfoot.isKeyDown("c") && !isDashing) {
            if (dashCooldown <= 0) {
                isDashing = true;
                dashTimer = dashDuration;
                swingSpawned = false;
                dashCooldown = 20;
                dashSound.play();
                return;
            }
        }

        if ((Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("right"))&& velocityY < 2) {//stupidest possible way to check this
            if (!footstepsSound.isPlaying()) {
                footstepsSound.playLoop();
            }
        } else {
            if (footstepsSound.isPlaying()) {
                footstepsSound.stop();
            }
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

        if (isOnGround() && (Greenfoot.isKeyDown("space"))) {
            isJumping = true;
            jumpTimer = 0;
            onGround = false;
            setVelocityY(20);  // set jump velocity
            setImage(ImageUtils.scale("player/jump.png", 40, 60));
        }

        if (isJumping && Greenfoot.isKeyDown("space")) {
            if (jumpTimer < maxJumpTime) {
                setVelocityY(-14);  // continue jumping
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

    private void dash() {
        dashTimer--;  // Decrease the dash timer

        // Determine the direction of the dash based on the facing direction
        int dx = directionFacing.equals("right") ? dashSpeed : -dashSpeed;
        setLocation(getX() + dx, getY());

        // Calculate which dash frame to use based on how long the dash has been happening
        int dashFrame = (dashDuration - dashTimer) * dashImages.length / dashDuration;
        dashFrame = Math.min(dashFrame, dashImages.length - 1); // Ensure we don't exceed the array bounds

        // Update the image for the dash
        GreenfootImage img = new GreenfootImage(dashImages[dashFrame]);
        if (directionFacing.equals("left")) {
            img.mirrorHorizontally();  // Mirror the image if facing left
        }
        setImage(img);

        // End the dash once the dashTimer reaches 0
        if (dashTimer <= 0) {
            isDashing = false;
            setVelocityY(0);  // Reset the vertical velocity after the dash
        }
    }

    private void spawnSwing() {
        String dir = directionFacing;
        if (Greenfoot.isKeyDown("up")) dir = "up";
        else if (Greenfoot.isKeyDown("down") && !onGround) dir = "down";

        Swing swing = new Swing();

        int spawnX = getX();
        int spawnY = getY();

        switch (dir) {
            case "right": spawnX += swingDistance; break; // swingDistance = 40
            case "left":  spawnX -= swingDistance; break;
            case "up":    spawnY -= (swingDistance + 10); break;
            case "down":  spawnY += (swingDistance + 20); break;
        }

        swing.setRotationBasedOnDirection(dir);
        getWorld().addObject(swing, spawnX, spawnY);
    }

    private void spawnFireBall() {
        soul -= 30;
        MyWorld world = (MyWorld) getWorld();
        world.spawnFireBall(directionFacing, this);
    }

    private void focus() {
        if (soul > 0 && health < 100) {  // max health 100, min soul 0
            focus.playLoop();
            soul--;
            health++;
        }else{
            focus.stop();
        }
    }

    private void checkHit() {
        if (isTouching(Enemy.class)) {
            // Decrease health when the player touches an enemy
            health--;
            Enemy enemy = (Enemy) getOneIntersectingObject(Enemy.class);
            MyWorld world = (MyWorld) getWorld();
            world.setScore(health);

            if (enemy instanceof AspidBullet) {  // aspid bullets do extra damage
                health -= 2;
            }
        }
    }

    private void applyKnockBack() {
        move(-1);
    }

    public void addSoul(int addSoul) {
        soul += addSoul;
    }

    public void checkPogo() {
        if (Greenfoot.isKeyDown("down")) {
            setVelocityY(-15);
        }
    }

    public int getHealth() {
        return health;
    }
    public void die(){
        if(deathFrames<79){
            deathFrames++;
        }
        setImage(deathImages[deathFrames/20]);
    }
}
