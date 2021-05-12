import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NavigationPaneController implements Controller{
    private final NavigationPaneView view;
    private final CardLayout cardLayout;
    private final JPanel container;
    private int controllerCount;

    NavigationPaneController(JPanel container){
        // create NavigationPane view
        view = new NavigationPaneView();
        // set up the container to use CardLayout.
        this.container = container;
        cardLayout = new CardLayout();
        container.setLayout(cardLayout);

        controllerCount = 0;
    }

    private void setActiveController(NavigationItem navItem, Controller controller){
        view.setActiveItem(navItem);
        // if a controller implements BeforeShowEventListener, we should notify it.
        if(controller instanceof BeforeShowEventListener){
            BeforeShowEventListener listener = (BeforeShowEventListener) controller;
            listener.beforeShow();
        }

        cardLayout.show(container, navItem.getLabelNameText());
    }

    void addRoute(String name, Controller controller){
        ++controllerCount;
        // Create a new NavigationItem for this route.
        NavigationItem navItem = new NavigationItem(name);
        view.addNavigationItem(navItem);

        // Add the view of the controller to the container.
        container.add(controller.getView(), name);

        // handle click events from NavigationItem.
        navItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setActiveController(navItem, controller);
            }
        });
        // auto activate the first route
        if(controllerCount == 1){
            setActiveController(navItem, controller);
        }
    }

    @Override
    public JPanel getView() {
        return view;
    }
}