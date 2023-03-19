import java.awt.*;
import java.util.Random;

public class Asteroid extends GameObject
{
    public static final double maxSpeed = 100;

    public int lifeTime = (int) (Math.random() * 5000) + 1000;

    int LargeAsteroid = 20;
    int MediumAsteroid = 12;
    int SmallAsteroid = 6;

    public Asteroid(Vector2D position, Vector2D velocity, int radius)
    {
        super(position, velocity, radius);
    }

    public static Asteroid makeRandomAsteroid()
    {
        double x = Math.random()* 1000;
        double y = Math.random()* 1000;

        Random random = new Random();
        double xv = (random.nextDouble() * (maxSpeed * 2)) - maxSpeed;
        double yv = (random.nextDouble() * (maxSpeed * 2)) - maxSpeed;

        Vector2D pos = new Vector2D(x, y);
        Vector2D vel = new Vector2D(xv, yv);

        return new Asteroid(pos, vel , 20);
    }

    public Asteroid split()
    {
        int currentSize = radius;
        int newSize = 0;

        if (currentSize == LargeAsteroid)
        {
            newSize = MediumAsteroid;
        }

        if (currentSize == MediumAsteroid)
        {
            newSize = SmallAsteroid;
        }

        Random random = new Random();
        double xv = (random.nextDouble() * (maxSpeed * 2)) - maxSpeed;
        double yv = (random.nextDouble() * (maxSpeed * 2)) - maxSpeed;

        Vector2D vel = new Vector2D(xv, yv);
        Vector2D pos = new Vector2D(position);

        return new Asteroid(pos, vel, newSize);
    }

    public void draw(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        g.drawOval((int) position.x - radius, (int) position.y - radius, 2 * radius, 2 * radius);

    }
}