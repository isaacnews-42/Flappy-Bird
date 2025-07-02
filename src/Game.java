import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener {

    int boardWidth = 360;
    int boardHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image toppipeImage;
    Image bottompipeImage;

    int birdx = boardWidth / 8;
    int birdy = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdx;
        int y = birdy;
        int width = birdWidth;
        int height = birdHeight;

        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    int pipex = birdWidth;
    int pipey = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {

        int x = pipex;
        int y = pipey;
        int width = pipeWidth;
        int height = pipeHeight;

        Image img;

        Boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    Bird bird;
    int velocityx = -4;
    int velocityy = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;

    Game() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        backgroundImage = new ImageIcon("flappybirdimages/flappybirdbg.png").getImage();
        birdImage = new ImageIcon("flappybirdimages/flappybird.png").getImage();
        bottompipeImage = new ImageIcon("flappybirdimages/bottompipe.png").getImage();
        toppipeImage = new ImageIcon("flappybirdimages/toppipe.png").getImage();

        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();

        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipeTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    void placePipes() {

        int randomPipeY = (int) (pipey - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;
        int initialPipeX = boardWidth;

        Pipe topPipe = new Pipe(toppipeImage);
        topPipe.y = randomPipeY;
        topPipe.x = initialPipeX;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottompipeImage);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        bottomPipe.x = initialPipeX;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, this.boardWidth, this.boardHeight, null);
        g.drawImage(birdImage, bird.x, bird.y, bird.width, bird.height, null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.WHITE);

        g.setFont(new Font("Tahoma", Font.BOLD, 32));
        if (gameOver) {
            g.setFont(new Font("Tahoma", Font.BOLD, 20));
            g.drawString("Game Over, final score: " + String.valueOf((int) score), 10, 25);
            g.drawString("Press Space to Restart", 10, 50);
            g.drawString("Press Escape to Exit", 10, 75);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void moveMechanic() {

        velocityy += gravity;
        bird.y += velocityy;
        bird.y = Math.max(bird.y, 0);

        for (int i = 0; i < pipes.size(); i++) {

            Pipe pipe = pipes.get(i);
            pipe.x += velocityx;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {

                score += .5;
                pipe.passed = true;

            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }

        }
        if (bird.y > boardHeight - 90) {
            gameOver = true;
        }

    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width && a.x + a.width > b.x &&
                a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveMechanic();
        repaint();
        if (gameOver) {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityy = -9;

            if (gameOver) {
                bird.y = birdy;
                velocityy = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}




