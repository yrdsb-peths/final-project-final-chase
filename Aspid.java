import greenfoot.*; //(World, Actor, GreenfootImage, Greenfoot and MouseInfo)
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

    private double dx = 0;  
    private double dy = 0;  
    private int wanderTimer = 0;  

    private boolean facingLeft = true;  

    // constructor sets images and random
    public Aspid() { 
        flyImage = new GreenfootImage[2];  
        flyImage[0] = ImageUtils.scale("enemies/aspid/fly1.png", 70, 60);  
        flyImage[1] = ImageUtils.scale("enemies/aspid/fly2.png", 70, 60);  
        shootImage = ImageUtils.scale("enemies/aspid/shoot.png", 80, 60);  
        hitImage = ImageUtils.scale("enemies/aspid/hit.png", 70, 60);  
        setImage(flyImage[0]);  
        rand = new Random();  
    }

    // spawns bullet in world
    public void shoot() { 
        MyWorld world = (MyWorld) getWorld();  
        if(world != null) world.spawnBullet(getX(), getY());  
    }

    // main loop controls actions
    public void act() { 
        if(hitFrames > 0) {
            setImage(hitImage);  
            updateFacing();  
            hitFrames--;  
            if(health <= 0 && hitFrames == 0) getWorld().removeObject(this);  
            return;  
        }
        if(shootFrames > 0) {
            shootFrames--;  
            setImage(shootImage);  
            updateFacing();  
        } else {
            if(rand.nextInt(100) == 0) {
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

    // handles fly animation frames
    private void animateFly() { 
        animationCounter++;  
        if(animationCounter % runDelay == 0) {
            flyAnimationFrame = (flyAnimationFrame + 1) % flyImage.length;  
            setImage(flyImage[flyAnimationFrame]);  
        }
    }

    // random movement logic
    private void wander() { 
        if(wanderTimer <= 0) {
            //barely know how this works, asked chat for a formula and i coded it
            double angle = rand.nextDouble() * 2 * Math.PI;//Pick a random angle (0 to 2π radians)
            dx = 2 * Math.cos(angle);//calc x
            dy = 2 * Math.sin(angle);//calc y
            wanderTimer = rand.nextInt(30) + 20;  
        } else {
            setLocation((int)(getX() + dx), (int)(getY() + dy));//move by x and y
            wanderTimer--;  
        }
    }

    // flip images based on player position
    private void updateFacing() { 
        List<Player> players = getWorld().getObjects(Player.class);  
        if(players.isEmpty()) return;  
        Player player = players.get(0);  
        boolean shouldFaceLeft = (player.getX() <= getX());  
        if(shouldFaceLeft != facingLeft) {
            flipImages();  
            facingLeft = shouldFaceLeft;  
        }
    }

    // mirror all images horizontally
    private void flipImages() { 
        for(int i = 0; i < flyImage.length; i++) flyImage[i].mirrorHorizontally();  
        shootImage.mirrorHorizontally();  
        hitImage.mirrorHorizontally();  
    }

    // called when hit, lowers health and triggers effects
    public void decreaseHealth(int amount) { 
        health -= amount;  
        hitFrames = 5;  
        setImage(hitImage);  
        List<Player> players = getWorld().getObjects(Player.class);  
        if(!players.isEmpty()) {
            Player player = players.get(0);  
            player.checkPogo();  
            player.addSoul(15);  
        }
    }
}
