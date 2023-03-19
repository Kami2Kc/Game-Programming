import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.*;

public class ButtonActions implements ActionListener
{
    Scoreboard sb = new Scoreboard();

    private Asteroids aster;
    private int action;

    ButtonActions(Asteroids aster, int action)
    {
        this.aster = aster;
        this.action = action;
    }

    public void actionPerformed(ActionEvent e)
    {
        // Play game action
        if (this.action == 1)
        {
            aster.makeLevel();
            aster.cardLayout.show(aster.panel, "Game");
            aster.gameRunning = true;
            aster.game.setFocusable(true);
        }

        // Scoreboard button actions
        if (this.action == 2)
        {
            refreshScoreboard();
        }

        // Controls button action
        if (this.action == 3)
        {
            aster.cardLayout.show(aster.panel, "Controls");
        }

        // Exit button action
        if (this.action == 4)
        {
            System.exit(0);
        }

        // Scoreboard back button action
        if (this.action == 5)
        {
            aster.cardLayout.show(aster.panel, "Menu");
        }

        // Controls back button action
        if (this.action == 6)
        {
            aster.cardLayout.show(aster.panel, "Menu");
        }

        // Import button action
        if (this.action == 7)
        {
            int picked = aster.pickScoreFile.showOpenDialog(null);

            if (picked == JFileChooser.APPROVE_OPTION)
            {
                aster.scoreFile = aster.pickScoreFile.getSelectedFile();
                aster.cardLayout.show(aster.panel, "Menu");
            }
        }

        // Create new file button action
        if (this.action == 8)
        {
            int picked = aster.pickScoreFile.showSaveDialog(null);

            if (picked == JFileChooser.APPROVE_OPTION)
            {
                aster.scoreFile = aster.pickScoreFile.getSelectedFile();
                sb.createNewScoreFile(aster.scoreFile);
                aster.cardLayout.show(aster.panel, "Menu");
            }
        }

        // Enter name field action on game over screen
        if (this.action == 9)
        {
            sb.addScore(aster.scoreFile, aster.playerNameField.getText(), aster.playerScore);
            aster.playerNameField.setText("");
            aster.endGame();
            refreshScoreboard();
        }

        // Quit game button
        if (this.action == 10)
        {
            aster.gameRunning = false;
            aster.gameOverScore.setText("YOUR SCORE : " + aster.playerScore);
            aster.cardLayout.show(aster.panel, "GameOver");
        }
    }

    public void refreshScoreboard()
    {
        aster.cardLayout.show(aster.panel, "Scoreboard");

        ArrayList<String> top5Score = sb.getScores(aster.scoreFile);

        for (int i = 0; i < top5Score.size(); i++)
        {
            if (i == 0)
            {
                aster.score1.setText("1st : " + top5Score.get(0));
            }

            if (i == 1)
            {
                aster.score2.setText("2nd : " + top5Score.get(1));
            }

            if (i == 2)
            {
                aster.score3.setText("3rd : " + top5Score.get(2));
            }

            if (i == 3)
            {
                aster.score4.setText("4th : " + top5Score.get(3));
            }

            if (i == 4)
            {
                aster.score5.setText("5th : " + top5Score.get(4));
            }
        }
    }
}