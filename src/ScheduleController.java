import javax.swing.*;

public class ScheduleController implements Controller{
    private ScheduleView view;
    ScheduleController(){
        view = new ScheduleView();
    }
    @Override
    public JPanel getView() {
        return view;
    }
}
