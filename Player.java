import greenfoot.*;
import java.util.Random;

public class Player extends Actor {
    private int velocityY = 0;
    private final int gravity = 1;
    private final int jumpStrength = -11;
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
    public int soul = 0;

    public Player() {
        idleImages = new GreenfootImage[1];
        idleImages[0] = ImageUtils.scale("player/idle.png", 30, 60);
        
        runImages = new GreenfootImage[4];

        runImages[0] = ImageUtils.scale("player/run/run1.png", 37, 60);
        runImages[1] = ImageUtils.scale("player/run/run2.png", 37, 60);
        runImages[2] = ImageUtils.scale("player/run/run3.png", 37, 60);
        runImages[3] = ImageUtils.scale("player/run/run4.png", 37, 60);
        
        attackImage = new GreenfootImage("images/player/attacks/swing3.png");
        
        dashImages = new GreenfootImage[3];
        dashImages[0] = new GreenfootImage(ImageUtils.scale("player/dash/dash1.png", 150, 50));
        dashImages[1] = new GreenfootImage(ImageUtils.scale("player/dash/dash2.png", 110, 50)); //stretchy
        dashImages[2] = new GreenfootImage(ImageUtils.scale("player/dash/dash3.png", 80, 50));
        dashImage = dashImages[0];

        GreenfootImage start = new GreenfootImage(idleImages[0]);
        start.mirrorHorizontally();
        setImage(start);
    }

    public void act() {
        if (isDashing) {
            dash();
            return;
        }

        checkKeys();
        applyGravity();
        checkGround();

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
    }
    
    private void checkKeys() {
        if(Greenfoot.isKeyDown("a")){
            focus();
        }
        if (Greenfoot.isKeyDown("c") && !isDashing) {
            if (dashCooldown <= 0) {
                isDashing = true;
                dashTimer = dashDuration;
                swingSpawned = false;
                dashCooldown = 20;  // cooldown set here before return
                return;
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
    
        if (onGround && (Greenfoot.isKeyDown("space"))) {
            isJumping = true;
            jumpTimer = 0;
            onGround = false;
            velocityY = jumpStrength;
            setImage(ImageUtils.scale("player/jump.png", 40, 60));
        }
    
        if (isJumping && Greenfoot.isKeyDown("space")) {
            if (jumpTimer < maxJumpTime) {
                velocityY = jumpStrength;
                jumpTimer++;
            } else {
                isJumping = false;
            }
        }
    
        if (!Greenfoot.isKeyDown("space")) {
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
            velocityY = 0;  // Reset the vertical velocity after the dash
        }
    }


    private void applyGravity() {
        velocityY += gravity;
        if (velocityY > maxFallSpeed) {
            velocityY = maxFallSpeed;
        }
        setLocation(getX(), getY() + velocityY);
    }

    private void checkGround() {
        World world = getWorld();
        Actor ground = getOneIntersectingObject(Ground.class);

        if (ground != null) {
            while (isTouching(Ground.class)) {
                setLocation(getX(), getY() - 1);
            }
            velocityY = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        if (getY() + getImage().getHeight() / 2 >= world.getHeight()) {
            setLocation(getX(), world.getHeight() - getImage().getHeight() / 2);
            velocityY = 0;
            onGround = true;
        }
    }

    private void spawnSwing() {
        String dir = directionFacing;
        if (Greenfoot.isKeyDown("up")) dir = "up";
        else if (Greenfoot.isKeyDown("down") && !onGround) dir = "down";

        MyWorld world = (MyWorld) getWorld();
        Swing swing = new Swing();

        int spawnX = getX();
        int spawnY = getY();

        switch (dir) {
            case "right": spawnX += swingDistance; break; //swingDistance=40
            case "left":  spawnX -= swingDistance; break;
            case "up":    spawnY -= (swingDistance+10); break;
            case "down":  spawnY += (swingDistance+20); break;
        }

        swing.setRotationBasedOnDirection(dir);
        world.addObject(swing, spawnX, spawnY);
    }
    private void focus(){
        if(soul>0&&health<100){//maxhealth 100, minsoul 0
            soul--;
            health++;
        }
    }
    private void checkHit() {
        if (isTouching(Enemy.class)) {
            // Decrease health when the player touches an enemy
            health--;
    
            // Get the world and update the score (which also updates the score label)
            MyWorld world = (MyWorld) getWorld();
            world.setScore(health);  // Pass the updated health to the world
        }
    }
    private void applyKnockBack(){
        move(-1);
    }
    public void addSoul(int addSoul){
        soul+=addSoul;
    }
    public void checkPogo(){
        if(Greenfoot.isKeyDown("down")){
            velocityY = -15;
        }
    }
}
