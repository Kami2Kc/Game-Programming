import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Made by Kamil Cwigun kc18960 1805841

public class Asteroids extends JFrame
{
    // Main panels
    JPanel panel;
    JPanel menuPanel;
    JPanel scoreboardPanel;
    JPanel gameOverPanel;
    JPanel enterScoreFilePanel;
    JPanel controlsPanel;

    // Main menu buttons
    JButton playButton;
    JButton scoreboardButton;
    JButton controlsButton;
    JButton exitButton;

    // Scoreboard buttons
    JButton scoreBackButton;

    // Controls buttons
    JButton controlsBackButton;

    // File selector
    JButton importButton;
    JButton createNewFileButton;

    JButton quitGameButton;

    JFileChooser pickScoreFile;

    //Game over
    JTextField playerNameField;

    // Game
    JLabel playerLivesLabel;
    JLabel playerScoreLabel;

    JLabel score1;
    JLabel score2;
    JLabel score3;
    JLabel score4;
    JLabel score5;

    JLabel menuTitle;
    JLabel scoreboardTitle;
    JLabel controlsTitle;
    JLabel controlsLabel1;
    JLabel controlsLabel2;
    JLabel controlsLabel3;
    JLabel gameOverTitle;

    JLabel gameOverScore;
    JLabel enterNameLabel;

    JLabel selectFileTitle;
    JLabel selectFileLabel;

    JLabel spacingLabel0;
    JLabel spacingLabel1;
    JLabel spacingLabel2;
    JLabel spacingLabel3;
    JLabel spacingLabel4;
    JLabel spacingLabel5;
    JLabel spacingLabel6;
    JLabel spacingLabel7;
    JLabel spacingLabel8;
    JLabel spacingLabel9;
    JLabel spacingLabel10;
    JLabel spacingLabel11;
    JLabel spacingLabel12;
    JLabel spacingLabel13;

    Font h1Font = new Font("Arial", Font.BOLD, 110);
    Font h3Font = new Font("Arial", Font.BOLD, 60);
    Font h4Font = new Font("Arial", Font.BOLD, 50);
    Font h5Font = new Font("Arial", Font.BOLD, 40);

    CardLayout cardLayout = new CardLayout();

    public File scoreFile;

    public boolean gameRunning = false;

    public int playerLives = 3;
    public int playerScore = 0;
    public int newLifeAt = 10000;
    public int initialNumberOfAsteroids = 4;
    public int currentNumberOfAsteroids = initialNumberOfAsteroids;
    public int incrementAsteroidsBy = 4;

    int LargeAsteroid = 20;
    int MediumAsteroid = 12;
    int SmallAsteroid = 6;

    public Game game;

    public ArrayList<GameObject> gameObjects;

    public Ship ship;
    public KeyActions ctrl;

    public static void main(String[] args) throws InterruptedException
    {
        Asteroids aster = new Asteroids();

        aster.setSize(1000, 1000);
        aster.setMaximumSize(new Dimension(1000, 1000));
        aster.setTitle("Asteroids 1805841");
        aster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aster.setResizable(false);
        aster.setVisible(true);
        aster.setFocusable(true);
        aster.setFocusTraversalKeysEnabled(false);
        aster.addKeyListener(aster.ctrl);

        // For some reason game doesn't run after pressing play unless I add another line to this while loop >:(

        while (true)
        {
            aster.setTitle("Asteroids 1805841");
            aster.runGame();
        }
    }

    public Asteroids()
    {
        // Create panels for the game
        panel = new JPanel();
        menuPanel = new JPanel();
        scoreboardPanel = new JPanel();
        gameOverPanel = new JPanel();
        enterScoreFilePanel = new JPanel();
        controlsPanel = new JPanel();
        game = new Game(this);

        panel.setLayout(cardLayout);

        initializeCardLayout();
        initializeMenu();
        initializeScoreboard();
        initializeControls();
        initializeGame();
        initializeEnterFile();
        initializeGameOver();

        gameObjects = new ArrayList<>();

        ctrl = new KeyActions();
        ship = new Ship(ctrl);

        gameObjects.add(ship);
    }

