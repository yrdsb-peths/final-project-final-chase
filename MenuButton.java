import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class MenuButton extends Actor {
    private int function;

    public MenuButton(int width, int height, int function) {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(50, 255, 255, 0)); // Cyan, semi-transparent
        img.fill();
        setImage(img);
        this.function = function;
    }

    public void act() {
        if (!Greenfoot.mouseClicked(this)) return;

        MyWorld world = (MyWorld) getWorld();
        if (world == null) return;

        // Handle button action first, then clear/add UI as needed
        switch (function) {
            case 0 -> {
                world.clearAll();
                world.startGame();
            }
            case 1 -> {
                world.clearAll();
                world.settings();
            }
            case 2 -> {
                world.clearAll();
                world.controls();
            }
            case 3  -> {
                MVolumeButton();
                world.clearAll();
                world.settings();
            }
            case 4 -> {
                world.controls();
                world.clearAll();
                world.startMenu();
            }
        }
    }



    public void MVolumeButton() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null) return;

        int newVol = (world.getMV() + 1) % 11;
        world.setMV(newVol);
    }
}
