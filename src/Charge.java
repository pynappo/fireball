/**
 * A Move that gives the player 1 charge, but leaves the player vulnerable to Fireballs.
 *
 * @author David
 */

public class Charge extends Move {

  /**
   * Creates a Charge with the corresponding name and icon.
   */
  public Charge() {
    super("Charge", "🔃", -1);
  }

  /**
   * Determines whether this move beats opponentMove.
   *
   * @param opponentMove The move to compare against.
   * @return Whether this move beats opponentMove.
   */
  public boolean win(Move opponentMove) {
    return false;
  }

  /**
   * Determines whether this move loses to the opponentMove.
   *
   * @param opponentMove The move to compare against.
   * @return Whether this move loses to the opponentMove.
   */
  public boolean lose(Move opponentMove) {
    return opponentMove.getName().equals(new SuperFireball().getName()) || opponentMove.getName().equals(new Fireball().getName());
  }
}