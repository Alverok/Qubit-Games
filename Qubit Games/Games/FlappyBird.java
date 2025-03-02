import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private final int boardWidth = 950;
    private final int boardHeight = 850;
    private Image backgroundImg;
    private Bird bird;
    private ArrayList<Pipe> pipes;
    private Timer gameLoop;
    private Timer placePipeTimer;
    private boolean gameOver = false;
    private double score = 0;
    private int velocityY = 0;

    public FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
        loadImages();
        initializeGame();
    }

    private void loadImages() {
        backgroundImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/flappybirdbg.png"))).getImage();
        Image birdImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/flappybird.png"))).getImage();
        bird = new Bird(birdImg);
        pipes = new ArrayList<>();
    }

    private void initializeGame() {
        placePipeTimer = new Timer(1500, e -> placePipes());
        placePipeTimer.start();
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    private void placePipes() {
        int randomPipeY = (int) (0 - (double) 512 / 4 - Math.random() * ((double) 512 / 2));
        int openingSpace = boardHeight / 4;
        Image topPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/toppipe.png"))).getImage();
        Image bottomPipeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/bottompipe.png"))).getImage();

        pipes.add(new Pipe(topPipeImg, boardWidth, randomPipeY));
        pipes.add(new Pipe(bottomPipeImg, boardWidth, randomPipeY + 512 + openingSpace));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
        g.drawImage(bird.getImage(), bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight(), null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.getImage(), pipe.getX(), pipe.getY(), pipe.getWidth(), pipe.getHeight(), null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + (int) score, 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    private void move() {
        int gravity = 1;
        velocityY += gravity;
        bird.move(velocityY);

        for (Pipe pipe : pipes) {
            int velocityX = -4;
            pipe.move(velocityX);

            if (!pipe.isPassed() && bird.getX() > pipe.getX() + pipe.getWidth()) {
                score += 0.5;
                pipe.setPassed(true);
            }

            if (bird.collidesWith(pipe)) {
                gameOver = true;
            }
        }

        if (bird.getY() > boardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if (gameOver) {
                resetGame();
            }
        }
    }

    private void resetGame() {
        bird.setY(boardHeight / 2);
        velocityY = 0;
        pipes.clear();
        gameOver = false;
        score = 0;
        gameLoop.start();
        placePipeTimer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private static class Bird {
        private final int x;
        private int y;
        private final int width = 34;
        private final int height = 24;
        private final Image img;

        public Bird(Image img) {
            this.img = img;
            this.x = 950 / 4;
            this.y = 850 / 2;
        }

        public void move(int dy) {
            y += dy;
            y = Math.max(y, 0);
        }

        public boolean collidesWith(Pipe pipe) {
            return x < pipe.getX() + pipe.getWidth() &&
                    x + width > pipe.getX() &&
                    y < pipe.getY() + pipe.getHeight() &&
                    y + height > pipe.getY();
        }

        public Image getImage() {
            return img;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    private static class Pipe {
        private int x;
        private final int y;
        private final Image img;
        private boolean passed = false;

        public Pipe(Image img, int x, int y) {
            this.img = img;
            this.x = x;
            this.y = y;
        }

        public void move(int dx) {
            x += dx;
        }

        public Image getImage() {
            return img;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return 64;
        }

        public int getHeight() {
            return 512;
        }

        public boolean isPassed() {
            return passed;
        }

        public void setPassed(boolean passed) {
            this.passed = passed;
        }
    }
}
