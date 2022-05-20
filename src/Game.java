import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fireball is a game that involves 2 Players, who each have access to moves such as
 * shields, fireballs, and reloads that allow them to fight each other in a manner similar to Rock-Paper-Scissors.
 * <p>
 * Each turn, both Players choose 1 Move from the following Moves:
 * <ul>
 *   <li> A fireball that can beat the opponent if they are reloading. </li>
 *   <li> A charge which gives the Player one charge to use for fireballs. </li>
 *   <li> A shield that blocks fireballs. </li>
 *   <li> A super fireball that takes 5 charges and beats all other moves. </li>
 * </ul>
 * <p>
 * The user plays against an AI Player until they lose a round. The user's score is the number of rounds they won.
 * <p>
 * The helper classes PlayerPanel and MoveButton help create components displaying game information in the Fireball GUI.
 *
 * @author David, Dougy
 */
public class Game extends JFrame {
  /**
   * The Player that the user plays as.
   */
  private Player player;
  /**
   * The personalized AI for each game.
   */
  private AI ai;
  /**
   * The number of rounds played.
   */
  private int round;

  /**
   * Creates a new Game instance and sets up the setup GUI.
   */
  public Game() {
    super("Fireball Game");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    JPanel gameSetup = new JPanel();

    // Set up prompt for player name
    JPanel playerNameInput = new JPanel();
    JTextField nameField = new JTextField(15);
    playerNameInput.add(new JLabel("Set name:"));
    playerNameInput.add(nameField);

    // Create game and hide UI once name is submitted
    JButton submit = new JButton("Submit");
    submit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        round = 1;
        player = new Player(nameField.getText());
        ai = new AI();
        play();
        gameSetup.setVisible(false);
      }
    });

    // Format and display window
    gameSetup.add(playerNameInput);
    gameSetup.add(submit);
    add(gameSetup, BorderLayout.SOUTH);
  }

  /**
   * Creates and facilitates a Fireball game along with its GUI.
   */
  public void play() {

    // Set up moves
    Move fireball = new Fireball();
    Move shield = new Shield();
    Move charge = new Charge();
    Move superFireball = new SuperFireball();

    // Set up bottom move panel with the above moves
    MoveButton fireballButton = new MoveButton(fireball);
    MoveButton superFireballButton = new MoveButton(superFireball);
    MoveButton shieldButton = new MoveButton(shield);
    MoveButton chargeButton = new MoveButton(charge);

    MoveButton[] moveButtons = {fireballButton, superFireballButton, shieldButton, chargeButton};

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new GridLayout(2, 1, 0, 0));
    JPanel movePanel = new JPanel();
    movePanel.setLayout(new GridLayout(2, 2, 0, 0));

    bottomPanel.add(new JLabel(player.getName() + ", select a move below:"));
    bottomPanel.add(movePanel);
    PlayerPanel userStatus = new PlayerPanel(player);
    PlayerPanel aiStatus = new PlayerPanel(ai);

    final int[] turnCount = {1}; // (has to be an Array to be accessed within the ActionPerformed)

    // Create the panel that shows round number
    JPanel scorePanel = new JPanel();
    JLabel scoreLabel = new JLabel("Round " + (round));
    scorePanel.add(scoreLabel);

    // Create the panel that shows turn number
    JPanel turnPanel = new JPanel();
    JLabel turnLabel = new JLabel("Turn 1");
    turnPanel.add(turnLabel);

    // Format the GUI
    setLayout(new BorderLayout());
    add(userStatus, BorderLayout.WEST);
    add(aiStatus, BorderLayout.EAST);
    add(turnPanel, BorderLayout.CENTER);
    add(scorePanel, BorderLayout.NORTH);
    add(bottomPanel, BorderLayout.SOUTH);

    // Panels to toggle off when the user loses
    JPanel[] gamePanels = {scorePanel, turnPanel, aiStatus, userStatus, bottomPanel};

    // Main game loop, executes every time a button is pressed
    ActionListener moveListener = new ActionListener() {
      public void actionPerformed(ActionEvent move) {
        if (scorePanel.getBackground() == Color.GREEN) {
          scorePanel.setBackground(Color.WHITE);
          scoreLabel.setText("Round: " + round);
        }
        Move playerMove;
        int pCharge = player.getCharge();
        int aiCharge = ai.getCharge();

        // The AI is not cheating >_<
        Move aiMove = ai.makeMove(pCharge, aiCharge);

        String moveName = move.getActionCommand();
        if (moveName.equals(fireball.getName())) {
          playerMove = fireball;
          ai.updateUsersMove(pCharge, aiCharge, 0); // update AI
        } else if (moveName.equals(shield.getName())) {
          playerMove = shield;
          ai.updateUsersMove(pCharge, aiCharge, 1); // update AI
        } else if (moveName.equals(charge.getName())) {
          playerMove = charge;
          ai.updateUsersMove(pCharge, aiCharge, 2); // update AI
        } else {
          playerMove = superFireball;
        }

        // Apply move cost and add Move to both player's move histories
        player.useMove(playerMove);
        ai.useMove(aiMove);

        // update AI
        ai.computeProbability();

        // If the player wins, start a new round.
        if (playerMove.win(aiMove)) {
          scoreLabel.setText("Nice! You won Round " + round);
          scorePanel.setBackground(Color.GREEN);
          round++;
          player.setCharge(0);
          ai.setCharge(0);
          turnCount[0] = 1;
        }

        // If the player loses, hide all gameplay panels and run the end() procedure
        else if (playerMove.lose(aiMove)) {
          for (JPanel panel : gamePanels) {
            remove(panel);
          }
          remove(movePanel);
          end();
        }

        // Refresh the panel for the next iteration of the game.
        userStatus.refresh();
        aiStatus.refresh();
        turnLabel.setText("Turn: " + turnCount[0]);
        turnCount[0]++;
        for (MoveButton mb : moveButtons) {
          mb.setEnabled(mb.getMove().getCost() <= player.getCharge());
        }

        // Repaint GUI
        revalidate();
        repaint();
      }
    };

    // Configure all move buttons properly
    for (MoveButton mb : moveButtons) {
      mb.addActionListener(moveListener);
      mb.setEnabled(mb.getMove().getCost() <= player.getCharge());
      movePanel.add(mb);
    }

    // Repaint GUI
    revalidate();
    repaint();
  }

  /**
   * Displays the end screen reviewing the user's performance and adds their GameResult to the Leaderboard.
   */
  public void end() {
    JLabel endLabel = new JLabel();
    if (round > 1)
      endLabel.setText("Good game! You lost after " + (round - 1) + " round(s).");
    else
      endLabel.setText("Darn, you didn't win a round this time.");
    JButton endButton = new JButton("GG.");

    // Set the endButton to close the window
    endButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    JPanel endPanel = new JPanel();

    // Format and display the end panel 
    endPanel.add(endLabel, BorderLayout.CENTER);
    endPanel.add(endButton, BorderLayout.SOUTH);
    add(endPanel, BorderLayout.SOUTH);
    endPanel.setVisible(true);

    // Update the window
    revalidate();
    repaint();

    // Add the result to the leaderboard
    Main.leaderboard.addResult(new GameResult(player, round - 1));
  }
}

