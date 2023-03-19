import java.awt.*;

public class Bullet extends GameObject
{
    public int lifeTime = 50;

    public Bullet(Vector2D position, Vector2D velocity, int radius)
    {
        super(position, velocity, radius);
    }

    public void draw(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        g.fillOval((int) position.x - radius, (int) position.y - radius, 2 * radius, 2 * radius);
    }
}
