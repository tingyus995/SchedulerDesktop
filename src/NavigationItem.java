import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class NavigationItem extends JPanel{
    private final int ITEM_HEIGHT = 50;

    JLabel labelName;
    JPanel activeIndicator;
    private boolean isActive;

    NavigationItem(String name){
        super();
        // use border layout
        setLayout(new BorderLayout());
        // limit the height of the item
        setMaximumSize(new Dimension(Integer.MAX_VALUE, ITEM_HEIGHT));
        // active indicator
        activeIndicator = new JPanel();
        add(activeIndicator, BorderLayout.WEST);
        // label
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 15);
        labelName = new JLabel(name);
        labelName.setFont(labelFont);
        labelName.setForeground(Color.white);
        labelName.setVisible(true);
        // add left padding to the label
        labelName.setBorder(BorderFactory.createEmptyBorder(0,10,0, 0));
        add(labelName, BorderLayout.CENTER);

        setActive(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(!isActive) {
                    setBackground(ThemeColors.accent_light);
                    activeIndicator.setBackground(ThemeColors.accent_light);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!isActive) {
                    setBackground(ThemeColors.accent);
                    activeIndicator.setBackground(ThemeColors.accent);
                }
            }
        });
    }
    void setActive(boolean active){
        isActive = active;
        if(isActive){
            setBackground(ThemeColors.accent_light);
            activeIndicator.setBackground(Color.white);
        }else{
            setBackground(ThemeColors.accent);
            activeIndicator.setBackground(ThemeColors.accent);
        }
    }

    public String getLabelNameText(){
        return labelName.getText();
    }
}