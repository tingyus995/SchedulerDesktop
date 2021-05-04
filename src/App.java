import javax.swing.*;
import java.awt.*;

public class App extends JFrame{
    // singleton pattern used to ensure at most one App instance is created.
    private static App instance = null;
    // make the constructor private so that singleton pattern is enforced.
    private App(){
        // set default window size
        setSize(1280, 720);
        // exit the java program when this window is closed.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set look & feel
        setupLookAndFeel();
        // initialize root UI components such as NavigationPane.
        initUI();
        // show the window.
        setVisible(true);
    }

    private void initUI(){
        // use BorderLayout so that we can easily dock the nav pane on the left
        // and have the view container use the rest of the space.
        setLayout(new BorderLayout());
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
    private void setupLookAndFeel(){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
            System.out.println("Nimbus theme is unavailable. Switched to default look and feel.");
        }
    }

    public static App getInstance(){
        if(instance == null){
            instance = new App();
        }
        return instance;
    }

    public static void main(String[] args) {
        App app = App.getInstance();
    }
}