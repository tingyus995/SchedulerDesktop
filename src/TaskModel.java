import java.nio.file.Paths;
import java.util.ArrayList;

class TaskModel{
    private static final String rootDir = "db/tasks";

    public static Task[] getAll(){
        ArrayList<Task> blocks = Task.getAll(rootDir);
        return blocks.toArray(new Task[0]);
    }

    public static Task getById(String id){
        return Task.load(Paths.get(rootDir, id).toString());
    }
}