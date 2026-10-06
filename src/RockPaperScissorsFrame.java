import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * GUI Rock Paper Scissors game against the computer.
 * The computer picks a Strategy each round based on probability.
 *
 * @author Kirby Fortney
 */
public class RockPaperScissorsFrame extends JFrame
{
    // player history used by the inner class strategies
    private int playerRockCount = 0;
    private int playerPaperCount = 0;
    private int playerScissorsCount = 0;
    private String lastPlayerMove = "";

    // running stats
    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;

    // strategies
    private Strategy cheat = new Cheat();
    private Strategy randomStrategy = new RandomStrategy();
    private Strategy leastUsed = new LeastUsed();
    private Strategy mostUsed = new MostUsed();
    private Strategy lastUsed = new LastUsed();
    private Random rnd = new Random();

    // GUI parts
    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;
    private JButton quitButton;
    private JTextField playerWinsField;
    private JTextField computerWinsField;
    private JTextField tiesField;
    private JTextArea resultsArea;

    /**
     * Builds the game window.
     */
    public RockPaperScissorsFrame()
    {
        setTitle("Rock Paper Scissors Game");
        setLayout(new BorderLayout(5, 5));

        add(createButtonPanel(), BorderLayout.NORTH);
        // wrapper keeps the stats panel at the top instead of stretching
        JPanel statsWrapper = new JPanel(new BorderLayout());
        statsWrapper.add(createStatsPanel(), BorderLayout.NORTH);
        add(statsWrapper, BorderLayout.WEST);
        add(createResultsPanel(), BorderLayout.CENTER);

        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Creates the panel with the Rock, Paper, Scissors and Quit buttons.
     *
     * @return the button panel
     */
    private JPanel createButtonPanel()
    {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Make your move"));

        rockButton = new JButton("Rock", loadIcon("/rock.png"));
        paperButton = new JButton("Paper", loadIcon("/paper.png"));
        scissorsButton = new JButton("Scissors", loadIcon("/scissors.png"));
        quitButton = new JButton("Quit", loadIcon("/quit.png"));

        // one listener for the three game buttons
        MoveListener moveListener = new MoveListener();
        rockButton.addActionListener(moveListener);
        paperButton.addActionListener(moveListener);
        scissorsButton.addActionListener(moveListener);

        quitButton.addActionListener(ae -> System.exit(0));

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(quitButton);
        return buttonPanel;
    }

    /**
     * Creates the stats panel with the win and tie counts.
     *
     * @return the stats panel
     */
    private JPanel createStatsPanel()
    {
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats"));

        playerWinsField = new JTextField("0", 5);
        computerWinsField = new JTextField("0", 5);
        tiesField = new JTextField("0", 5);

        playerWinsField.setEditable(false);
        computerWinsField.setEditable(false);
        tiesField.setEditable(false);

        statsPanel.add(new JLabel("Player Wins:"));
        statsPanel.add(playerWinsField);
        statsPanel.add(new JLabel("Computer Wins:"));
        statsPanel.add(computerWinsField);
        statsPanel.add(new JLabel("Ties:"));
        statsPanel.add(tiesField);
        return statsPanel;
    }

    /**
     * Creates the panel with the scrolling results text area.
     *
     * @return the results panel
     */
    private JPanel createResultsPanel()
    {
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));

        resultsArea = new JTextArea(10, 40);
        resultsArea.setEditable(false);

