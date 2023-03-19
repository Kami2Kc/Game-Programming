import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyActions extends KeyAdapter implements Controller
{
    Action action;

    public KeyActions()
    {
        action = new Action();
    }

    public Action action()
    {
        return action;
    }

    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                action.thrust = 1;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                action.turn = -1;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                action.turn = +1;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = true;
                break;
        }
    }

    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                action.thrust = 0;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                action.turn = 0;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = false;
                break;
        }
    }
}