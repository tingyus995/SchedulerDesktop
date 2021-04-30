import javax.swing.*;

public class TasksController implements Controller{
    TasksView view;

    TasksController(){
        view = new TasksView();
    }
    @Override
    public JPanel getView() {
        return view;
    }
}