    public void runGame() throws InterruptedException
    {
        if (gameRunning)
        {
            update();
            game.repaint();
            Thread.sleep(20);
        }
    }

    public void makeLevel()
    {
        resetShip();

        for (int i = 0; i < currentNumberOfAsteroids; i++)
        {
            gameObjects.add(Asteroid.makeRandomAsteroid());
        }

        currentNumberOfAsteroids += incrementAsteroidsBy;
        spawnProtection();
    }

    public void endGame()
    {
        gameObjects.clear();
        gameObjects.add(ship);

        currentNumberOfAsteroids = initialNumberOfAsteroids;
        playerScore = 0;
        playerLives = 3;

        resetShip();
    }

    public void resetShip()
    {
        ship.position.set(492, 490.5);
        ship.velocity.set(0,0);
        ship.direction.set(0, -1);
    }

    public void spawnProtection()
    {
        ArrayList<GameObject> correctedAsteroids = new ArrayList<>(gameObjects);

        boolean safeSpawn = false;

        while (!safeSpawn)
        {
            safeSpawn = true;

            for (GameObject object : correctedAsteroids)
            {
                if (object instanceof Asteroid)
                {
                    if (object.position.dist(ship.position) < 200)
                    {
                        double x = Math.random()* 1000;
                        double y = Math.random()* 1000;

                        object.position.set(x, y);
                        safeSpawn = false;
                    }
                }
            }
        }

        gameObjects.clear();
        gameObjects.addAll(correctedAsteroids);
    }

    public void update()
    {
        playerScoreLabel.setText("SCORE : " + playerScore + "      ");
        playerLivesLabel.setText("LIVES : " + playerLives + "      ");

        ArrayList<GameObject> alive = new ArrayList<>();

        ArrayList<GameObject> splitAsteroids = new ArrayList<>();

        checkForCollision();

        for (GameObject object : gameObjects)
        {
            object.update();

            if (object instanceof Bullet)
            {
                if (((Bullet) object).lifeTime <= 0)
                {
                    object.alive = false;
                }
                else
                {
                    ((Bullet) object).lifeTime --;
                }
            }

            if (object instanceof Asteroid)
            {
                if (object.radius != SmallAsteroid)
                {
                    if (((Asteroid) object).lifeTime <= 0)
                    {
                        object.alive = false;
                        for (int i = 0; i != 4; i++)
                        {
                            splitAsteroids.add(((Asteroid) object).split());
                        }
                    }
                    else
                    {
                        ((Asteroid) object).lifeTime --;
                    }
                }
            }

            if (object.alive)
            {
                alive.add(object);
            }
        }

        if (ship.bullet != null)
        {
            alive.add(ship.bullet);
            ship.bullet = null;
        }

        synchronized (Asteroids.class)
        {
            gameObjects.clear();
            gameObjects.addAll(alive);
        }

        int numberOfAsteroids = 0;

        for (GameObject object : gameObjects)
        {
            if (object instanceof Asteroid)
            {
                numberOfAsteroids ++;
            }
        }

        if (numberOfAsteroids == 0)
        {
            makeLevel();
        }

        if (ctrl.action.thrust == 1)
        {
            SoundManager.startThrust();
        }
        else
            {
                SoundManager.stopThrust();
            }

        gameObjects.addAll(splitAsteroids);
        checkLives();
    }

