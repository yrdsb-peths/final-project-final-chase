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
        addObject(player, 815, 100);
        
        

        screen11(); //starting screen
    }

    private void testWorld() {//the code for this screen is the old format 
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
        
        addObject(new Aspid(), 700, 200);

        addObject(new Transition(40, 120, 1,900,400), 5, 360);
    }

    private void screen1() {//starting room
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

        addObject(new Transition(40, 200, 0,100,400), 1000, 400);
        addObject(new Transition(40, 500, 2,900,300), 0, 300);
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
        addObject(new Wall(50, 250), 280, 120);
        
        addObject(new Roof(300, 50), 150, 250);
        addObject(new Roof(190, 50), 515, 340);
        
        addObject(new Walker(), 400, 470);
        addObject(new Aspid(), 400, 150);

        addObject(new Transition(50, 1000, 1,100,480), 1000, 300);
        addObject(new Transition(50, 1000, 3,950,50), 0, 300);

    }
    
    private void screen3(){//crossroads
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen3.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);

        addObject(new Ground(560, 50), 500, 340);
        addObject(new Ground(100, 50), 990, 200);
        addObject(new Ground(100, 50), 30, 200);
        addObject(new Ground(120, 50), 910 , 520);
        addObject(new Ground(120, 50), 80 , 520);
        
        addObject(new Wall(25, 130), 72 , 250);
        addObject(new Wall(25, 130), 943, 250);
        addObject(new Wall(25, 130), 228 , 404);
        addObject(new Wall(25, 130), 778 , 404);
        
        addObject(new Roof(100, 25),990 , 310);

        addObject(new Transition(10, 200, 2,100,350), 1000, 100);
        addObject(new Transition(1000, 10, 4,500,50), 500, 550);
        
        addObject(new Aspid(), 400, 150);
    }
    
    public void screen4(){
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen4.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
        addObject(new Transition(1000, 10, 5,500,100), 500, 550);
        addObject(new Transition(1000, 10, 3,850,350), 500, 0);
        addObject(new Ground(500, 25), 770, 475);
        addObject(new Ground(500, 25), 120, 475);
        addObject(new Ground(220, 25), 660, 175);
        addObject(new Ground(220, 25), 390, 200);
        addObject(new Ground(150, 25), 930, 330);
        addObject(new Ground(150, 25), 100, 265);
        addObject(new Roof(150, 25), 100, 275);
        addObject(new Roof(150, 25), 930, 340);
        addObject(new Roof(220, 25), 660, 185);
        addObject(new Roof(220, 25), 390, 210);
        addObject(new Roof(220, 25), 660, 25);
        addObject(new Wall(25, 100), 525, 521);
        addObject(new Wall(25, 100), 365, 521);
        
        addObject(new Walker(), 227, 420);
    }
    
    public void screen5(){
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen5.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
        addObject(new Transition(1000, 10, 6,450,0), 500, 550);
        addObject(new Transition(1000, 10, 4,500,500), 500, 0);
        addObject(new Ground(400, 25), 420, 135);
        addObject(new Ground(400, 25), 530, 200);
        addObject(new Ground(50, 25), 900, 325);
        addObject(new Ground(50, 25), 750, 420);
        addObject(new Ground(80, 25), 816, 553);
        
        addObject(new Wall(25, 500), 62, 299);
        addObject(new Wall(25, 230), 228, 248);
        addObject(new Wall(25, 100), 717, 248);
        addObject(new Wall(25, 100), 610, 181);
        
        addObject(new Roof(400, 25), 200, 30);
        addObject(new Roof(400, 25), 800, 30);
        
        addObject(new Aspid(), 827, 314);
    }
    
    public void screen6(){
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen6.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
        addObject(new Transition(25, 200, 7,100,350), 600, 475);
        addObject(new Ground(80, 25), 409, 390);
        addObject(new Ground(200, 25), 524, 558);
        addObject(new Wall(25, 200), 447, 490);
        addObject(new Wall(25, 800), 378, 257);
        addObject(new Wall(25, 400), 558, 218);
        addObject(new Wall(25, 200), 522, 117);
    }
    
    public void screen7(){
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen7.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
        addObject(new Transition(25, 200, 8,100,350), 950, 450);
        addObject(new Transition(25, 200, 6,556,500), 5, 350);
        addObject(new Ground(500, 25), 808, 518);
        addObject(new Ground(150, 25), 476, 454);
        addObject(new Ground(300, 25), 333, 556);
        addObject(new Ground(200, 25), 83, 388);
        addObject(new Ground(130, 10), 514, 80 );    
        addObject(new Ground(100, 10), 746, 212); 
        addObject(new Ground(100, 10), 642, 348); 
        addObject(new Ground(80, 10), 360, 280); 
        addObject(new Ground(80, 10), 190, 144); 
        addObject(new Wall(10, 300), 76, 91); 
        addObject(new Wall(25, 200), 405, 545); 
        addObject(new Wall(25, 200), 545, 545); 
        addObject(new Wall(10, 400), 873, 180); 
        addObject(new Wall(10, 200), 184, 481); 
        addObject(new Roof(130, 10), 514, 100 );    
        addObject(new Roof(100, 10), 746, 232); 
        addObject(new Roof(100, 10), 642, 368); 
        addObject(new Roof(80, 10), 360, 300); 
        addObject(new Roof(80, 10), 190, 164); 
        addObject(new Roof(80, 10), 38, 242); 
        addObject(new Roof(80, 10), 912, 380); 

        addObject(new Aspid(), 700, 100);
        addObject(new Aspid(), 774, 420);
        addObject(new Aspid(), 313, 172);
        addObject(new Walker(), 360, 490);
    }
    
    public void screen8(){
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen8.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
        addObject(new Ground(700, 10), 338, 470);
        addObject(new Ground(300, 10), 788, 280);
        addObject(new Ground(100, 10), 28, 362);
        addObject(new Ground(80, 10), 523, 388);
        
        addObject(new Wall(10, 100), 79, 414);
        addObject(new Wall(10, 100), 464, 136);
        addObject(new Wall(10, 300), 641, 431);
        addObject(new Wall(10, 100), 810, 41);
        addObject(new Wall(10, 100), 671, 50);
        addObject(new Wall(10, 300), 927, 127);
        addObject(new Wall(10, 100), 172, 286);
        addObject(new Wall(10, 200), 308, 236);
        
        addObject(new Roof(200, 10), 910, 90);
        addObject(new Roof(140, 10), 240, 336);
        addObject(new Roof(200, 10), 73, 238);
        addObject(new Roof(200, 10), 570, 102);
        addObject(new Roof(200, 10), 363, 182);
        addObject(new Roof(80, 10), 523, 418);
        
        addObject(new Transition(200, 25, 9,560,400), 731, 45);
        addObject(new Transition(25, 200, 7,850,500), 5, 350);
    }
    
    public void screen9(){
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen9.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
        
        addObject(new Ground(300, 10), 144, 126);
        addObject(new Ground(240, 10), 607, 126);
        addObject(new Ground(240, 10), 504, 228);
        addObject(new Ground(240, 10), 668, 196);
        addObject(new Ground(240, 10), 904, 361);
        addObject(new Ground(240, 10), 837, 493);
        addObject(new Ground(90, 10), 560, 558);
        
        addObject(new Wall(10, 200), 786, 461);
        addObject(new Wall(10, 100), 718, 544);
        addObject(new Wall(10, 100), 727, 180);
        addObject(new Wall(10, 100), 488, 172);
        addObject(new Wall(10, 500), 251, 382);
        addObject(new Wall(10, 130), 292, 190);
        addObject(new Wall(10, 100), 386, 280);
        addObject(new Wall(10, 80), 792, 238);
        addObject(new Wall(10, 300), 892, 238);
        addObject(new Wall(10, 80), 790, 51);
        addObject(new Wall(10, 80), 689, 296);
        
        addObject(new Roof(100, 10), 841, 90);
        addObject(new Roof(300, 10), 534, 330);
        addObject(new Roof(100, 10), 745, 277);
        
        addObject(new Transition(25, 200, 10,900,400), 22, 86);
        addObject(new Transition(1000, 10, 8,773,112), 500, 600);
    }
    
    public void screen10(){
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen10.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
        
        addObject(new Ground(1000, 10), 770, 396);
        addObject(new Ground(300, 10), 127, 325);
        
        addObject(new Wall(10, 100), 275, 375);
        addObject(new Wall(10, 100), 211, 144);
        addObject(new Wall(10, 300), 940, 108);
        
        addObject(new Roof(100, 10), 990, 261);
        addObject(new Roof(200, 10), 108, 192);
        addObject(new Roof(1000, 10), 600, 92);
        
        addObject(new Transition(25, 200, 11,900,500), 0, 250);
        addObject(new Transition(25, 200, 9,100,88), 1000, 362);
        addObject(new Walker(), 340, 362);
    }
    
    void screen11(){
        GreenfootImage bg = new GreenfootImage("images/backgrounds/screen11.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
        
        addObject(new Ground(1000, 10), 500, 521);
        addObject(new Wall(10, 500), 58, 262);
        addObject(new Wall(10, 500), 938, 172);
        addObject(new Roof(100, 10), 986, 421);
        addObject(new Transition(25, 200, 10,100,280), 1000, 500); 
        FalseKnight knight = new FalseKnight();
        addObject(knight, 340, 362);
        addObject(new FalseKnightHurtBox(knight), 340, 362);

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
    
        // Condensed version using a switch expression
        switch (inputScreen) {
            case 0 -> testWorld();
            case 1 -> screen1();
            case 2 -> screen2();
            case 3 -> screen3();
            case 4 -> screen4();
            case 5 -> screen5();
            case 6 -> screen6();
            case 7 -> screen7();
            case 8 -> screen8();
            case 9 -> screen9();
            case 10 -> screen10();
            case 11 -> screen11();
            default -> testWorld(); // Default case if the inputScreen is not recognized
        }
    
        player.setLocation(playerX, playerY);
    }

    public void spawnBullet(int x,int y){
        addObject(new AspidBullet(), x,y);
    }
    public Player getPlayer() {
        return player;
    }

    public void clearScreen() { //remove previous screen, except player and ui
        List<Actor> actors = getObjects(null);
        for (Actor actor : actors) {
            if (actor != scoreLabel && actor != soulLabel && actor != player) {
                removeObject(actor);
            }
        }
    }
    public void spawnWave(int direction,int x , int y){
        addObject(new FalseKnightWave(direction), x,y);
    }
}