        resultsPanel.add(new JScrollPane(resultsArea), BorderLayout.CENTER);
        return resultsPanel;
    }

    /**
     * Loads an image from the src folder.
     *
     * @param path the image file name, e.g. "/rock.png"
     * @return the ImageIcon
     */
    private ImageIcon loadIcon(String path)
    {
        return new ImageIcon(getClass().getResource(path));
    }

    /**
     * Single listener for the Rock, Paper and Scissors buttons.
     */
    private class MoveListener implements ActionListener
    {
        /**
         * Plays one round using the button that was clicked as the player move.
         *
         * @param ae the button click event
         */
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            String playerMove = "";
            if (ae.getSource() == rockButton)
            {
                playerMove = "R";
            }
            else if (ae.getSource() == paperButton)
            {
                playerMove = "P";
            }
            else if (ae.getSource() == scissorsButton)
            {
                playerMove = "S";
            }
            playRound(playerMove);
        }
    }

    /**
     * Plays one round: picks a strategy, gets the computer move,
     * decides the winner and updates the display.
     *
     * @param playerMove the player's move: "R", "P" or "S"
     */
    private void playRound(String playerMove)
    {
        // pick a strategy using a number from 1 - 100
        int chance = rnd.nextInt(100) + 1;
        Strategy strategy;
        String strategyName;

        if (chance <= 10)
        {
            strategy = cheat;
            strategyName = "Cheat";
        }
        else if (chance <= 30)
        {
            strategy = leastUsed;
            strategyName = "Least Used";
        }
        else if (chance <= 50)
        {
            strategy = mostUsed;
            strategyName = "Most Used";
        }
        else if (chance <= 70)
        {
            strategy = lastUsed;
            strategyName = "Last Used";
        }
        else
        {
            strategy = randomStrategy;
            strategyName = "Random";
        }

        String computerMove = strategy.getMove(playerMove);

        // build the result line
        String result;
        if (playerMove.equals(computerMove))
        {
            ties++;
            result = getName(playerMove) + " vs " + getName(computerMove) + " (Tie!";
        }
        else if (beats(playerMove, computerMove))
        {
            playerWins++;
            result = getWinText(playerMove) + " (Player wins!";
        }
        else
        {
            computerWins++;
            result = getWinText(computerMove) + " (Computer wins!";
        }
        resultsArea.append(result + " Computer: " + strategyName + ")\n");

        // update the stats
        playerWinsField.setText("" + playerWins);
        computerWinsField.setText("" + computerWins);
        tiesField.setText("" + ties);

        // record the player move for next time
        recordPlayerMove(playerMove);
    }

    /**
     * Saves the player's move so the strategies can use it later.
     *
     * @param playerMove the player's move
     */
    private void recordPlayerMove(String playerMove)
    {
        switch (playerMove)
        {
            case "R":
                playerRockCount++;
                break;
            case "P":
                playerPaperCount++;
                break;
            case "S":
                playerScissorsCount++;
                break;
        }
        lastPlayerMove = playerMove;
    }

    /**
     * Checks if the first move beats the second move.
     *
     * @param move1 the first move
     * @param move2 the second move
     * @return true if move1 wins
     */
    private boolean beats(String move1, String move2)
    {
        return (move1.equals("R") && move2.equals("S")) ||
               (move1.equals("P") && move2.equals("R")) ||
               (move1.equals("S") && move2.equals("P"));
    }

    /**
     * Gets the move that beats the given move.
     *
     * @param move "R", "P" or "S"
     * @return the move that wins against it
     */
    private String getWinningMove(String move)
    {
        switch (move)
        {
            case "R":
                return "P";
            case "P":
                return "S";
            default:
                return "R";
        }
    }

    /**
     * Gets the text for a win by the given move.
     *
     * @param winningMove the move that won
     * @return text such as "Rock breaks Scissors"
     */
    private String getWinText(String winningMove)
    {
        switch (winningMove)
        {
            case "R":
                return "Rock breaks Scissors";
            case "P":
                return "Paper covers Rock";
            default:
                return "Scissors cut Paper";
        }
    }

    /**
     * Gets the full name of a move.
     *
     * @param move "R", "P" or "S"
     * @return "Rock", "Paper" or "Scissors"
     */
    private String getName(String move)
    {
        switch (move)
        {
            case "R":
                return "Rock";
            case "P":
                return "Paper";
            default:
                return "Scissors";
        }
    }

    // ---------- inner class strategies (they need the player history) ----------

    /**
     * Least Used: beat the symbol the player has used the least.
     */
    private class LeastUsed implements Strategy
    {
        /**
         * Picks the move that beats the player's least used symbol.
         *
         * @param playerMove the player's move (not used)
         * @return the computer's move
         */
        @Override
        public String getMove(String playerMove)
        {
            String leastMove = "R";
            int leastCount = playerRockCount;

            if (playerPaperCount < leastCount)
            {
                leastMove = "P";
                leastCount = playerPaperCount;
            }
            if (playerScissorsCount < leastCount)
            {
                leastMove = "S";
            }
            return getWinningMove(leastMove);
        }
    }

    /**
     * Most Used: beat the symbol the player has used the most.
     */
    private class MostUsed implements Strategy
    {
        /**
         * Picks the move that beats the player's most used symbol.
         *
         * @param playerMove the player's move (not used)
         * @return the computer's move
         */
        @Override
        public String getMove(String playerMove)
        {
            String mostMove = "R";
            int mostCount = playerRockCount;

            if (playerPaperCount > mostCount)
            {
                mostMove = "P";
                mostCount = playerPaperCount;
            }
            if (playerScissorsCount > mostCount)
            {
                mostMove = "S";
            }
            return getWinningMove(mostMove);
        }
    }

    /**
     * Last Used: play the symbol the player used last round.
     * On the first round there is no last move, so it plays randomly.
     */
    private class LastUsed implements Strategy
    {
        /**
         * Returns the player's move from the last round.
         *
         * @param playerMove the player's move (not used)
         * @return the computer's move
         */
        @Override
        public String getMove(String playerMove)
        {
            if (lastPlayerMove.isEmpty())
            {
                return randomStrategy.getMove(playerMove); // first round
            }
            return lastPlayerMove;
        }
    }
}
