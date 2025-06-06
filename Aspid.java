import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
public class Aspid extends Enemy
{
    private GreenfootImage[] flyImage;
    private GreenfootImage shootImage;
    
    public Aspid(){
        flyImage[0] = ImageUtils.scale("enemies/aspid/fly1.png", 30, 60);
        flyImage[1] = ImageUtils.scale("enemies/aspid/fly2.png", 30, 60);
        shootImage = ImageUtils.scale("enemies/aspid/shoot.png", 30, 60);
    }
    public void act()
    {
        
    }
}
