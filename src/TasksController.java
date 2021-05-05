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
            TaskItem item = view.addTask(t);
            listenTaskItem(item);
        }

        view.createAction.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Task task = Utils.showTaskEditDialog(null);
                TaskItem item = view.insertTask(task);
                listenTaskItem(item);
            }
        });
    }

    private void listenTaskItem(TaskItem item){
        item.editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                handleEditButton(item);

            }
        });

        item.deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                handleDeleteButton(item);
            }
        });
    }

    private void handleDeleteButton(TaskItem item){
        Task task = item.getTask();
        task.delete();
        view.removeTask(item);
    }

    private void handleEditButton(TaskItem item){
        Task task = item.getTask();
        Utils.showTaskEditDialog(task);
        item.updateData();
    }
    @Override
    public JPanel getView() {
        return view;
    }
}
