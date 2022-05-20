/**
 * The driver class.
 *
 * @author David
 */
class Main {
  // The leaderboard for the game.
  public static Leaderboard leaderboard = new Leaderboard();

  /**
   * Main method that creates the launcher for the game.
   *
   * @param args Arguments given when this program is run.
   */
  public static void main(String[] args) {
    Launcher launcher = new Launcher();
    launcher.setVisible(true);
  }
}