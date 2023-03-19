import javax.swing.*;
import java.awt.*;

public class Game extends JPanel
{
    public Color backgroundColor = Color.BLACK;

    private Asteroids aster;

    public Game(Asteroids aster)
    {
        this.aster = aster;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        // 984 W : 961 H

        synchronized (Asteroids.class)
        {
            for (GameObject object : aster.gameObjects)
            {
                object.draw(g2d);
            }
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(1000, 1000);
    }
}