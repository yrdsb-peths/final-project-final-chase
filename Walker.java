import greenfoot.*;
import java.util.Random;

public class Walker extends Enemy {
    private int velocityY = 0;
    private final int gravity = 1;
    private final int maxFallSpeed = 10;
    private boolean onGround = false;

    private GreenfootImage[] runImages;
    private GreenfootImage idleImage;
    private GreenfootImage hitImage;

    private int direction = 1; // 1 = right, -1 = left
    private int walkSpeed = 1;
    private int walkTimer = 0;
    private int pauseTimer = 0;
    private boolean isPaused = false;
    private boolean chasingPlayer = false;
    private boolean x = true;
    private int runFrame = 0;
    private int runCounter = 0;
    private final int runDelay = 6;
    private int health = 14;
    private Random rand = new Random();
    private int hitFrames = 0;

    public Walker() {
        // Load run animation frames
        runImages = new GreenfootImage[4];
        for (int i = 0; i < 4; i++) {
            runImages[i] = ImageUtils.scale("enemies/walker/run" + (i + 1) + ".png", 50, 60);
        }

        idleImage = new GreenfootImage(runImages[0]);
        setImage(idleImage);
        hitImage = ImageUtils.scale("enemies/walker/walkerHit.png", 50, 60);
    }

    public void act() {
        if (hitFrames > 0) {
            setImage(hitImage);
            hitFrames--;
        } else {
            applyGravity();
            checkGround();
            checkWall();
            if (x) {
                wanderOrChase();
            }
            if (!chasingPlayer) {
                x = !x; // Slow down if not chasing
            }
            if (health <= 0) { // If health reaches 0 remove the walker
                getWorld().removeObject(this);
            }
        }
    }

    private void wanderOrChase() {
        Player player = getNearestPlayer();

        // If player is near, start chasing and never stop
        if (player != null && isPlayerNearby(player)) {
            chasingPlayer = true;
        }

        if (chasingPlayer && player != null) {
            if (player.getX() > getX()) {
                direction = 1;
            } else {
                direction = -1;
            }

            move(direction * walkSpeed);
            animateRun();
            return;
        }

        // Random wandering behavior (only before chasing starts)
        if (isPaused) {
            pauseTimer--;
            setImage(getMirroredImage(idleImage));
            if (pauseTimer <= 0) {
                isPaused = false;
                walkTimer = rand.nextInt(120) + 60;
            }
        } else {
            move(direction * walkSpeed);
            animateRun();

            walkTimer--;
            if (walkTimer <= 0 || isAtEdge() || isTouching(Ground.class)) {
                direction *= -1;
                maybePause();
            }
        }
    }

    private void animateRun() {
        runCounter++;
        if (runCounter >= runDelay) {
            runCounter = 0;
            runFrame = (runFrame + 1) % runImages.length;
        }

        GreenfootImage frame = runImages[runFrame];
        setImage(getMirroredImage(frame));
    }

    private GreenfootImage getMirroredImage(GreenfootImage original) {
        GreenfootImage img = new GreenfootImage(original);
        if (direction == 1) {
            img.mirrorHorizontally();
        }
        return img;
    }

    private void maybePause() {
        if (rand.nextBoolean()) {
            isPaused = true;
            pauseTimer = rand.nextInt(60) + 30;
        }
    }



    private Player getNearestPlayer() {
        return (Player) getWorld().getObjects(Player.class).stream()
            .findFirst().orElse(null);
    }

    private boolean isPlayerNearby(Player player) {
        int dx = Math.abs(player.getX() - getX());
        int dy = Math.abs(player.getY() - getY());
        return dx <= 100 && dy <= 100;
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        setImage(hitImage);
        hitFrames = 5;
        Player player = (Player) getWorld().getObjects(Player.class).get(0);
        player.checkPogo();
        if (player != null) {
            player.addSoul(15);
        }
    }
}
