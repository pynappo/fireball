/**
 * Holds information on the winner and score of those who've played a full game of Fireball.
 *
 * @author Armeet
 */

public class GameResult {
  private final Player player;
  private final int score;

  /**
   * Constructor
   *
   * @param player The player who played.
   * @param score  The score of the game.
   */
  public GameResult(Player player, int score) {
    this.player = player;
    this.score = score;
  }

  /**
   * Returns the winner of the game
   *
   * @return Winner of the game.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Returns the score of the winner
   *
   * @return score of the winner
   */
  public int getScore() {
    return score;
  }
}