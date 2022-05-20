import java.util.ArrayList;

/**
 * Holds player information and move history. Also provides methods to facilitate easy gameplay implementation.
 *
 * @author Armeet, David, Dougy
 */
public class Player {
  private final ArrayList<Move> moveHistory = new ArrayList<Move>();
  private final String name;
  private int charge;

  /**
   * Constructor that creates a Player with given name.
   *
   * @param name The name that the Player should have.
   */
  public Player(String name) {
    this.name = name;
    charge = 0;
    moveHistory.add(new Move());
  }

  /**
   * Updates the name of the player.
   *
   * @return Name of the player.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the charge of the player.
   *
   * @return Charge of the player.
   */
  public int getCharge() {
    return charge;
  }

  /**
   * Updates the charge of the player.
   *
   * @param newCharge New charge amount for the player.
   */
  public void setCharge(int newCharge) {
    charge = newCharge;
  }

  /**
   * Uses the given move - the Move is added to the player's move history, and player charge amount is changed appropriately.
   *
   * @param move - the Move the user chooses to perform.
   */
  public void useMove(Move move) {
    charge -= move.getCost();
    moveHistory.add(move);
  }

  /**
   * Returns the move history of the player.
   *
   * @return ArrayList of past Moves made by the player
   */
  public ArrayList<Move> getMoveHistory() {
    return moveHistory;
  }

  /**
   * Returns the total amount of charge used (which equals to the total cost of
   * the moves)
   *
   * @return Amount of remaining charge
   */
  public int getRemainingCharge() {
    int sum = 0;
    for (Move cur : moveHistory) {
      sum += cur.getCost();
    }
    return sum;
  }
}