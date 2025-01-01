import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;



public class PacMan extends JPanel implements ActionListener,KeyListener{
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int initialx;
        int initialy;

        char direction = 'U';
        int speedx=0;
        int speedy = 0;

        Block(Image image,int x,int y,int width,int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.initialx = x;
            this.initialy=y;
        }

        void newDirection(char direction){
            char prevDir = this.direction;
            this.direction = direction;
            newSpeed();
            this.x += this.speedx;
            this.y += this.speedy;

            for(Block wall:walls){
                if (Collision(this,wall)){
                    this.x -= this.speedx;
                    this.y -=this.speedy;
                    this.direction=prevDir;
                    newSpeed();
        }
    }
}

        void newSpeed(){
            int movespeed = Size/8;
            if (this.direction == 'U'){
                this.speedx = 0;
                this.speedy = -movespeed; 
            }
            else if(this.direction == 'D'){
                this.speedx = 0;
                this.speedy = movespeed;
            }
            else if(this.direction == 'L'){
                this.speedx = -movespeed;
                this.speedy = 0;
            }
            else if(this.direction == 'R'){
                this.speedx = movespeed;
                this.speedy = 0;
            }
        }

    

    void reset(){
        this.x = this.initialx;
        this.y = this.initialy;
    }

    }


    private int row = 21;
    private int colom = 19;
    private int Size = 32;
    private int Width = colom*Size;
    private int Height = row*Size;

    private Image Brick;
    private Image Ghost1;
    private Image Ghost2;
    private Image Ghost3;
    private Image Ghost4;
    
    private Image pacUp;
    private Image pacDown;
    private Image pacRight;
    private Image pacLeft;


    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X      X     X",
        "XXXX XXXX XXXX XXXX",
        "000X X       X X000",
        "XXXX X XXrXX X XXXX",
        "0       bpo       0",
        "XXXX XXXX XXXX XXXX",
        "000X X       X X000",
        "XXXX XXXX XXXX XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X    P       X X",
        "X XX X XXXXX X XX X",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> energy;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameloop;
    char[] directions = {'U','R','D','L'};
    Random rand = new Random();
    int points = 0;
    boolean endgame = false;
    int health = 100;
    

    PacMan(){
        setPreferredSize(new Dimension(Width,Height));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
    
        Brick = new ImageIcon(getClass().getResource("./WALL.png")).getImage();
        Ghost1 = new ImageIcon(getClass().getResource("./blue ghost.jpg")).getImage();
        Ghost2 = new ImageIcon(getClass().getResource("./orange ghost.png")).getImage();
        Ghost3 = new ImageIcon(getClass().getResource("./orange ghost.png")).getImage();
        Ghost4 = new ImageIcon(getClass().getResource("./pink ghost.png")).getImage();

        pacUp = new ImageIcon(getClass().getResource("./pacman.jpg")).getImage();
        pacDown = new ImageIcon(getClass().getResource("./pacman.jpg")).getImage();
        pacLeft = new ImageIcon(getClass().getResource("./pacman.jpg")).getImage();
        pacRight = new ImageIcon(getClass().getResource("./pacman.jpg")).getImage();
        loadMap();

        for(Block ghost:ghosts){
            char newDir = directions[rand.nextInt(4)];
           ghost.newDirection(newDir); 
        }

        gameloop = new Timer(50,this);
        gameloop.start();

    System.out.println(walls.size());
    System.out.println(energy.size());
    System.out.println(ghosts.size());

    }


        public void loadMap() {
            walls = new HashSet<Block>();
            energy = new HashSet<Block>();
            ghosts = new HashSet<Block>();

            for (int r = 0;r < row;r++){
                for(int c=0;c<colom;c++){
                   String  row = tileMap[r];
                   char tileMapChar = row.charAt(c);
                    int x = c*Size;
                    int y = r*Size;

                    if (tileMapChar == 'X'){
                        Block wall = new Block(Brick, x, y,Size,Size);
                        walls.add(wall);
                    }
                    
                    else if (tileMapChar == 'b'){
                        Block ghost = new Block(Ghost1, x, y,Size,Size);
                        ghosts.add(ghost);
                    }
                       
                    else if (tileMapChar == 'o'){
                        Block ghost = new Block(Ghost2, x, y,Size,Size);
                        ghosts.add(ghost);
                    }
                    else if (tileMapChar == 'r'){
                        Block ghost = new Block(Ghost3, x, y,Size,Size);
                        ghosts.add(ghost);
                    }
                    else if (tileMapChar == 'p'){
                        Block ghost = new Block(Ghost4, x, y,Size,Size);
                        ghosts.add(ghost);
                    }
                    else if (tileMapChar == 'P'){
                        pacman = new Block(pacRight, x, y,Size,Size);
                    }
                    else if (tileMapChar == ' '){
                        Block food = new Block(null, x+14, y+14,4,4);
                        energy.add(food); 
                    }
                           

                }
            }
            
        }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(pacman.image,pacman.x,pacman.y,pacman.width,pacman.height,null);
        for (Block ghost : ghosts){
            g.drawImage(ghost.image,ghost.x,ghost.y,ghost.width,ghost.height,null);
        }
        for (Block wall : walls){
            g.drawImage(wall.image,wall.x,wall.y,wall.width,wall.height,null);
        }
        g.setColor(Color.WHITE);
        for (Block food  : energy){
            g.fillRect(food.x,food.y,food.width,food.height);
        }
        g.setFont(new Font("Arial",Font.PLAIN,18));
        if(endgame){
            g.drawString("GAME OVER : "+String.valueOf(points),Size/2,Size/2);
        }
        else{
            g.drawString("x"+String.valueOf(health) + "Score : "+String.valueOf(points),Size/2,Size/2);
        }
    }
    public void move(){
        pacman.x += pacman.speedx;
        pacman.y += pacman.speedy;

        for(Block wall : walls){

            if (Collision(pacman,wall)){
                if (Collision(pacman,wall)){
                    pacman.x -= pacman.speedx;
                    pacman.y -= pacman.speedy;
                    break;

                }
              
            }
            for(Block ghost : ghosts){
                if(Collision(ghost,pacman)){
                    health -= 1;
                    if(health == 0){
                        endgame = true;
                        return;
                    }
                    Reset();
                }
                if(ghost.y == Size*9 && ghost.direction != 'U' && ghost.direction != 'D'){
                    ghost.newDirection('U');
                }
                        ghost.x += ghost.speedx;
                        ghost.y += ghost.speedy;
                        for (Block walll:walls){
                            if(Collision(ghost,walll)|| ghost.x <= 0 || ghost.x+ghost.width >= Width){
                                ghost.x -= ghost.speedx;
                                ghost.y -= ghost.speedy;
                                char newDir =directions[rand.nextInt(4)];
                                ghost.newDirection(newDir);
                            }
                        }
            }
            Block snacks = null;
            for(Block food:energy){
                if(Collision(pacman,food)){
                    snacks = food;
                    points += 10;
                }
            }
            energy.remove(snacks);
            if(energy.isEmpty()){
                loadMap();
                Reset();
            }

    }

}

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if (endgame){
            gameloop.stop();
        }
    }

    
    

    public boolean Collision(Block var1 ,Block var2){
               return var1.x < var2.x + var2.width &&
                     var1.x + var1.width > var2.x &&
                     var1.y < var2.y +var2.height &&
                     var1.y+var1.height >var2.y;

    }

    public void Reset(){
        pacman.reset();
        pacman.speedx = 0;
        pacman.speedy = 0;

    for (Block ghost: ghosts){
        ghost.reset();
        char newDir = directions[rand.nextInt(4)];
        ghost.newDirection(newDir); 

    }

    }





    public void  keyReleased(KeyEvent e){
        if (endgame){
            loadMap();
            Reset();
            health = 3;
            points = 0;
            endgame = false;
            gameloop.start();
        }





        //System.out.println("KeyEvent:"+e.getKeyCode());
        if(e.getKeyCode() == KeyEvent.VK_UP){
            pacman.newDirection('U');
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            pacman.newDirection('D');
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            pacman.newDirection('L');
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            pacman.newDirection('R');
        }
        if (pacman.direction =='U'){
            pacman.image = pacUp;
        }
        else if (pacman.direction =='D'){
            pacman.image = pacDown;
        }
        else if (pacman.direction =='L'){
            pacman.image = pacLeft;
        }
        else if (pacman.direction =='R'){
            pacman.image = pacRight;
        }
    }
    
    


    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }


    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
    }
}
  


