import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GameStartApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow gameWindow = new GameWindow(950, 850, "Qubit Games");
            gameWindow.setVisible(true);
        });
    }
}

class GameWindow extends JFrame {
    private final int boardWidth;
    private final int boardHeight;

    public GameWindow(int width, int height, String title) {
        super(title);
        this.boardWidth = width;
        this.boardHeight = height;
        initialize();
    }

    private void initialize() {
        setSize(boardWidth, boardHeight);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelWithBackground = createBackgroundPanel();

        JButton b1 = createButton(75, e -> startGame(new FlappyBird()));
        JButton b2 = createButton(352, e -> startGame(new SnakeGame(boardWidth, boardHeight)));
        JButton b3 = createButton(622, e -> startGame(new TicTacToe()));

        panelWithBackground.setLayout(null); // Use null layout to position buttons
        panelWithBackground.add(b1);
        panelWithBackground.add(b2);
        panelWithBackground.add(b3);

        add(panelWithBackground);
    }

    private JPanel createBackgroundPanel() {
        ImageIcon backgroundIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/qubit.png")));
        Image backgroundImage = backgroundIcon.getImage();

        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
    }

    private JButton createButton(int x, ActionListener actionListener) {
        JButton button = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/play1.png"))));
        button.setBounds(x, 690, 250, 100);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(actionListener);
        return button;
    }

    private void startGame(JPanel gamePanel) {
        getContentPane().removeAll();
        getContentPane().add(gamePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        gamePanel.requestFocusInWindow();
    }
}
