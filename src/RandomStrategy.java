import java.util.Random;

/**
 * Random strategy: the computer picks Rock, Paper or Scissors at random.
 *
 * @author Kirby Fortney
 */
public class RandomStrategy implements Strategy
{
    private static final String[] MOVES = {"R", "P", "S"};
    private Random rnd = new Random();

    /**
     * Returns a random move. The player's move is ignored.
     *
     * @param playerMove the player's move (not used)
     * @return "R", "P" or "S"
     */
    @Override
    public String getMove(String playerMove)
    {
        return MOVES[rnd.nextInt(MOVES.length)];
    }
}