    public void checkForCollision()
    {
        ArrayList<GameObject> splitAsteroids = new ArrayList<>();

        for (GameObject object : gameObjects)
        {
            for (GameObject otherObject : gameObjects)
            {
                if (object.getClass() != otherObject.getClass())
                {
                    if (!((object instanceof Ship && otherObject instanceof Bullet) || (object instanceof Bullet && otherObject instanceof Ship)))
                    {
                        if ((object instanceof Bullet && otherObject instanceof Asteroid) || (object instanceof Asteroid && otherObject instanceof Bullet))
                        {
                            if (object.alive & otherObject.alive)
                            {
                                if (object.collisionHandler(otherObject))
                                {
                                    if (object.radius == LargeAsteroid || otherObject.radius == LargeAsteroid)
                                    {
                                        playerScore += 20;
                                        SoundManager.asteroidsLarge();
                                    }

                                    else if (object.radius == MediumAsteroid || otherObject.radius == MediumAsteroid)
                                    {
                                        playerScore += 50;
                                        SoundManager.asteroidsMedium();
                                    }

                                    else if (object.radius == SmallAsteroid || otherObject.radius == SmallAsteroid)
                                    {
                                        playerScore += 100;
                                        SoundManager.asteroidsSmall();
                                    }

                                    if (object.radius != SmallAsteroid && otherObject.radius != SmallAsteroid)
                                    {
                                        if (object instanceof Asteroid)
                                        {
                                            for (int i = 0; i != 2; i++)
                                            {
                                                splitAsteroids.add(((Asteroid) object).split());
                                            }
                                        }

                                        else
                                        {
                                            for (int i = 0; i != 2; i++)
                                            {
                                                splitAsteroids.add(((Asteroid) otherObject).split());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        else if ((object instanceof Ship && otherObject instanceof Asteroid) || (object instanceof Asteroid && otherObject instanceof Ship))
                        {
                            if (object.alive & otherObject.alive)
                            {
                                if (object.collisionHandler(otherObject))
                                {
                                    playerLives --;
                                }
                            }
                        }
                    }
                }
            }
        }

        gameObjects.addAll(splitAsteroids);
    }

    public void checkLives()
    {
        if(playerScore >= newLifeAt)
        {
            playerLives ++;
            newLifeAt += 10000;
        }

        if (playerLives <= 0)
        {
            gameRunning = false;
            gameOverScore.setText("YOUR SCORE : " + playerScore);
            cardLayout.show(panel, "GameOver");
        }
    }

    private void initializeCardLayout()
    {
        panel.add(menuPanel, "Menu");
        panel.add(scoreboardPanel, "Scoreboard");
        panel.add(controlsPanel, "Controls");
        panel.add(game, "Game");
        panel.add(enterScoreFilePanel, "EnterFile");
        panel.add(gameOverPanel, "GameOver");

        add(panel);

        cardLayout.show(panel, "EnterFile");
    }

    private void initializeMenu()
    {
        menuPanel.setBackground(Color.BLACK);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        spacingLabel0 = new JLabel(" ");
        spacingLabel0.setFont(h4Font);

        menuTitle = new JLabel("ASTEROIDS");
        menuTitle.setFont(h1Font);
        menuTitle.setForeground(Color.WHITE);

        spacingLabel1 = new JLabel(" ");
        spacingLabel1.setFont(h4Font);

        playButton = new JButton("PLAY");
        playButton.setFont(h3Font);
        playButton.setFocusable(false);
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(Color.BLACK);
        playButton.setBorderPainted(false);
        playButton.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                playButton.setForeground(Color.RED);
                playButton.setText("=PLAY=");
            }

            public void mouseExited(MouseEvent e)
            {
                playButton.setForeground(Color.WHITE);
                playButton.setText("PLAY");
            }
        });

        scoreboardButton = new JButton("SCOREBOARD");
        scoreboardButton.setFont(h3Font);
        scoreboardButton.setFocusable(false);
        scoreboardButton.setForeground(Color.WHITE);
        scoreboardButton.setBackground(Color.BLACK);
        scoreboardButton.setBorderPainted(false);
        scoreboardButton.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                scoreboardButton.setForeground(Color.RED);
                scoreboardButton.setText("=SCOREBOARD=");
            }

            public void mouseExited(MouseEvent e)
            {
                scoreboardButton.setForeground(Color.WHITE);
                scoreboardButton.setText("SCOREBOARD");
            }
        });

        controlsButton = new JButton("CONTROLS");
        controlsButton.setFont(h3Font);
        controlsButton.setFocusable(false);
        controlsButton.setForeground(Color.WHITE);
        controlsButton.setBackground(Color.BLACK);
        controlsButton.setBorderPainted(false);
        controlsButton.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                controlsButton.setForeground(Color.RED);
                controlsButton.setText("=CONTROLS=");
            }

