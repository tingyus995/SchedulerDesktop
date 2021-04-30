import javax.swing.*;
import java.awt.*;

public class TasksView extends JPanel {
    TasksView(){
        super();
        setBackground(Color.YELLOW);
        JLabel label = new JLabel("TasksView", SwingConstants.CENTER);
        add(label);
    }
}
