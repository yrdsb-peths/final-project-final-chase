import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class AspidBullet extends Enemy
{
    private GreenfootImage bullet;
    private Player player; 
    public int health = 1;
    private int hitFrames = 0;
    private boolean directionSet = false;  // Add this as a field in the class
    public AspidBullet(){
        bullet = ImageUtils.scale("enemies/Aspid/projectile.png", 30, 30);
        setImage(bullet);
    }

    public void act()
    {
        if (player == null) {
            // Find the player in the world once
            player = getWorld().getObjects(Player.class).stream().findFirst().orElse(null);
        }
        if (player != null) {
            moveTowardsPlayer();
            checkWalls();
        } else {
            // No player found, maybe just move forward or do nothing
            move(-10);  // or no movement
        }
        if(health<1){
            getWorld().removeObject(this);
        }
    }

    private void checkWalls() {
        if (isAtEdge()) {
            World world = getWorld();
            if (world != null) {
                world.removeObject(this);
            }
        }
    }

    private void moveTowardsPlayer(){
        if (!directionSet) {
            int destinationX = player.getX();
            int destinationY = player.getY();
    
            int dx = destinationX - getX();
            int dy = destinationY - getY();
    
            double angle = Math.toDegrees(Math.atan2(dy, dx));
            setRotation((int)angle);
    
            directionSet = true;  // Lock the direction after setting it once
        }
        move(10);  // Always move forward in the fixed direction
    }
    
    private void checkCollision(){
        if(isTouching(WorldBlocks.class)){
            getWorld().removeObject(this);
        }
    }
    
    public void decreaseHealth(int amount) {
        health -= amount;
        hitFrames = 5;
        Player player = (Player) getWorld().getObjects(Player.class).get(0);
        player.checkPogo();
        if (player != null) {
            player.addSoul(1);
        }
    }
}
