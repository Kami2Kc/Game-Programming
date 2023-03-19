import java.io.*;
import java.util.*;

public class Scoreboard
{
    private ArrayList<String> Scores;
    private ArrayList<String> top5Scores;
    int currentHighestScore = 0;

    public ArrayList<String> getScores(File file)
    {
        Scores = new ArrayList<>();

        try
        {
            File scoresFile  = new File(String.valueOf(file));
            BufferedReader reader = new BufferedReader(new FileReader(scoresFile));

            String currentLine;

            while ( (currentLine = reader.readLine()) != null)
            {
                if (!(currentLine.equals("")))
                {
                    Scores.add(currentLine);
                }
            }

        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        getTop5Scores();

        return top5Scores;
    }

    public void getTop5Scores()
    {
        top5Scores = new ArrayList<>();

        int min = 5;
        int length = Scores.size();
        int topScoreIndex = 0;

        if (Scores.size() < 5)
        {
            min = Scores.size();
        }

        for (int j = 0; j < min ; j++)
        {
            for (int i = 0; i < length; i ++)
            {
                String[] currentScoreLine = Scores.get(i).split(" ", 2);
                int currentScore = Integer.parseInt(currentScoreLine[1]);
                if (currentScore >= currentHighestScore)
                {
                    currentHighestScore = currentScore;
                    topScoreIndex = i;
                }
            }
            currentHighestScore = 0;
            top5Scores.add(Scores.get(topScoreIndex));
            Scores.remove(topScoreIndex);
            length = Scores.size();

        }
    }

    public void addScore(File file, String playerName, int score)
    {
        playerName = playerName.replace(" ", "");
        playerName = playerName.toUpperCase();
        String newScore = "\n" + playerName + " " + score;

        try
        {
            File scoresFile = new File(String.valueOf(file));
            FileWriter filewriter = new FileWriter(scoresFile, true);
            BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
            bufferedwriter.write(newScore);
            bufferedwriter.close();
            filewriter.close();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void createNewScoreFile(File file)
    {
        try
        {
            boolean fileCreated = file.createNewFile();
            System.out.println("File created : " + fileCreated);
        }

        catch (IOException e)
        {
            System.out.println("File created : " + false);
            e.printStackTrace();
        }
    }
}