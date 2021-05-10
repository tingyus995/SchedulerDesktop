import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
