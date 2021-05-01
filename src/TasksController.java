import javax.swing.*;
import java.time.LocalDateTime;

public class TasksController implements Controller{
    TasksView view;

    TasksController(){

        view = new TasksView();
        view.addTask(new Task("Do homework", LocalDateTime.now(), 60, Task.TaskType.URGENT_IMPORTANT));
        view.addTask(new Task("Prepare for exam", LocalDateTime.of(2021,5,14,22,38), 120, Task.TaskType.NON_URGENT_IMPORTANT));
        view.addTask(new Task("Learn Blender", LocalDateTime.of(2021,7,14,22,38), 80, Task.TaskType.NON_URGENT_UNIMPORTANT));


    }
    @Override
    public JPanel getView() {
        return view;
    }
}
class Task{

    private String name;
    private LocalDateTime dueTime;
    private TaskType type;

    public int getTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(int timeRequired) {
        this.timeRequired = timeRequired;
    }

    private int timeRequired;

    enum TaskType{
        URGENT_IMPORTANT,
        URGENT_UNIMPORTANT,
        NON_URGENT_IMPORTANT,
        NON_URGENT_UNIMPORTANT,
    }

    Task(String name, LocalDateTime dueTime, int timeRequired, TaskType type){
        this.name = name;
        this.dueTime = dueTime;
        this.timeRequired = timeRequired;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }

    public TaskType getTaskType(){
        return type;
    }
}