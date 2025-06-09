import greenfoot.*;  //(World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class AspidBullet extends Enemy
{
    //Bullet image and player reference
    private GreenfootImage bullet;
    private Player player;

    //health and 
    public int health = 1;

    //check if bulletDirection set
    private boolean directionSet = false;

    //gets angle from location to playerlocation once, moves in direction
    public AspidBullet() {
        //Load and resize the bullet image
        bullet = ImageUtils.scale("enemies/Aspid/projectile.png", 30, 30);
        setImage(bullet);
    }

    //Runs every frame
    public void act() {
        if (player == null) {
            //Find the player object in the world
            player = getWorld().getObjects(Player.class).stream().findFirst().orElse(null);
        }

        if (player != null) {
            moveTowardsPlayer();
            checkWalls();
        }

        if (health < 1) {
            getWorld().removeObject(this);
        }
    }

    //if touching borders, removeObject
    private void checkWalls() {
        if (isAtEdge()) {
            World world = getWorld();
            if (world != null) {
                world.removeObject(this);
            }
        }
    }

    //set bullet direction once and move
    private void moveTowardsPlayer() {
        if (!directionSet) {
            int destinationX = player.getX();
            int destinationY = player.getY();

            int dx = destinationX - getX();
            int dy = destinationY - getY();

            //Calculate angle between bullet and player
            double angle = Math.toDegrees(Math.atan2(dy, dx));
            setRotation((int)angle);

            directionSet = true;  //Lock direction so it doesn't change again
        }

        move(10);  //Move bullet forward
    }


    //called when hit in swing or fireball
    public void decreaseHealth(int amount) {
        health -= amount;

        Player player = (Player) getWorld().getObjects(Player.class).get(0);

        if (player != null) {
            player.checkPogo();   //Player bounces when bullet is hit
            player.addSoul(1);    //Add soul to player
        }
    }
}
