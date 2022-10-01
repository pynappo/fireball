import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Holds GameResults and provides a method to display a leaderboard, sorted from highest to lowest GameResult scores.
 *
 * @author Alex, Armeet, David
 */
public class Leaderboard {
  private final ArrayList<GameResult> gameResults;

  /**
   * Creates a Leaderboard with a blank ArrayList of GameResults.
   */
  public Leaderboard() {
    gameResults = new ArrayList<GameResult>();
  }

  /**
   * Recursive mergesort that sorts a GameResult array in ascending order.
   *
   * @param a     reference to an array of GameResult to be sorted
   * @param first starting index of range of values to be sorted
   * @param last  ending index of range of values to be sorted
   */
  public static void mergeSort(ArrayList<GameResult> a, int first, int last) {
    if (first < last) {
      int mid = (first + last) / 2;
      mergeSort(a, first, mid);
      mergeSort(a, mid + 1, last);
      merge(a, first, mid, last);
    }
  }

  /**
   * Merges the sublist from a[first] to a[mid] and sublist from a[mid+1] to a[last]
   * such that the sublist from a[first] to a[last] is sorted in ascending order.
   *
   * @param a     Reference to an array of GameResult to be sorted
   * @param first Starting index of range of values to be sorted
   * @param mid   Midpoint index of range of values to be sorted
   * @param last  Last index of range of values to be sorted
   */
  private static void merge(ArrayList<GameResult> a, int first, int mid, int last) {
    int firstIndex = first;
    int secondIndex = mid + 1;
    GameResult firstResult = a.get(firstIndex);
    GameResult secondResult = a.get(secondIndex);
    while (firstIndex < secondIndex && secondIndex <= last) {
      if (firstResult.getScore() > secondResult.getScore()) {
        a.add(firstIndex++, a.remove(secondIndex++));
        if (secondIndex <= last) {
          secondResult = a.get(secondIndex);
        }
      } else {
        firstIndex++;
        if (firstIndex < secondIndex) {
          firstResult = a.get(firstIndex);
        }
      }
    }
  }

  /**
   * Shortens a player's name if it's too long.
   *
   * @param player The player whose name should be shortened.
   * @return The shortened name.
   */
  public static String concatenatedName(Player player) {
    int shortLength = 12;
    if (player.getName().length() > shortLength) return player.getName().substring(0, shortLength) + "...";
    else return player.getName();
  }

  /**
   * Creates a new window displaying the game's leaderboard.
   */
  public void display() {
    // Set up the window layout
    JFrame frame = new JFrame();
    frame.setSize(400, 400);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setLayout(new GridLayout(0, 1, 0, 10));

    // Set up the JPanel for the leaderboard
    JLabel pageLabel = new JLabel("Leaderboard");
    pageLabel.setVerticalAlignment(JLabel.TOP);
    pageLabel.setHorizontalAlignment(JLabel.CENTER);
    pageLabel.setFont(new Font("Sans Serif", Font.BOLD, 30));

    String[] columnNames = {"Player Name", "High Score"};

    mergeSort(gameResults, 0, gameResults.size() - 1);
    JTable pageTable = new JTable(getLeaderboard(), columnNames);
    pageTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
    pageTable.setFillsViewportHeight(true);

    JScrollPane scroll = new JScrollPane(pageTable);

    // Display the final leaderboard window
    frame.add(pageLabel);
    frame.add(scroll);
    frame.setVisible(true);
  }

  /**
   * Sorts the GameResults in descending order and returns a String[][] with each row representing a GameResult and each column in a row holding a value of the GameResult.
   *
   * @return A String[][] with each row representing a GameResult and each column in a row holding a value of the GameResult.
   */
  public String[][] getLeaderboard() {
    mergeSort(gameResults, 0, gameResults.size() - 1);
    String[][] leaderboard = new String[gameResults.size()][2];
    for (int r = 0; r < gameResults.size(); r++) {
      leaderboard[leaderboard.length - r - 1][0] = Leaderboard.concatenatedName(gameResults.get(r).getPlayer());
      leaderboard[leaderboard.length - r - 1][1] = String.valueOf(gameResults.get(r).getScore());
    }

    return leaderboard;
  }

  /**
   * Adds a GameResult to the leaderboard.
   *
   * @param result GameResult to be added.
   */
  public void addResult(GameResult result) {
    gameResults.add(result);
    mergeSort(gameResults, 0, gameResults.size() - 1);
  }
}