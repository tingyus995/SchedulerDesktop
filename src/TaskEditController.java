import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskEditController implements Controller{
    TaskEditView view;
    Task task;
    LocalDate date;

    TaskEditController(Task task){
        this.task = task;
        view = new TaskEditView();
        loadTaskToView();
        view.dateChooseBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                LocalDate date = Utils.showDateChooserDialog(LocalDate.now(), LocalDate.of(3000,1,1));
                setDate(date);
            }
        });
    }
    private void setDate(LocalDate date){
        view.dateChooseBtn.setText(date.toString() + " (click to change)");
        this.date = date;
    }
    private void loadTaskToView(){
        view.nameField.setText(task.getName());
        view.taskTypeSelector.setType(task.getTaskType());
        view.timeRequiredSpinner.setValue(task.getTimeRequired());
        setDate(task.getDueTime().toLocalDate());
        view.timeChooser.setTime(task.getDueTime().toLocalTime());
    }

    public Task saveTask(){
        task.setName(view.nameField.getText());
        task.setTaskType(view.taskTypeSelector.getType());
        task.setTimeRequired((int) view.timeRequiredSpinner.getValue());
        task.setDueTime(LocalDateTime.of(date, view.timeChooser.getTime()));
        task.save();

        return task;
    }

    @Override
    public JPanel getView() {
        return view;
    }
}
