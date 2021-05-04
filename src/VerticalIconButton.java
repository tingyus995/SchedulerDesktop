import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class VerticalIconButton extends JPanel {
    ImageIcon imageIcon;
    JLabel imageLabel;
    JLabel nameLabel;
    private final Color background = new Color(255, 255, 255);
    private final Color backgroundHover = new Color(240,240,240);

    VerticalIconButton(String name, String iconPath){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setBackground(background);
        // read the image
        try {
            BufferedImage bufferedImg = ImageIO.read(Objects.requireNonNull(this.getClass().getResource(iconPath)));

            Image img = bufferedImg.getScaledInstance(30, 30,Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(img);
            imageLabel = new JLabel(imageIcon);
            add(imageLabel, BorderLayout.CENTER);
        }catch (IOException e){
            System.out.println(e);
        }

        nameLabel = new JLabel(name);
        add(nameLabel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(backgroundHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(background);
            }
        });

    }
}
