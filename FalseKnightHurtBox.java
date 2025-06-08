import greenfoot.*;

public class FalseKnightHurtBox extends Enemy {
    private FalseKnight target;

    public FalseKnightHurtBox(FalseKnight target) {
        this.target = target;
        GreenfootImage img = new GreenfootImage(150, 150);
        img.setColor(new Color(255, 255, 50, 0));
        img.fill();
        setImage(img);
    }

    public void act() {
        if (target != null) {
            setLocation(target.getX(), target.getY());
        }
    }
    public void takeDamage(int amount) {
        if (target != null) {
            target.decreaseHealth(amount);
        }
    }

}
