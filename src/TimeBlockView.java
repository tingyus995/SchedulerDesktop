import javax.swing.*;
import java.awt.*;

public class TimeBlockView extends JPanel {
    TimeBlockView(){
        super();
        setBackground(Color.BLUE);
        JLabel label = new JLabel("TimeBlockView", SwingConstants.CENTER);
        add(label);
    }
}
