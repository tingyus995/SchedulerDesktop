import javax.swing.*;
import java.awt.*;

public class SettingsView extends JPanel {
    SettingsView(){
        super();
        setBackground(Color.CYAN);
        JLabel label = new JLabel("SettingsView", SwingConstants.CENTER);
        add(label);
    }
}
