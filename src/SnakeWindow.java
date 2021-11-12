import javax.swing.*;
import java.awt.*;

public class SnakeWindow extends JFrame {

    public SnakeWindow() {
        setTitle("Змейка");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(335,357);
        setIconImage(Toolkit.getDefaultToolkit().getImage("Image/Иконка.jpg"));
        setResizable(false);
        setLocationRelativeTo(null);
        add(new GameField());
        setVisible(true);
    }

    public static void main(String[] arr) {
        new SnakeWindow();
    }
}
