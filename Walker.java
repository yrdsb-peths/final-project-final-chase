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
    private int walkSpeed = 2;
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

    //2 behaviours - wander and chase
    //wander by default
    //chase if player nearby or damage taken
    public Walker() {
        // Load run animation frames
        runImages = new GreenfootImage[4];
        for (int i = 0; i < 4; i++) {
            runImages[i] = ImageUtils.scale("enemies/walker/run" + (i + 1) + ".png", 50, 60);
        }

        idleImage = new GreenfootImage(runImages[0]);
        setImage(idleImage);
        hitImage = ImageUtils.scale("enemies/walker/walkerHit.png", 50, 60);

        walkTimer = rand.nextInt(120) + 60; // initialize walk timer
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

            if (health <= 0) {
                getWorld().removeObject(this);
            }
        }
    }

    private void wanderOrChase() {
        Player player = getNearestPlayer();

        // Chase if player is close
        if (player != null && isPlayerNearby(player)) {
            chasingPlayer = true;
        }

        if (chasingPlayer && player != null) {
            direction = player.getX() > getX() ? 1 : -1;
            move(direction * walkSpeed/2); // chase twice as fast
            animateRun();
            return;
        }

        // Wander when not chasing
        if (isPaused) {
            pauseTimer--;
            setImage(getMirroredImage(idleImage));
            if (pauseTimer <= 0) {
                isPaused = false;
                walkTimer = rand.nextInt(120) + 60; // reset walk timer after pause
            }
        } else {
            move(direction * walkSpeed /2); // walk at half speed when wandering
            animateRun();

            walkTimer--;
            if (walkTimer <= 0 || isAtEdge() || isTouching(Ground.class)) {
                // Flip direction randomly before pausing
                isPaused = true;
                pauseTimer = rand.nextInt(50) + 30; // pause duration
                if (rand.nextBoolean()) {
                    direction *= -1;
                }
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
        chasingPlayer = true;//start chase if damage taken
    }
}
