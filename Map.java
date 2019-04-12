import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Map
{
    public ArrayList<Rectangle> pipes;
    private int time;
    private int currentTime = 0;
    
    private int speed = 4;
    
    private Random random;
    private final int SPACE_PIPES = 100; //gap between pipes
    private final int WIDTH_PIPES = 40; //width of pipes
    
    public Map(int time)
    {
        pipes = new ArrayList<>();
        this.time = time;
        
        random = new Random();
    }
    
    public void update()
    {
        currentTime++;
        if(currentTime == time)
        {
            //spawns pipes
            currentTime = 0;
            
            int height1 = random.nextInt(FlappyGame.HEIGHT/2);
            int y2 = height1 + SPACE_PIPES;
            int height2 = FlappyGame.HEIGHT - y2;
            
            pipes.add(new Rectangle(FlappyGame.WIDTH, 0, WIDTH_PIPES, height1));
            pipes.add(new Rectangle(FlappyGame.WIDTH, y2, WIDTH_PIPES, height2));
        }
        
        for(int i = 0; i < pipes.size(); i++)
        {
            Rectangle rect = pipes.get(i);
            rect.x -= speed;
            
            if(rect.x + rect.width <= 0)
            {
                pipes.remove(i--);
                FlappyGame.score+= .5;
                continue;
            }
        }
    }
    
    public void render(Graphics g)
    {
        g.setColor(Color.green.darker());
        
        for(int i = 0; i < pipes.size(); i++)
        {
            Rectangle rect = pipes.get(i);
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }
}
