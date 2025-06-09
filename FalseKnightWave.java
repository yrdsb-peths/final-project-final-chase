import greenfoot.*;

public class FalseKnightWave extends Enemy {

    int direction;
    GreenfootImage[] wave;
    int frame;

    //constructor
    public FalseKnightWave(int direction) {
        this.direction = direction;
        wave = new GreenfootImage[3];
        wave[0] = ImageUtils.scale("enemies/falseKnight/wave1.png", 100, 100);
        wave[1] = ImageUtils.scale("enemies/falseKnight/wave2.png", 100, 100);
        wave[2] = ImageUtils.scale("enemies/falseKnight/wave3.png", 100, 100);

        if(direction < 0) {
            for(int i = 0; i < wave.length; i++) {
                wave[i].mirrorHorizontally();
            }
        }

        setImage(wave[0]);
    }

    //main loop
    public void act() {
        move(direction);
        animateWave();
        checkWalls();
    }

    //animation frames
    public void animateWave() {
        frame++;
        setImage(wave[frame / 10 % 3]);
    }

    //removes if offscreen
    private void checkWalls() {
        if(getX() < 10 || getX() > 990) {
            World world = getWorld();
            if(world != null) {
                world.removeObject(this);
            }
        }
    }
}
