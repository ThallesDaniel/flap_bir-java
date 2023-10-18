import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private int WIDTH = 400;
    private int HEIGHT = 600;
    private int GROUND_HEIGHT = 100;
    private int SPEED = 5;
    private double GRAVITY = 0.4;
    private int JUMP_STRENGTH = 10;
    Image birdImage = new ImageIcon("bird.png").getImage();
    int birdSize = 30;
    Image bird_img = birdImage.getScaledInstance(birdSize, birdSize, Image.SCALE_SMOOTH);
    Image ground_img = new ImageIcon("ground.png").getImage();

    private List<Rectangle> obstacles;
    private int birdX = 50;
    private int birdY;
    private double birdVel;
    private int score;
    private boolean gameover;

    public FlappyBird() {
        obstacles = new ArrayList<>();
        birdY = (HEIGHT - GROUND_HEIGHT) / 2;
        birdVel = 0;
        score = 0;
        gameover = false;
        Timer timer = new Timer(30, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocus();
    }

    public void createObstacle() {
        Random random = new Random();
        int obstacleHeight = random.nextInt(201) + 100;
        int obstacleY = 0;
        int bottomObstacleHeight = HEIGHT - GROUND_HEIGHT - obstacleHeight - 200;
        int bottomObstacleY = obstacleHeight + 200;
        obstacles.add(new Rectangle(WIDTH, obstacleY, 50, obstacleHeight));
        obstacles.add(new Rectangle(WIDTH, bottomObstacleY, 50, bottomObstacleHeight));
    }

    public void restartGame() {
        birdY = (HEIGHT - GROUND_HEIGHT) / 2;
        birdVel = 0;
        obstacles.clear();
        score = 0;
        gameover = false;
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameover) {
            birdVel += GRAVITY;
            birdY += birdVel;

            if (birdY >= HEIGHT - GROUND_HEIGHT) {
                birdY = HEIGHT - GROUND_HEIGHT;
                birdVel = 0;
            }

            if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).x < WIDTH - 200) {
                createObstacle();
            }

            for (int i = 0; i < obstacles.size(); i++) {
                Rectangle obstacle = obstacles.get(i);
                obstacle.x -= SPEED;
                if (obstacle.x < -50) {
                    obstacles.remove(i);
                    score++;
                    i--;
                }
            }

            for (int i = 0; i < obstacles.size(); i += 2) {
                Rectangle obstacle = obstacles.get(i);
                Rectangle bottomObstacle = obstacles.get(i + 1);
                if (birdX + birdSize > obstacle.x && birdX < obstacle.x + 50) {
                    if (birdY < obstacle.height || birdY + birdSize > bottomObstacle.y) {
                        gameover = true;
                    }
                }
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLUE);
        g.drawImage(bird_img, birdX, birdY, null);
        for (Rectangle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }
        g.drawImage(ground_img, 0, HEIGHT - GROUND_HEIGHT, null);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.setColor(Color.BLUE);
        g.drawString("Pontuação: " + score, 10, 30);
        if (gameover) {
            g.setColor(Color.RED);
            g.drawString("Você perdeu! Pontuação: " + score, WIDTH / 2 - 150, HEIGHT / 2 - 50);
            g.drawString("Pressione 'R' para recomeçar", WIDTH / 2 - 180, HEIGHT / 2 + 50);
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE && !gameover) {
            birdVel = -JUMP_STRENGTH;
        } else if (key == KeyEvent.VK_R && gameover) {
            restartGame();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.setSize(flappyBird.WIDTH, flappyBird.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
