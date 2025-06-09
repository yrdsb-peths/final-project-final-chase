import greenfoot.*;

public class ScreenDim extends Actor {

    public GreenfootImage vignette;
    Player player;
    int playerHealth;
    int transparency;

    //constructor
    public ScreenDim(Player player) {
        this.player = player;
        vignette = ImageUtils.scale("Vignette.png", 1000, 600);
        vignette.setTransparency(0);
        setImage(vignette);
    }

    //main loop
    public void act() {
        if (player != null) {
            playerHealth = player.getHealth();
            transparency = 255 - (playerHealth * 2);

            if (transparency < 0) {
                transparency = 0;
            } else if (transparency > 255) {
                transparency = 255;
            }

            vignette.setTransparency(transparency);
        }
    }
}
