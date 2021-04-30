import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class NavigationPaneView extends JPanel {
    private final JPanel header;
    private final JLabel productName;
    private final ArrayList<NavigationItem> navItems;

    NavigationPaneView(){
        super();
        // keep track of NavigationItems added to the pane.
        navItems = new ArrayList<NavigationItem>();
        // use BoxLayout to easily stack NavigationItem vertically.
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // set background color.
        setBackground(ThemeColors.accent);
        // set the width of the pane.
        setPreferredSize(new Dimension(180, Integer.MAX_VALUE));

        // create header
        header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(ThemeColors.accent);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        add(header);
        // create product name label
        Font productNameFont = new Font(Font.SANS_SERIF, Font.BOLD, 30);
        productName = new JLabel("Scheduler", SwingConstants.CENTER);
        productName.setFont(productNameFont);
        productName.setForeground(Color.white);
        header.add(productName, BorderLayout.CENTER);

    }

    public void setActiveItem(NavigationItem nav_item){
        for(NavigationItem item : navItems){
            item.setActive((nav_item == item));
        }
    }

    void addNavigationItem(NavigationItem item){
        navItems.add(item);
        add(item);
    }
}