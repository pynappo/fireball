/**
 * A 5-cost Move that charges and beats any other move. Neutralizes itself.
 *
 * @author Dougy
 */

public class SuperFireball extends Move {

  /**
   * Creates a SuperFireball with the corresponding name and icon.
   */
  public SuperFireball() {
    super("Super Fireball", "⭐", 5);
  }

  /**
   * Determines whether this move beats opponentMove.
   *
   * @param opponentMove The move to compare against.
   * @return Whether this move beats opponentMove.
   */
  public boolean win(Move opponentMove) {
    return !opponentMove.getName().equals(new SuperFireball().getName());
  }

  /**
   * Determines whether this move loses to the opponentMove.
   *
   * @param opponentMove The move to compare against.
   * @return Whether this move loses to the opponentMove.
   */
  public boolean lose(Move opponentMove) {
    return false;
  }
}