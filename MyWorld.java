import greenfoot.*;

public class MyWorld extends World {
    Swing objectA = new Swing();
    public int score = 0;
    Label scoreLabel;
    Label soulLabel;

    public MyWorld() {
        super(1000, 600, 1);

        scoreLabel = new Label(score, 80);
        addObject(scoreLabel, 200, 50);
        soulLabel = new Label("soul",50);
        addObject(soulLabel,200,100);
        Player player = new Player();
        addObject(player, 300, 200);
        testWorld();
        //screen1();
    }

    private void testWorld() {
        GreenfootImage bg = new GreenfootImage("images/background.jpg");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);

        Ground g1 = new Ground(470, 100);
        Ground g2 = new Ground(260, 100);
        Ground g3 = new Ground(400, 50);

        addObject(g1, 175, 460);
        addObject(g2, 515, 515);
        addObject(g3, 800, 570);
        
        Wall w1 = new Wall (20, 100);
        addObject(w1, 415, 470);
        Wall w2 = new Wall(20,100);
        addObject(w2, 650, 530);
        Wall w3 = new Wall(20,100);
        addObject(w3,30,250);
        Wall w4 = new Wall(20,200);
        addObject(w4,420,120);
        
        Walker walker1 = new Walker();
        addObject(walker1, 700, 200);
        Walker walker2 = new Walker();
        addObject(walker2, 500, 200);
        
        Transition t1 = new Transition(40,120,1);
        addObject(t1,5,360);
    }
    private void screen1(){
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen1.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
        Ground g1 = new Ground(490,50);
        addObject(g1,130,520);
        Ground g2 = new Ground(580,50);
        addObject(g2,795,495);
        Ground g3 = new Ground(100,15);
        addObject(g3,380,800);
        Wall w1 = new Wall(20,100);
        addObject(w1,375,550);
        Wall w2 = new Wall(20,75);
        addObject(w2,505,510);
        Wall w3 = new Wall(20,300);
        addObject(w3,750,200);
        Wall w4 = new Wall(20,300);
        addObject(w4,880,200);
        Wall w5 = new Wall(20,300);
        addObject(w5,610,200);
        Roof r1 = new Roof(140,20);
        addObject(r1,675,325);
        Roof r2 = new Roof(150,20);
        addObject(r2,950,325);
    }
    private void screen2(){
        //next screen
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
    public void spawnFireBall(String direction, Player player){
        int dir = 0;
        int x=player.getX();
        int y=player.getY();
        if(direction.equals("left")){
             dir = -1;
        }else if(direction.equals("right")){
             dir = 1;
        }
        FireBall fireball = new FireBall(dir*30);
        addObject(fireball, x,y);
    }
    public void setScore(int setScore) {
        scoreLabel.setValue(setScore + " health");  // Update the score label when the score changes
    }
    public void setSoul(int setSoul) {
        soulLabel.setValue(setSoul + " soul");  // Update the score label when the score changes
    }
    public void setScreen(int inputScreen){
        clearScreen();
        if(inputScreen==1){
            screen1();
        }
    }
    public void clearScreen(){
        screen1();
    }
}
