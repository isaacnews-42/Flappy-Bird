import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Main{
        public static void main(String[] args){

        int boardWidth = 360;
        int boardHeight = 640;

                JFrame frame = new JFrame("Flappy Bird");
                frame.setSize(boardWidth, boardHeight);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Game flappyBird = new Game();
                frame.add(flappyBird);
                frame.pack();
                flappyBird.requestFocus();
                frame.setVisible(true);
        }

}