/**
 * Cheat strategy: the computer looks at the player's move and picks the winner.
 *
 * @author Kirby Fortney
 */
public class Cheat implements Strategy
{
    /**
     * Returns the move that beats the player's move.
     *
     * @param playerMove the player's move: "R", "P" or "S"
     * @return the winning move for the computer
     */
    @Override
    public String getMove(String playerMove)
    {
        String computerMove = "";
        switch (playerMove)
        {
            case "R":
                computerMove = "P";
                break;
            case "P":
                computerMove = "S";
                break;
            case "S":
                computerMove = "R";
                break;
            default:
                computerMove = "X";
                break;
        }
        return computerMove;
    }
}
