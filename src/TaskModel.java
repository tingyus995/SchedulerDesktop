import java.nio.file.Paths;
import java.util.ArrayList;

class TaskModel{
    private static final String rootDir = "db/tasks";

    public static Task[] getAll(){
        ArrayList<Task> Tasks = Task.getAll(rootDir); //anyway, this will get me all the tasks
        return Tasks.toArray(new Task[0]); // 0 is the size
    }

    public static Task getById(String id){
        return Task.load(Paths.get(rootDir, id).toString());
    }
}