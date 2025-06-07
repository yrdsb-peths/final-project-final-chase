import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class FalseKnightWave extends Enemy
{
    int direction;
    GreenfootImage[] wave;
    int frame;
    public FalseKnightWave(int direction){
        this.direction = direction;
        wave = new GreenfootImage[3];
        wave[0] = ImageUtils.scale("enemies/falseKnight/wave1.png", 100, 100);
        wave[1] = ImageUtils.scale("enemies/falseKnight/wave2.png", 100, 100);
        wave[2] = ImageUtils.scale("enemies/falseKnight/wave3.png", 100, 100);
        setImage(wave[0]); // set the image of this actor
    }

    public void act()
    {
        move(direction);
        animateWave();
        checkWalls();
    }
    public void animateWave(){
        frame++;
        setImage(wave[frame/10%3]);
    }
    private void checkWalls() {
        if (getX()<10||getX()>990) {
            World world = getWorld();
            if (world != null) {
                world.removeObject(this);
            }
        }
    }
}
