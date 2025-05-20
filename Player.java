import greenfoot.*;

public class Player extends Actor {
    private int velocityY = 0;
    private final int gravity = 1;
    private final int jumpStrength = -10;
    private final int maxFallSpeed = 10;
    private boolean onGround = false;

    public String directionFacing = "left";

    private GreenfootImage[] idleImages;
    private GreenfootImage[] runImages;
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
    private final int swingDistance = 40;

    public Player() {
        idleImages = new GreenfootImage[1];
        idleImages[0] = new GreenfootImage("player/idle.png");

        runImages = new GreenfootImage[4];
        for (int i = 0; i < runImages.length; i++) {
            runImages[i] = new GreenfootImage("player/run/run" + (i + 1) + ".png");
        }

        attackImage = new GreenfootImage("images/player/attacks/swing3.png");
        dashImage = new GreenfootImage("images/player/dash.png");

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
    }
    
    private void checkKeys() {
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
    
        if (onGround && Greenfoot.isKeyDown("space")) {
            isJumping = true;
            jumpTimer = 0;
            onGround = false;
            velocityY = jumpStrength;
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
        dashTimer--;
        int dx = directionFacing.equals("right") ? dashSpeed : -dashSpeed;
        setLocation(getX() + dx, getY());

        GreenfootImage img = new GreenfootImage(dashImage);
        if (directionFacing.equals("left")) img.mirrorHorizontally();
        setImage(img);

        if (dashTimer <= 0) {
            isDashing = false;
            velocityY = 0;
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
            case "up":    spawnY -= swingDistance; break;
            case "down":  spawnY += swingDistance; break;
        }

        swing.setRotationBasedOnDirection(dir);
        world.addObject(swing, spawnX, spawnY);
    }
}
