import java.time.LocalDateTime;

class Task extends Schema{

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
        rootDir = "db/tasks";
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
    public void setTaskType(TaskType taskType){
        this.type = taskType;
    }
}