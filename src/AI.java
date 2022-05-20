/**
 * A Player that has additional methods to facilitate smart AI gameplay for the user to play against.
 *
 * @author Dougy
 * <p>
 * AI: simulate the probability using dynamic programming
 * <p>
 * States: dp[charge of player][charge of AI] - 5*5 total
 * <p>
 * Transitions: 9 total (fireball, fireball), (fireball, shield),
 * (fireball, charge), (shield, fireball), (shield, shield), (shield,
 * charge), (charge, fireball), (charge, shield), (charge, charge)
 * <p>
 * Each transition also has associated edges (a double value) with them
 * that tell the probability that the user or the AI is going choose
 * this transition; the edge value will actively change mid-game based
 * on the number of times the user selected the particular transition
 * associated with it as the game goes on - meaning the AI will adapt
 * to the player and adjust its model (sort of like a neural network)
 * so every game is different depending on the player
 * <p>
 * The probability that the AI is going to choose a move is
 * proportional to the cubed ^3 of the computed winning chance of that
 * move (meaning the AI doesn't necessarily always choose the best
 * action, so it's a little more randomized and fun)
 * <p>
 * The idea for our AI is influenced from the following source:
 * https://math.stackexchange.com/questions/1612290/optimal-strategy-for-this-schoolyard-game-charge-block-shoot
 */

public class AI extends Player {
  // The following arrays store the states and transitions
  // All arrays stores a double from 0 to 1, representing the probability of a
  // particular event
  private double[][] states = new double[6][6]; // chance of AI winning in this state
  private double[][][] winningProb = new double[6][6][3]; // chance of AI winning if it chooses move (k) in state (i, j)
  private double[][] statesCount = new double[6][6]; // number of times the game reached state (i, j)
  private double[][][] userChoice = new double[6][6][3]; // number of times the user choose action (k) in state (i, j)
  private double[][][] userProb = new double[6][6][3]; // the probability of the user choosing action (k) in state (i, j)

  /**
   * Initializes all the states and edge cases for the probability graph
   */
  public AI() {
    super("Computer");
    // winning probability initialization
    for (int i = 0; i <= 4; i++) {
      for (int j = 0; j <= 4; j++) {
        states[i][j] = winningProb[i][j][0] = winningProb[i][j][1] = winningProb[i][j][2] = (double) (1) / 2;
      }
    }
    for (int i = 0; i <= 5; i++) {
      states[i][5] = winningProb[i][5][0] = winningProb[i][5][1] = winningProb[i][5][2] = 1;
    }
    for (int i = 0; i <= 4; i++) {
      states[5][i] = winningProb[5][i][0] = winningProb[5][i][1] = winningProb[5][i][2] = 0;
    }
    states[5][5] = winningProb[5][5][0] = winningProb[5][5][1] = winningProb[5][5][2] = 0.5;

    // statesCount
    for (int i = 0; i <= 5; i++) {
      for (int j = 0; j <= 5; j++) {
        statesCount[i][j] = 3;
      }
    }
    // user's Choice
    for (int i = 0; i <= 5; i++) {
      for (int j = 0; j <= 5; j++) {
        userChoice[i][j][0] = userChoice[i][j][1] = userChoice[i][j][2] = 1;
      }
    }
    // user's Prob
    for (int i = 0; i <= 5; i++) {
      for (int j = 0; j <= 5; j++) {
        for (int k = 0; k < 3; k++) {
          userProb[i][j][k] = userChoice[i][j][k] / statesCount[i][j];
        }
      }
    }

    // hard code (edge adjustments)
    for (int i = 0; i <= 4; i++) {
      userProb[0][i][0] = 0;
      userProb[0][i][1] = userProb[0][i][2] = (double) (1) / 2; // can't fireball when user has 0 charge

      statesCount[0][i] = 2;
      userChoice[0][i][1] = userChoice[0][i][2] = 1;
      userChoice[0][i][0] = 0;

      winningProb[i][0][0] = 0; // can't fireball when AI has 0 charge
    }

    for (int i = 0; i <= 4; i++) {
      userProb[i][0][1] = 0;
      userProb[i][0][0] = userProb[i][0][2] = (double) (1) / 2; // no need to shield when the AI has 0 charge

      statesCount[i][0] = 2;
      userChoice[i][0][0] = userChoice[i][0][2] = 1;
      userChoice[i][0][1] = 0;

      winningProb[0][i][1] = 0; // no need to shield when user has 0 charge
    }

    // edge case for the beginning of the game: both player and AI should charge up
    userProb[0][0][0] = userProb[0][0][1] = 0;
    userProb[0][0][2] = 1;
    statesCount[0][0] = 1;
    userChoice[0][0][2] = 1;
    userChoice[0][0][0] = userChoice[0][0][1] = 0;
  }

