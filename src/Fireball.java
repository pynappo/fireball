/**
 * A 1-cost Move which can beat the opponent while they are charging. Neutralizes itself.
 *
 * @author David
 */

public class Fireball extends Move {

  /**
   * Creates a Fireball with the corresponding name and icon.
   */
  public Fireball() {
    super("Fireball", "🔥", 1);
  }

  /**
   * Determines whether this move beats opponentMove.
   *
   * @param opponentMove The move to compare against.
   * @return Whether this move beats opponentMove.
   */
  public boolean win(Move opponentMove) {
    return opponentMove.getName().equals(new Charge().getName());
  }

  /**
   * Determines whether this move loses to the opponentMove.
   *
   * @param opponentMove The move to compare against.
   * @return Whether this move loses to the opponentMove.
   */
  public boolean lose(Move opponentMove) {
    return opponentMove.getName().equals(new SuperFireball().getName());
  }
}