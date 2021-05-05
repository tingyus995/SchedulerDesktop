import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class IconButton extends JPanel {
    ImageIcon imageIcon;
    JLabel imageLabel;

    private final float opacity;
    private final float hoverOpacity;
    private float currentOpacity;


    IconButton(String name, String iconPath, int width){
        opacity = 0.5f;
        hoverOpacity = 0.8f;
        currentOpacity = opacity;

        setLayout(new BorderLayout());
        setToolTipText(name);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        setOpaque(false);

        // read the image
        try {

            BufferedImage bufferedImg = ImageIO.read(Objects.requireNonNull(this.getClass().getResource(iconPath)));


            int height = (bufferedImg.getHeight() * width) / bufferedImg.getWidth();
            Image img = bufferedImg.getScaledInstance(width, height,Image.SCALE_SMOOTH);


            imageIcon = new ImageIcon(img);
            imageLabel = new JLabel(imageIcon);

            add(imageLabel, BorderLayout.CENTER);

        }catch (IOException e){
            System.out.println(e);
        }

        // handle mouse hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                currentOpacity = hoverOpacity;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                 currentOpacity = opacity;
                 repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // draw the whole component with specific opacity.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentOpacity));
        super.paintComponent(g);
    }
}