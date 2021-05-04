import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TasksController implements Controller{
    TasksView view;

    TasksController(){

        view = new TasksView();

        ArrayList<Task> tasks = Schema.getAll("db/tasks");

        for(Task t : tasks){
            view.addTask(t);
        }


//        view.addTask(new Task("Do homework", LocalDateTime.now(), 60, Task.TaskType.URGENT_IMPORTANT));
//        view.addTask(new Task("Prepare for exam", LocalDateTime.of(2021,5,14,22,38), 120, Task.TaskType.NON_URGENT_IMPORTANT));
//        view.addTask(new Task("Learn Blender", LocalDateTime.of(2021,7,14,22,38), 80, Task.TaskType.NON_URGENT_UNIMPORTANT));


        view.createAction.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Task t = Utils.showTaskEditDialog(null);

                if(t != null){
                    System.out.println(t.getName());
                    view.insertTask(t);
                }
            }
        });


    }
    @Override
    public JPanel getView() {
        return view;
    }
}
