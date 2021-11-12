import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private int size = 320;
    private int scale = 16;
    private int allDots = 400;
    private Image dot;
    private Image apple;
    private Image face;
    private Image faceUp;
    private Image faceDown;
    private Image faceRight;
    private Image faceLeft;
    private Image kill;
    private int indexDead;
    private int appleX;
    private int appleY;
    private int[] x = new int[allDots];
    private int[] y = new int[allDots];
    private int length;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    private Color[] colors = new Color[]{Color.LIGHT_GRAY, Color.PINK, Color.WHITE};
    private int index = 0;
    private String[] arrayLabels = new String[]{"1X", "1.5X", "2X", "2.5X"};
    private int[] timeDelay = new int[]{100, 50, 25, 15};
    private int index2 = 0;
    private JLabel label = new JLabel("SCORE:");
    private JLabel label2 = new JLabel("0");
    private JLabel label3 = new JLabel("SPEED:");
    private JLabel label4 = new JLabel(arrayLabels[index2]);

    public GameField() {
        addLabels();
        loadImages();
        initGame();
        addKeyListener(new KeyListener());
        setFocusable(true);
    }

    public void addLabels() {
        setLayout(null);
        label.setForeground(Color.BLACK);
        label.setBounds(3, 248, 100, 100);
        label2.setForeground(Color.BLACK);
        label2.setBounds(53, 248, 100, 100);
        label3.setForeground(Color.BLACK);
        label3.setBounds(3, 260, 100, 100);
        label4.setForeground(Color.BLACK);
        label4.setBounds(53, 260, 100, 100);
        add(label);
        add(label2);
        add(label3);
        add(label4);
    }

    private void restartGame() {
        inGame = true;
        up = false;
        down = false;
        left = false;
        right = true;
        index = 0;
        Arrays.fill(x, 0);
        Arrays.fill(y, 0);
        label2.setText("0");
        label4.setText(arrayLabels[index2]);
        initGame();
    }

    public void initGame() {
        setBackground(colors[index++]);
        length = 2;
        indexDead = -1;
        for (int i = 0; i < length; i++) {
            x[i] = 48 - i * scale;
            y[i] = 48;
        }
        timer = new Timer(timeDelay[index2], this);
        timer.start();
        createApple();
    }

    public void createApple() {
        appleX = new Random().nextInt(20) * scale;
        appleY = new Random().nextInt(20) * scale;
    }

    public void loadImages() {
        dot = new ImageIcon("Image/dot.png").getImage();
        apple = new ImageIcon("Image/apple.png").getImage();
        faceUp = new ImageIcon("Image/faceUp.png").getImage();
        faceRight = new ImageIcon("Image/faceRight.png").getImage();
        faceDown = new ImageIcon("Image/faceDown.png").getImage();
        faceLeft = new ImageIcon("Image/faceLeft.png").getImage();
        kill = new ImageIcon("Image/kill.png").getImage();
    }

    public void move() {
        if(!inGame){
            return;
        }
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= scale;
            face = faceLeft;
        } else if (right) {
            x[0] += scale;
            face = faceRight;
        } else if (up) {
            y[0] -= scale;
            face = faceUp;
        } else if (down) {
            y[0] += scale;
            face = faceDown;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            MySimpleThread thread = new MySimpleThread("Sound/Eat.wav");
            thread.start();
            length++;
            label2.setText("" + (length - 2));
            if (index == colors.length) {
                index = 0;
            }
            setBackground(colors[index++]);
            createApple();
        }
    }

    public void checkCollisions() {
        for (int i = length; i > 0; i--) {
            if (i > 3 && x[0] == x[i] && y[0] == y[i]) {
                indexDead = i;
                inGame = false;
            }
        }
        if (x[0] > size - scale) {
            x[0] = 0;
        } else if (x[0] < 0) {
            x[0] = size - scale;
        } else if (y[0] > size - scale) {
            y[0] = 0;
        } else if (y[0] < 0) {
            y[0] = size - scale;
        }
        checkApple();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
            g.drawImage(apple, appleX, appleY, this);
            g.drawImage(face, x[0], y[0], this);
            for (int i = 1; i <= length; i++) {
                if(i==indexDead){
                    g.drawImage(kill,x[indexDead], y[indexDead], this);
                    continue;
                }
                g.drawImage(dot, x[i], y[i], this);
            }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        else{
            timer.stop();
            Sound.playSound("Sound/Fail.wav").join();
            SwingUtils.showInfoMessageBox("Вы проиграли!");
            restartGame();
        }
        repaint();
    }

    private class KeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && !right) {
                left = true;
                up = false;
                down = false;
            }
            else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && !left) {
                right = true;
                up = false;
                down = false;
            }
            else if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && !down) {
                left = false;
                up = true;
                right = false;
            }
            else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && !up) {
                left = false;
                right = false;
                down = true;
            }
            if (key == KeyEvent.VK_R) {
                timer.stop();
                Sound.playSound("Sound/Restart.wav").join();
                restartGame();
            }
            if (key == KeyEvent.VK_E && index2 != timeDelay.length - 1) {
                timer.stop();
                MySimpleThread thread = new MySimpleThread("Sound/click.wav");
                thread.start();
                index2++;
                restartGame();
            }
            if (key == KeyEvent.VK_F && index2 != 0) {
                timer.stop();
                MySimpleThread thread = new MySimpleThread("Sound/click.wav");
                thread.start();
                index2--;
                restartGame();
            }
        }
    }
}
