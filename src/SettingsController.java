import javax.swing.*;

public class SettingsController implements Controller{
    SettingsView view;
    SettingsController(){
        view = new SettingsView();
    }
    @Override
    public JPanel getView() {
        return view;
    }
}
