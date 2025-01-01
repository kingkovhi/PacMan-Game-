import javax.swing.JFrame;


public class App{
public static void main(String[] args) throws Exception{
    int row = 21;
    int colom = 19;
    int Size = 32;
    int Width = colom*Size;
    int Height = row*Size;

    JFrame frame = new JFrame("PacMan");

    frame.setSize(Width,Height);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    PacMan Game = new PacMan();
    frame.add(Game);
    frame.pack();
    Game.requestFocus();
    frame.setVisible(true);
}

}