  /**
   * Calculates the cube of a number.
   *
   * @param x The number to be cubed
   * @return x to the power of 3
   */
  double cube(double x) {
    return x * x * x;
  }

  /**
   * Computes the probability that the AI is going to choose a move.
   * The probability is proportional to the cube of the computed winning chance of that move.
   *
   * @param playerCharge Player's charge
   * @param aiCharge     AI's charge
   * @param i            Move index (0 for fireball, 1 for shield, 2 for charge)
   * @return The probability for the AI to choose the move indexed i, factoring in both player's charge count.
   */
  double getProbability(int playerCharge, int aiCharge, int i) {
    double sum = cube(winningProb[playerCharge][aiCharge][0]) + cube(winningProb[playerCharge][aiCharge][1]) + cube(winningProb[playerCharge][aiCharge][2]);
    return cube(winningProb[playerCharge][aiCharge][i]) / sum;
  }

  /**
   * Computes the probabilities for each state (i, j, k) uses the dynamic probability model.
   */
  public void computeProbability() {
    int i, j, k;

    for (i = 0; i < 100; i++) {
      for (j = 0; j < 5; j++) {
        for (k = 0; k < 5; k++) {
          // fireball
          if (k > 0) {
            if (j > 0) {
              winningProb[j][k][0] = userProb[j][k][0] * states[j - 1][k - 1] + userProb[j][k][1] * states[j][k - 1]
                + userProb[j][k][2] * 1;
            } else {
              winningProb[j][k][0] = userProb[j][k][1] * states[j][k - 1] + userProb[j][k][2] * 1;
            }
          }
          // shield
          if (j > 0) {
            winningProb[j][k][1] = userProb[j][k][0] * states[j - 1][k] + userProb[j][k][1] * states[j][k]
              + userProb[j][k][2] * states[j + 1][k];
          } else {
            winningProb[j][k][2] = userProb[j][k][1] * states[j][k] + userProb[j][k][2] * states[j + 1][k];
          }
          // charge
          winningProb[j][k][2] = userProb[j][k][1] * states[j][k + 1] + userProb[j][k][2] * states[j + 1][k + 1];
          // update states
          states[j][k] = getProbability(j, k, 0) * winningProb[j][k][0] + getProbability(j, k, 1) * winningProb[j][k][1]
            + getProbability(j, k, 2) * winningProb[j][k][2];
        }
      }
    }
  }

  /**
   * Make a move from state (i, j) or (player charge, AI charge)
   *
   * @param playerCharge The player's charge amount
   * @param aiCharge     The AI's charge amount
   * @return The AI's Move
   */
  public Move makeMove(int playerCharge, int aiCharge) {
    // edge case
    if (aiCharge >= 5)
      return new SuperFireball();

    if (playerCharge > 5) playerCharge = 5;

    computeProbability();

    double prob0 = getProbability(playerCharge, aiCharge, 0);
    double prob1 = getProbability(playerCharge, aiCharge, 1);
    double prob2 = getProbability(playerCharge, aiCharge, 2);

    double randomVal = Math.random();
    if (randomVal < prob0) {
      return new Fireball();
    } else if (randomVal > prob0 && randomVal < prob0 + prob1) {
      return new Shield();
    } else {
      return new Charge();
    }
  }

  /**
   * Adjust the transition and the AI model based on the user's most recent action
   *
   * @param playerCharge      Player's charge count
   * @param aiCharge          AI's charge count
   * @param playerActionIndex Player's action (0 for fireball, 1 for shield,
   *                          2 for charge)
   */
  public void updateUsersMove(int playerCharge, int aiCharge, int playerActionIndex) {
    if (playerCharge > 5) playerCharge = 5;
    statesCount[playerCharge][aiCharge]++;
    userChoice[playerCharge][aiCharge][playerActionIndex]++;
    userProb[playerCharge][aiCharge][0] = userChoice[playerCharge][aiCharge][0] / statesCount[playerCharge][aiCharge];
    userProb[playerCharge][aiCharge][1] = userChoice[playerCharge][aiCharge][1] / statesCount[playerCharge][aiCharge];
    userProb[playerCharge][aiCharge][2] = userChoice[playerCharge][aiCharge][2] / statesCount[playerCharge][aiCharge];
  }
}