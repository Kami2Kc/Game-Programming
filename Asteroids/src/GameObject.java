import java.awt.*;

abstract class GameObject
{
    public int DELAY = 20;
    public double DT = DELAY / 1000.0;

    public Vector2D position;
    public Vector2D velocity;

    public int radius;

    public boolean alive = true;

    public GameObject(Vector2D position, Vector2D velocity, int radius)
    {
        this.position = position;
        this.velocity = velocity;

        this.radius = radius;
    }

    public void update()
    {
        position.addScaled(velocity, DT);
        position.wrap(984, 961);
    }

    public abstract void draw(Graphics2D g);

    public void hit()
    {
        alive = false;
    }

    public boolean checkCollision(GameObject other)
    {
        return this.position.dist(other.position) < this.radius + other.radius;
    }

    public boolean collisionHandler(GameObject other)
    {
        if (this.getClass() != other.getClass() && this.checkCollision(other))
        {
            this.hit();
            other.hit();
            return true;
        }

        return false;
    }
}