/**
 * A JButton with a simple setup to be linked to a specified Move.
 */
class MoveButton extends JButton {
  private final Move MOVE;

  public MoveButton(Move move) {
    super(move.getName() + ' ' + move.getIcon() + " (" + move.getCost() * -1 + ")");
    MOVE = move;
    setActionCommand(MOVE.getName());
  }

  /**
   * Returns the move that this MoveButton is representing.
   *
   * @return The move that this MoveButton is representing.
   */
  public Move getMove() {
    return MOVE;
  }
}

/**
 * A JPanel with a simple setup to hold Player information.
 */
class PlayerPanel extends JPanel {
  private final Player PLAYER;
  private final JLabel P_CHARGES = new JLabel();
  private final JLabel P_LAST_MOVE = new JLabel();

  /**
   * Sets up the player panel.
   *
   * @param player The Player whose information will be displayed.
   */
  public PlayerPanel(Player player) {
    setLayout(new GridLayout(5, 1, 0, 5));
    this.PLAYER = player;

    JLabel namePanel = new JLabel();
    namePanel.setText(PLAYER.getName());
    refresh();
    for (JLabel panel : new JLabel[]{namePanel, P_CHARGES, P_LAST_MOVE}) {
      panel.setHorizontalAlignment(SwingConstants.CENTER);
      add(panel);
    }
  }

  /**
   * Updates the PlayerPanel with the latest player information.
   */
  public void refresh() {
    P_CHARGES.setText("⚡ Charges: " + PLAYER.getCharge());
    ArrayList<Move> moveHist = PLAYER.getMoveHistory();
    P_LAST_MOVE.setText("Last Move: " + moveHist.get(moveHist.size() - 1).getIcon());
  }
}