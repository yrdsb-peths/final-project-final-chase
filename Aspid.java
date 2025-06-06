import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.Random;

public class Aspid extends Enemy {
    private GreenfootImage[] flyImage;
    private GreenfootImage shootImage;
    private GreenfootImage hitImage;

    private int flyAnimationFrame = 0;
    private int animationCounter = 0;
    private int runDelay = 10;

    private int health = 1;
    private int hitFrames = 0;
    private int shootFrames = 0;
    private Random rand;

    // Wander velocity
    private double dx = 0;
    private double dy = 0;
    private int wanderTimer = 0;

    // Facing direction tracking: true = facing left (default), false = facing right (mirrored)
    private boolean facingLeft = true;

    public Aspid() {
        flyImage = new GreenfootImage[2];
        flyImage[0] = ImageUtils.scale("enemies/aspid/fly1.png", 70, 60);
        flyImage[1] = ImageUtils.scale("enemies/aspid/fly2.png", 70, 60);

        shootImage = ImageUtils.scale("enemies/aspid/shoot.png", 80, 60);
        hitImage = ImageUtils.scale("enemies/aspid/hit.png", 70, 60);

        setImage(flyImage[0]);

        rand = new Random();
    }

    public void shoot() {
        MyWorld world = (MyWorld) getWorld();
        if (world != null) {
            world.spawnBullet(getX(), getY());
        }
    }

    public void act() {
        if (hitFrames > 0) {
            setImage(hitImage);
            updateFacing();
            hitFrames--;
            
            if (health <= 0 && hitFrames == 0) {
                getWorld().removeObject(this);
            }
            return;
        }

        if (shootFrames > 0) {
            shootFrames--;
            setImage(shootImage);
            updateFacing();
        } else {
            int randomNum = rand.nextInt(100);
            if (randomNum == 0) {
                shoot();
                shootFrames = 10;
                setImage(shootImage);
                updateFacing();
            } else {
                animateFly();
                updateFacing();
                wander();
            }
        }
    }

    private void animateFly() {
        animationCounter++;
        if (animationCounter % runDelay == 0) {
            flyAnimationFrame = (flyAnimationFrame + 1) % flyImage.length;
            setImage(flyImage[flyAnimationFrame]);
        }
    }

    private void wander() {
        if (wanderTimer <= 0) {
            double angle = rand.nextDouble() * 2 * Math.PI;
            dx = 2 * Math.cos(angle);
            dy = 2 * Math.sin(angle);
            wanderTimer = rand.nextInt(30) + 20;
        } else {
            setLocation((int)(getX() + dx), (int)(getY() + dy));
            wanderTimer--;
        }
    }

    private void updateFacing() {
        // Find the player
        List<Player> players = getWorld().getObjects(Player.class);
        if (players.isEmpty()) {
            return; // no player to face
        }
        Player player = players.get(0);

        boolean shouldFaceLeft = (player.getX() <= getX());

        if (shouldFaceLeft != facingLeft) {
            // Flip all images horizontally
            flipImages();
            facingLeft = shouldFaceLeft;
        }
    }

    private void flipImages() {
        // Flip fly images
        for (int i = 0; i < flyImage.length; i++) {
            flyImage[i].mirrorHorizontally();
        }
        // Flip shoot and hit images
        shootImage.mirrorHorizontally();
        hitImage.mirrorHorizontally();
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
