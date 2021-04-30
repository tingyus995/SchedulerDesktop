import javax.swing.*;
import java.awt.*;

public class App extends JFrame{
    App(){
        // set default window size
        setSize(1280, 720);
        // initialize root UI components such as NavigationPane.
        initUI();
        // show the window.
        setVisible(true);
    }

    private void initUI(){
        // use BorderLayout so that we can easily dock the nav pane on the left
        // and have the view container use the rest of the space.
        setLayout(new BorderLayout());

        // exit the java program when this window is closed.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // a panel used to hosts each view.
        JPanel viewContainer = new JPanel();

        // create NavigationPaneController
        // Note: NavigationPaneController will "put" the selected route's view onto the container
        // with the help of CardLayout.
        NavigationPaneController navController = new NavigationPaneController(viewContainer);

        // add routes
        navController.addRoute("Schedule", new ScheduleController());
        navController.addRoute("Time Block", new TimeBlockController());
        navController.addRoute("Tasks", new TasksController());
        navController.addRoute("Settings", new SettingsController());

        // dock the nav panel to the left.
        add(navController.getView(), BorderLayout.WEST);

        // the container should use the rest of the space.
        add(viewContainer, BorderLayout.CENTER);
    }
    // start the app by create a instance of class App.
    public static void main(String[] args) {
        new App();
    }
}