            public void mouseExited(MouseEvent e)
            {
                controlsButton.setForeground(Color.WHITE);
                controlsButton.setText("CONTROLS");
            }
        });

        exitButton = new JButton("EXIT");
        exitButton.setFont(h3Font);
        exitButton.setFocusable(false);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.BLACK);
        exitButton.setBorderPainted(false);
        exitButton.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                exitButton.setForeground(Color.RED);
                exitButton.setText("=EXIT=");
            }

            public void mouseExited(MouseEvent e)
            {
                exitButton.setForeground(Color.WHITE);
                exitButton.setText("EXIT");
            }
        });

        menuPanel.add(spacingLabel0);
        menuPanel.add(menuTitle);
        menuPanel.add(spacingLabel1);
        menuPanel.add(playButton);
        menuPanel.add(scoreboardButton);
        menuPanel.add(controlsButton);
        menuPanel.add(exitButton);

        spacingLabel0.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        spacingLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        playButton.addActionListener(new ButtonActions(this, 1));
        scoreboardButton.addActionListener(new ButtonActions(this, 2));
        controlsButton.addActionListener(new ButtonActions(this, 3));
        exitButton.addActionListener(new ButtonActions(this, 4));
    }

    private void initializeScoreboard()
    {
        scoreboardPanel.setBackground(Color.BLACK);
        scoreboardPanel.setLayout(new BoxLayout(scoreboardPanel, BoxLayout.Y_AXIS));

        spacingLabel2 = new JLabel(" ");
        spacingLabel2.setFont(h4Font);

        scoreboardTitle = new JLabel("SCOREBOARD");
        scoreboardTitle.setFont(h1Font);
        scoreboardTitle.setForeground(Color.WHITE);

        spacingLabel3 = new JLabel(" ");
        spacingLabel3.setFont(h4Font);

        score1 = new JLabel("1st : ??? 000");
        score1.setFont(h3Font);
        score1.setForeground(Color.RED);

        score2 = new JLabel("2nd : ??? 000");
        score2.setFont(h3Font);
        score2.setForeground(Color.ORANGE);

        score3 = new JLabel("3rd : ??? 000");
        score3.setFont(h3Font);
        score3.setForeground(Color.YELLOW);

        score4 = new JLabel("4th : ??? 000");
        score4.setFont(h3Font);
        score4.setForeground(Color.WHITE);

        score5 = new JLabel("5th : ??? 000");
        score5.setFont(h3Font);
        score5.setForeground(Color.WHITE);

        spacingLabel4 = new JLabel(" ");
        spacingLabel4.setFont(h4Font);

        scoreBackButton = new JButton("BACK");
        scoreBackButton.setFont(h3Font);
        scoreBackButton.setFocusable(false);
        scoreBackButton.setForeground(Color.WHITE);
        scoreBackButton.setBackground(Color.BLACK);
        scoreBackButton.setBorderPainted(false);
        scoreBackButton.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                scoreBackButton.setForeground(Color.RED);
                scoreBackButton.setText("=BACK=");
            }

            public void mouseExited(MouseEvent e)
            {
                scoreBackButton.setForeground(Color.WHITE);
                scoreBackButton.setText("BACK");
            }
        });

        scoreboardPanel.add(spacingLabel2);
        scoreboardPanel.add(scoreboardTitle);
        scoreboardPanel.add(spacingLabel3);
        scoreboardPanel.add(score1);
        scoreboardPanel.add(score2);
        scoreboardPanel.add(score3);
        scoreboardPanel.add(score4);
        scoreboardPanel.add(score5);
        scoreboardPanel.add(spacingLabel4);
        scoreboardPanel.add(scoreBackButton);

        spacingLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreboardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        spacingLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        score1.setAlignmentX(Component.CENTER_ALIGNMENT);
        score2.setAlignmentX(Component.CENTER_ALIGNMENT);
        score3.setAlignmentX(Component.CENTER_ALIGNMENT);
        score4.setAlignmentX(Component.CENTER_ALIGNMENT);
        score5.setAlignmentX(Component.CENTER_ALIGNMENT);
        spacingLabel4.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreBackButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreBackButton.addActionListener(new ButtonActions(this, 5));
    }

    private void initializeControls()
    {
        controlsPanel.setBackground(Color.BLACK);
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));

        spacingLabel5 = new JLabel(" ");
        spacingLabel5.setFont(h4Font);

        controlsTitle = new JLabel("CONTROLS");
        controlsTitle.setFont(h1Font);
        controlsTitle.setForeground(Color.WHITE);

        spacingLabel6 = new JLabel(" ");
        spacingLabel6.setFont(h4Font);

        controlsLabel1 = new JLabel("Use keys A,D or LEFT, RIGHT to turn the ship");
        controlsLabel1.setFont(h5Font);
        controlsLabel1.setForeground(Color.WHITE);

        controlsLabel2 = new JLabel("Use keys W or UP to accelerate the ship");
        controlsLabel2.setFont(h5Font);
        controlsLabel2.setForeground(Color.WHITE);

        controlsLabel3 = new JLabel("Use key SPACEBAR to shoot");
        controlsLabel3.setFont(h5Font);
        controlsLabel3.setForeground(Color.WHITE);

        spacingLabel7 = new JLabel(" ");
        spacingLabel7.setFont(h4Font);

        controlsBackButton = new JButton("BACK");
        controlsBackButton.setFont(h3Font);
        controlsBackButton.setFocusable(false);
        controlsBackButton.setForeground(Color.WHITE);
        controlsBackButton.setBackground(Color.BLACK);
        controlsBackButton.setBorderPainted(false);
        controlsBackButton.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                controlsBackButton.setForeground(Color.RED);
                controlsBackButton.setText("=BACK=");
            }

            public void mouseExited(MouseEvent e)
            {
                controlsBackButton.setForeground(Color.WHITE);
                controlsBackButton.setText("BACK");
            }
        });

        controlsPanel.add(spacingLabel5);
        controlsPanel.add(controlsTitle);
        controlsPanel.add(spacingLabel6);
        controlsPanel.add(controlsLabel1);
        controlsPanel.add(controlsLabel2);
        controlsPanel.add(controlsLabel3);
        controlsPanel.add(spacingLabel7);
        controlsPanel.add(controlsBackButton);

        spacingLabel5.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        spacingLabel6.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlsLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlsLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlsLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        spacingLabel7.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlsBackButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        controlsBackButton.addActionListener(new ButtonActions(this, 6));
    }

    private void initializeGame()
    {
        game.setBackground(Color.BLACK);

        playerLivesLabel = new JLabel("LIVES : " + playerLives + "      ");
        playerLivesLabel.setFont(h5Font);
        playerLivesLabel.setForeground(Color.WHITE);

        playerScoreLabel = new JLabel("SCORE : " + playerScore + "      ");
        playerScoreLabel.setFont(h5Font);
        playerScoreLabel.setForeground(Color.WHITE);

        quitGameButton = new JButton("QUIT");
        quitGameButton.setFont(h5Font);
        quitGameButton.setFocusable(false);
        quitGameButton.setForeground(Color.WHITE);
        quitGameButton.setBackground(Color.BLACK);
        quitGameButton.setBorderPainted(false);
        quitGameButton.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                quitGameButton.setForeground(Color.RED);
            }

            public void mouseExited(MouseEvent e)
            {
                quitGameButton.setForeground(Color.WHITE);
            }
        });

        game.add(playerLivesLabel);
        game.add(playerScoreLabel);
        game.add(quitGameButton);

        quitGameButton.addActionListener(new ButtonActions(this, 10));
    }

    private void initializeEnterFile()
    {
        enterScoreFilePanel.setBackground(Color.BLACK);
        enterScoreFilePanel.setLayout(new BoxLayout(enterScoreFilePanel, BoxLayout.Y_AXIS));

        spacingLabel11 = new JLabel(" ");
        spacingLabel11.setFont(h4Font);

        selectFileTitle = new JLabel("FILE SELECTION");
        selectFileTitle.setFont(h1Font);
        selectFileTitle.setForeground(Color.WHITE);

        spacingLabel12 = new JLabel(" ");
        spacingLabel12.setFont(h4Font);

        selectFileLabel = new JLabel("IMPORT EXISTING SCORE FILE OR CREATE NEW");
        selectFileLabel.setFont(h5Font);
        selectFileLabel.setForeground(Color.WHITE);

        spacingLabel13 = new JLabel(" ");
        spacingLabel13.setFont(h4Font);

        importButton = new JButton("IMPORT EXISTING FILE");
        importButton.setFont(h3Font);
        importButton.setFocusable(false);
        importButton.setForeground(Color.WHITE);
        importButton.setBackground(Color.BLACK);
        importButton.setBorderPainted(false);
        importButton.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                importButton.setForeground(Color.RED);
                importButton.setText("=IMPORT EXISTING FILE=");
            }

            public void mouseExited(MouseEvent e)
            {
                importButton.setForeground(Color.WHITE);
                importButton.setText("IMPORT EXISTING FILE");
            }
        });

        createNewFileButton = new JButton("CREATE NEW FILE");
        createNewFileButton.setFont(h3Font);
        createNewFileButton.setFocusable(false);
        createNewFileButton.setForeground(Color.WHITE);
        createNewFileButton.setBackground(Color.BLACK);
        createNewFileButton.setBorderPainted(false);
        createNewFileButton.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e)
            {
                createNewFileButton.setForeground(Color.RED);
                createNewFileButton.setText("=CREATE NEW FILE=");
            }

            public void mouseExited(MouseEvent e)
            {
                createNewFileButton.setForeground(Color.WHITE);
                createNewFileButton.setText("CREATE NEW FILE");
            }
        });

        pickScoreFile = new JFileChooser();
        pickScoreFile.setMultiSelectionEnabled(false);
        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Text Files", "txt");
        pickScoreFile.setFileFilter(txtFilter);

        enterScoreFilePanel.add(spacingLabel11);
        enterScoreFilePanel.add(selectFileTitle);
        enterScoreFilePanel.add(spacingLabel12);
        enterScoreFilePanel.add(selectFileLabel);
        enterScoreFilePanel.add(spacingLabel13);
        enterScoreFilePanel.add(importButton);
        enterScoreFilePanel.add(createNewFileButton);

        spacingLabel11.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectFileTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        spacingLabel12.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectFileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        spacingLabel13.setAlignmentX(Component.CENTER_ALIGNMENT);
        importButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createNewFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        importButton.addActionListener(new ButtonActions(this, 7));
        createNewFileButton.addActionListener(new ButtonActions(this, 8));
    }

    private void initializeGameOver()
    {
        gameOverPanel.setBackground(Color.BLACK);
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));

        spacingLabel8 = new JLabel(" ");
        spacingLabel8.setFont(h4Font);

        gameOverTitle = new JLabel("GAME OVER");
        gameOverTitle.setFont(h1Font);
        gameOverTitle.setForeground(Color.WHITE);

        spacingLabel9 = new JLabel(" ");
        spacingLabel9.setFont(h4Font);

        gameOverScore = new JLabel("YOUR SCORE : " + playerScore);
        gameOverScore.setFont(h3Font);
        gameOverScore.setForeground(Color.WHITE);

        spacingLabel10 = new JLabel(" ");
        spacingLabel10.setFont(h4Font);

        enterNameLabel = new JLabel("ENTER YOUR NAME BELOW");
        enterNameLabel.setFont(h5Font);
        enterNameLabel.setForeground(Color.WHITE);

        playerNameField = new JTextField(10);
        playerNameField.setFont(h5Font);

        gameOverPanel.add(spacingLabel8);
        gameOverPanel.add(gameOverTitle);
        gameOverPanel.add(spacingLabel9);
        gameOverPanel.add(gameOverScore);
        gameOverPanel.add(spacingLabel10);
        gameOverPanel.add(enterNameLabel);
        gameOverPanel.add(playerNameField);

        spacingLabel8.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        spacingLabel9.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        spacingLabel10.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerNameField.setMaximumSize(new Dimension(400, 40));

        playerNameField.addActionListener(new ButtonActions(this, 9));
    }
}