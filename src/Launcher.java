import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A simple GUI menu for the user to launch other parts of the program.
 *
 * @author Armeet, David
 */
public class Launcher extends JFrame implements ActionListener {
  /**
   * Creates an instance of the Launcher.
   */
  public Launcher() {
    super("Fireball Launcher");
    setSize(400, 400);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new GridLayout(0, 1, 0, 10));

    JLabel pageLabel = new JLabel("Fireball Game 🔥");
    pageLabel.setHorizontalAlignment(JLabel.CENTER);
    pageLabel.setFont(new Font("Sans Serif", Font.BOLD, 30));

    JButton leaderboardButton = new JButton("Leaderboard");
    leaderboardButton.setActionCommand("leaderboard");
    leaderboardButton.addActionListener(this);

    JButton playButton = new JButton("Play Game!");
    playButton.setActionCommand("play");
    playButton.addActionListener(this);

    add(pageLabel);
    add(leaderboardButton);
    add(playButton);
  }

  /**
   * Launches a new instance of the specified window or program when an action occurs.
   *
   * @param e The action that occurred.
   */
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    if (command.equals("leaderboard")) {
      Main.leaderboard.display();
    } else if (command.equals("play")) {
      Game g = new Game();
      g.setVisible(true);
    }
  }
}