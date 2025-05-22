import greenfoot.*;

public class MyWorld extends World {
    Swing objectA = new Swing();
    public int score = 0;
    Label scoreLabel;
    Label soulLabel;

    public MyWorld() {
        super(1000, 600, 1);

        GreenfootImage bg = new GreenfootImage("images/background.jpg");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);

        setupWorld();
        scoreLabel = new Label(score, 80);
        addObject(scoreLabel, 50, 50);
        soulLabel = new Label("soul",50);
        addObject(soulLabel,50,100);
    }

    private void setupWorld() {
        Player player = new Player();
        addObject(player, 300, 200);

        Ground g1 = new Ground(500, 100);
        Ground g2 = new Ground(300, 100);
        Ground g3 = new Ground(400, 50);

        addObject(g1, 175, 460);
        addObject(g2, 515, 515);
        addObject(g3, 800, 570);

        Walker walker1 = new Walker();
        addObject(walker1, 700, 200);
        Walker walker2 = new Walker();
        addObject(walker2, 500, 200);

    }

    public void spawnSwing(String direction, Player player) {
        Swing swing = new Swing();
        int x = player.getX();
        int y = player.getY();
        int offset = 50 + swing.getImage().getWidth() / 2;

        if (direction.equals("right")) x += offset;
        if (direction.equals("left")) x -= offset;
        if (direction.equals("up")) y -= offset;
        if (direction.equals("down")) y += offset;

        swing.setRotationBasedOnDirection(direction);
        addObject(swing, x, y);
    }

    public void setScore(int setScore) {
        scoreLabel.setValue(setScore);  // Update the score label when the score changes
    }
    public void setSoul(int setSoul) {
        soulLabel.setValue(setSoul);  // Update the score label when the score changes
    }
}
