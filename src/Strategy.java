/**
 * A strategy the computer uses to pick its move.
 *
 * @author Kirby Fortney
 */
public interface Strategy
{
    /**
     * Picks the computer's move.
     *
     * @param playerMove the player's move: "R", "P" or "S"
     * @return the computer's move: "R", "P" or "S"
     */
    public String getMove(String playerMove);
}
