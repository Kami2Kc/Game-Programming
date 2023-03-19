import java.awt.*;

public class Ship extends GameObject
{
    public double steerRate = 2* Math.PI;
    public double maxAcceleration = 250;
    public double drag = 0.5;

    private Color shipColor = Color.WHITE;
    private Color thrusterColor = Color.RED;

    public Vector2D direction;

    private Controller ctrl;

    public Bullet bullet = null;

    public double muzzleVelocity = 750;

    public Ship(Controller ctrl)
    {
        super(new Vector2D(), new Vector2D(0,0), 12);
        this.ctrl = ctrl;

        direction = new Vector2D(0, -1);
    }

    public void update()
    {
        direction.rotate(ctrl.action().turn * steerRate * DT);
        direction.normalise();

        velocity.add(direction.x * maxAcceleration * ctrl.action().thrust * DT, direction.y * maxAcceleration * ctrl.action().thrust * DT);
        velocity.set(Math.round(velocity.x * 100.0) / 100.0, Math.round(velocity.y * 100.0) / 100.0);

        if (velocity.x > maxAcceleration)
        {
            velocity.x = maxAcceleration;
        }
        if (velocity.x < -maxAcceleration)
        {
            velocity.x = -maxAcceleration;
        }

        if (velocity.y > maxAcceleration)
        {
            velocity.y = maxAcceleration;
        }
        if (velocity.y < -maxAcceleration)
        {
            velocity.y = -maxAcceleration;
        }

        if (velocity.x > 0)
        {
            velocity.subtract(drag, 0);
        }
        if (velocity.x < 0)
        {
            velocity.add(drag, 0);
        }

        if (velocity.y > 0)
        {
            velocity.subtract(0, drag);
        }
        if (velocity.y < 0)
        {
            velocity.add(0, drag);
        }

        position.add(velocity.x * DT, velocity.y * DT);
        position.wrap(984, 961);

        if (ctrl.action().shoot)
        {
            makeBullet();
            ctrl.action().shoot = false;
        }
    }

    private void makeBullet()
    {
        bullet = new Bullet(new Vector2D(position), new Vector2D(direction.x * muzzleVelocity, direction.y * muzzleVelocity), 4);
        SoundManager.fire();
    }

    public void hit()
    {

    }

    public void draw(Graphics2D g)
    {
        Vector2D perpendicular = new Vector2D(direction.x, direction.y);
        perpendicular.rotate(Math.PI / 2);
        Vector2D tail = new Vector2D(position.x - (direction.x * radius), position.y - (direction.y * radius));

        int[] shipX = {(int) Math.round(position.x + (direction.x * radius)), (int) Math.round(tail.x + (perpendicular.x * radius)), (int) Math.round(tail.x - (perpendicular.x * radius))};
        int[] shipY = {(int) Math.round(position.y + (direction.y * radius)), (int) Math.round(tail.y + (perpendicular.y * radius)), (int) Math.round(tail.y - (perpendicular.y * radius))};
        int[] thrusterX = {(int) Math.round(position.x), shipX[1], shipX[2]};
        int[] thrusterY = {(int) Math.round(position.y), shipY[1], shipY[2]};
        int[] cutoutX = {(int) Math.round(position.x - (direction.x * radius / 2)), shipX[1], shipX[2]};
        int[] cutoutY = {(int) Math.round(position.y - (direction.y * radius / 2)), shipY[1], shipY[2]};

        g.setColor(shipColor);
        g.fillPolygon(shipX, shipY, 3);
        g.setColor(Color.BLACK);

        if (ctrl.action().thrust == 1)
        {
            g.setColor(thrusterColor);
        }
        g.fillPolygon(thrusterX, thrusterY, 3);

        g.setColor(Color.BLACK);
        g.fillPolygon(cutoutX, cutoutY, 3);
    }
}