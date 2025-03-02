import java.awt.*;
import javax.swing.*;

public class TicTacToe extends JPanel {
    private final JLabel textLabel;
    private final JButton[] buttons;
    private int player = 0;

    public TicTacToe() {
        setLayout(new BorderLayout());

        textLabel = new JLabel("Player 1 turn (X)", SwingConstants.CENTER);
        textLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        add(textLabel, BorderLayout.NORTH);

        JPanel board = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[9];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = createBoardButton(i);
            board.add(buttons[i]);
        }
        add(board, BorderLayout.CENTER);

        setPreferredSize(new Dimension(950, 850));
    }

    private JButton createBoardButton(int index) {
        JButton button = new JButton("");
        button.setFont(new Font("SansSerif", Font.PLAIN, 120));
        button.setFocusPainted(false);
        button.addActionListener(e -> handleButtonClick(index));
        return button;
    }

    private void handleButtonClick(int index) {
        JButton button = buttons[index];
        if (!button.getText().isEmpty()) return;

        button.setText(player % 2 == 0 ? "X" : "O");
        textLabel.setText(player % 2 == 0 ? "Player 2 turn (O)" : "Player 1 turn (X)");
        player++;

        if (checkWin()) {
            textLabel.setText(button.getText() + " wins!");
            disableButtons();
        } else if (isBoardFull()) {
            textLabel.setText("It's a draw!");
        }
    }

    private boolean checkWin() {
        String[][] patterns = {
                {buttons[0].getText(), buttons[1].getText(), buttons[2].getText()},
                {buttons[3].getText(), buttons[4].getText(), buttons[5].getText()},
                {buttons[6].getText(), buttons[7].getText(), buttons[8].getText()},
                {buttons[0].getText(), buttons[3].getText(), buttons[6].getText()},
                {buttons[1].getText(), buttons[4].getText(), buttons[7].getText()},
                {buttons[2].getText(), buttons[5].getText(), buttons[8].getText()},
                {buttons[0].getText(), buttons[4].getText(), buttons[8].getText()},
                {buttons[2].getText(), buttons[4].getText(), buttons[6].getText()},
        };

        for (String[] pattern : patterns) {
            if (!pattern[0].isEmpty() && pattern[0].equals(pattern[1]) && pattern[0].equals(pattern[2])) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (JButton button : buttons) {
            if (button.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void disableButtons() {
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
    }
}
