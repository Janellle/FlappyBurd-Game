import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Bird extends Rectangle
{
    private float speed = 4;
    public boolean isPressed = false;
    private BufferedImage sheet;
    private BufferedImage[] texture;
    private ArrayList<Rectangle> pipes;
    private boolean isFalling = false;
    private int frames = 0;
    private float gravity = 0.3f;

    public Bird(int x, int y, ArrayList<Rectangle> pipes)
    {
        setBounds(x, y, 32, 32);
        this.pipes = pipes;
        texture = new BufferedImage[1];

        try
        {
            sheet = ImageIO.read(getClass().getResource("/burdog.png"));
            texture[0] = sheet.getSubimage(0, 0, 48, 34); //last two ints resize img
        }
        catch(IOException e){}
    }

    public void update()
    {
        isFalling = false;
        if(isPressed)
        {
            speed = 4;
            y -= (speed);  
            frames = 0;
        }
        else
        {
            isFalling = true;
            y += speed;
            frames++;
            if (frames > 40) frames = 40;
        }

        if(isFalling)
        {
            speed += gravity;
            if(speed > 8) speed = 8;
        }

        for(int i = 0; i < pipes.size(); i++)
        {
            if (this.intersects(pipes.get(i)))
            {
                //restart the game if bird intersects pipe
                FlappyGame.map = new Map(80);
                pipes = FlappyGame.map.pipes;
                y = FlappyGame.HEIGHT / 2;
                FlappyGame.score = 0; //resets score when game restarts
                speed = 4;
                break;
            }

            if(y >= FlappyGame.HEIGHT || y <= 0) 
            {
                //restart the game if bird crosses top and lower boundaries
                FlappyGame.map = new Map(80);
                pipes = FlappyGame.map.pipes;
                y = FlappyGame.HEIGHT / 2;
                FlappyGame.score = 0; //resets score when game restarts
                speed = 4;
            }
        }
    }

    public void render(Graphics g)
    {
        g.drawImage(texture[0], x, y, width, height, null);
    }
}
