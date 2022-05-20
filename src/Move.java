import java.util.Objects;

/**
 * The base class to store the details of any move that a Player may want to use,
 * as well as provide helper methods to determine the effects of moves being used against each other.
 *
 * @author Armeet
 */

public class Move {

  private final String name;
  private final String icon;
  private int cost;

  /**
   * Constructor with default values for the moves.
   */
  public Move() {
    name = "N/A";
    icon = "";
    cost = 0;
  }

  /**
   * Constructor with given name, icon, and default (0) cost.
   *
   * @param name Name of move.
   * @param icon Icon of move.
   */
  public Move(String name, String icon) {
    super();
    this.name = name;
    this.icon = icon;
  }

  /**
   * Constructor with given name, icon, and cost.
   *
   * @param name Name of move.
   * @param icon Icon of move.
   * @param cost Cost of move.
   */
  public Move(String name, String icon, int cost) {
    super();
    this.name = name;
    this.icon = icon;
    this.cost = cost;
  }

  /**
   * Returns the name of the move.
   *
   * @return Name of the move.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the icon of the Move.
   *
   * @return Icon of the Move.
   */

  public String getIcon() {
    return icon;
  }

  /**
   * Returns cost of the move.
   *
   * @return Cost of the move.
   */
  public int getCost() {
    return cost;
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
    return opponentMove.getName().equals(new SuperFireball().getName());
  }
}