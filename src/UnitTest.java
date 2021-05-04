import java.time.LocalDateTime;
import java.util.ArrayList;

public class UnitTest {
    boolean testTaskSchema(){
        String task_name = "Do homework";
        LocalDateTime due_time = LocalDateTime.now();
        int timeRequired = 60;
        Task.TaskType taskType = Task.TaskType.NON_URGENT_UNIMPORTANT;
        Task t = new Task(task_name, due_time, timeRequired, taskType);
        t.save();
        Task t2 = Schema.load(t.getFilePath());
        return (t2.getName().equals(task_name)) &&
                (t2.getTimeRequired() == timeRequired) &&
                (t2.getTaskType() == taskType);
    }


    public static void main(String[] args) {
        UnitTest test = new UnitTest();
        if(test.testTaskSchema()){
            System.out.println("Passed schema test.");
        }

    }
}
