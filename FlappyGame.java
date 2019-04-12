import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
/**
 * FlappyBurd Final Project
 * 
 * Janelle Uganiza
 * Marcello Miccoli
 * Final Version
 */
public class FlappyGame extends Canvas implements Runnable, KeyListener
{
    public static final int WIDTH = 640, HEIGHT = 500;
    private boolean running = false;
    private Thread thread;

    public static double score = 0;

    public static Map map;
    public Bird bird;

    public FlappyGame()
    {
        Dimension d = new Dimension(FlappyGame.WIDTH, FlappyGame.HEIGHT);
        setPreferredSize(d);
        addKeyListener(this);
        map = new Map(90); //distance between a new set pipe of objects.
        bird = new Bird(20, FlappyGame.HEIGHT/2, map.pipes);
    }

    public synchronized void start()
    {
        if(running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop()
    {
        if(!running) return;
        running = false;
        try 
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("FlappyGame Bird");
        FlappyGame FlappyGame = new FlappyGame();
        frame.add(FlappyGame);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        FlappyGame.start();
    }

    @Override
    public void run()
    {
        int fps = 0;
        long lastTime = System.nanoTime();
        double ns = 1000000000 / 60;
        double delta = 0;
        double timer = System.currentTimeMillis();
        while (running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1)
            {
                update();
                render();
                fps++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 100)
            {
                //System.out.println("FPS: " + fps); //displays frames per second
                fps = 0;
                timer += 1000;
            }
        }
        stop();
    }

    private void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if( bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.orange); // <-- Dirt
        g.fillRect(0,0,getSize().width, getSize().height);

        g.setColor(Color.green); // <-- Ground
        g.fillRect(0,-50,getSize().width, getSize().height);

        g.setColor(Color.cyan); // <-- Sky
        g.fillRect(0,-60,getSize().width, getSize().height);

        map.render(g);
        bird.render(g);

        g.setColor(Color.black);
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 25)); //changes font and size for score counter
        g.drawString("Score: " + (int)score, 10, 20); //position of the score counter

        g.dispose();
        bs.show();
    }

    private void update()
    {
        map.update();
        bird.update();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) //change key to press
        {
            bird.isPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) //change key to press
        {
            bird.isPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }
}
