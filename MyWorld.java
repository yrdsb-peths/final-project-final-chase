import greenfoot.*;
import java.util.List;

public class MyWorld extends World {
    Swing objectA = new Swing();
    public int score = 0;
    Label scoreLabel;
    Label soulLabel;
    Player player; // Declare player as a field

    public MyWorld() {
        super(1000, 600, 1);

        scoreLabel = new Label(score, 80);
        addObject(scoreLabel, 200, 50);

        soulLabel = new Label("soul", 50);
        addObject(soulLabel, 200, 100);

        player = new Player();
        addObject(player, 815, 0);

        //testWorld(); 
        screen2();
    }

    private void testWorld() {
        GreenfootImage bg = new GreenfootImage("images/backGrounds/background.jpg");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);

        Ground g1 = new Ground(470, 100);
        Ground g2 = new Ground(260, 100);
        Ground g3 = new Ground(400, 50);

        addObject(g1, 175, 460);
        addObject(g2, 515, 515);
        addObject(g3, 800, 570);

        addObject(new Wall(20, 100), 415, 470);
        addObject(new Wall(20, 100), 650, 530);
        addObject(new Wall(20, 100), 30, 250);
        addObject(new Wall(20, 200), 420, 120);

        addObject(new Walker(), 700, 500);
        addObject(new Walker(), 500, 420);

        addObject(new Transition(40, 120, 1,900,400), 5, 360);
    }

    private void screen1() {
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen1.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);

        addObject(new Ground(325, 50), 130, 515);
        addObject(new Ground(700, 50), 790, 485);

        addObject(new Wall(20, 100), 300, 550);
        addObject(new Wall(20, 75), 440, 510);
        addObject(new Wall(20, 300), 735, 170);
        addObject(new Wall(20, 300), 900, 170);
        addObject(new Wall(20, 300), 560, 170);

        addObject(new Roof(150, 20), 650, 300);
        addObject(new Roof(100, 20), 950, 300);

        addObject(new Transition(40, 200, 0,100,500), 1000, 400);
        addObject(new Transition(40, 500, 2,950,300), 0, 300);
    }

    private void screen2() {
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen2.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
        
        addObject(new Ground(300, 50), 900, 340);
        addObject(new Ground(400, 50), 500, 515);
        addObject(new Ground(60, 60), 730, 460);
        addObject(new Ground(190, 50), 515, 310);
        addObject(new Ground(350, 50), 150, 400);
        
        addObject(new Wall(50, 150), 315, 450);
        addObject(new Wall(50, 150), 760, 395);
        addObject(new Wall(50, 50), 710, 470);
        addObject(new Wall(50, 250), 280, 140);
        
        addObject(new Roof(300, 50), 150, 250);



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

    public void spawnFireBall(String direction, Player player) {
        int dir = 0;
        int x = player.getX();
        int y = player.getY();

        if (direction.equals("left")) {
            dir = -1;
        } else if (direction.equals("right")) {
            dir = 1;
        }

        FireBall fireball = new FireBall(dir * 20);
        addObject(fireball, x, y);
    }

    public void setScore(int setScore) {
        scoreLabel.setValue(setScore + " health");
    }

    public void setSoul(int setSoul) {
        soulLabel.setValue(setSoul + " soul");
    }

    public void setScreen(int inputScreen, int playerX, int playerY) {
        clearScreen();
    
        if (inputScreen == 0) {
            testWorld();
        } else if (inputScreen == 1) {
            screen1();
        } else if (inputScreen == 2) {
            screen2();
        } else {
            testWorld(); // default
        }
    
        player.setLocation(playerX, playerY);
    }


    public void clearScreen() { //remove previous screen, except player and ui
        List<Actor> actors = getObjects(null);
        for (Actor actor : actors) {
            if (actor != scoreLabel && actor != soulLabel && actor != player) {
                removeObject(actor);
            }
        }
    }
}
