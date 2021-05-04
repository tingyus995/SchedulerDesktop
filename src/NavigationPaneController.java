import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NavigationPaneController implements Controller{
    private final NavigationPaneView view;
    private final CardLayout cardLayout;
    private final JPanel container;

    NavigationPaneController(JPanel container){
        // create NavigationPane view
        view = new NavigationPaneView();
        // set up the container to use CardLayout.
        this.container = container;
        cardLayout = new CardLayout();
        container.setLayout(cardLayout);
    }

    void addRoute(String name, Controller controller){
        // Create a new NavigationItem for this route.
        NavigationItem navItem = new NavigationItem(name);
        view.addNavigationItem(navItem);

        // Add the view of the controller to the container.
        container.add(controller.getView(), name);

        // handle click events from NavigationItem.
        navItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                view.setActiveItem(navItem);
                cardLayout.show(container, name);
            }
        });
    }

    @Override
    public JPanel getView() {
        return view;
    }
}