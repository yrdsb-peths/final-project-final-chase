import greenfoot.*;

public class Transition extends WorldBlocks {
    private int screen;
    private int spawnX;
    private int spawnY;

    public Transition(int width, int height, int inputScreen, int playerX, int playerY) {
        screen = inputScreen;
        spawnX = playerX;
        spawnY = playerY;

        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(255, 50, 255, 50));  // semi-transparent purple
        img.fill();
        setImage(img);
    }

    public int getScreen() {
        return screen;
    }

    public int getSpawnX() {
        return spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public void act() {
        if (isTouching(Player.class)) {
            MyWorld world = (MyWorld) getWorld();
            // Switch to the target screen and set player position
            world.setScreen(screen, spawnX, spawnY);
        }
    }
}
