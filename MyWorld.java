import greenfoot.*;

public class MyWorld extends World {
    public MyWorld() {
        super(1000, 600, 1);

        GreenfootImage bg = new GreenfootImage("images/background.jpg");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);

        setupWorld();
    }

    private void setupWorld() {
        Player player = new Player();
        addObject(player, 300, 200);

        Ground g1 = new Ground(500, 50);
        Ground g2 = new Ground(300, 50);
        Ground g3 = new Ground(400, 50);

        addObject(g1, 180, 435);
        addObject(g2, 510, 490);
        addObject(g3, 800, 570);
        Walker walker = new Walker();
        addObject(walker,600,200);
    }

    public void spawnSwing(String direction, Player player) {
        Swing swing = new Swing();
        int x = player.getX();
        int y = player.getY();
        int offset = 40 + swing.getImage().getWidth() / 2;

        if (direction.equals("right")) x += offset;
        if (direction.equals("left")) x -= offset;
        if (direction.equals("up")) y -= offset;
        if (direction.equals("down")) y += offset;

        swing.setRotationBasedOnDirection(direction);
        addObject(swing, x, y);
    }
}
