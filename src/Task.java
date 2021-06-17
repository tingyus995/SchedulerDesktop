import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class Task extends Schema{

    private String name;
    private LocalDateTime dueDateTime;
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

    Task(String name, LocalDateTime dueDateTime, int timeRequired, TaskType type){
        rootDir = "db/tasks";
        this.name = name;
        this.dueDateTime = dueDateTime;
        this.timeRequired = timeRequired;
        this.type = type;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public TaskType getTaskType(){
        return type;
    }
    public void setTaskType(TaskType taskType){
        this.type = taskType;
    }
}

/*If I needed those two attributes,
I would simply define a class extend Task,
with a constructor accepting a Task object.*/

class SchedulingTask{
    private LocalDate dueDate;
    private LocalTime dueTime;
    private LocalTime beginningTime;
    private LocalTime finishingTime;
    private Task task;

    public Task getTask(){
        return task;
    }

    SchedulingTask(Task task){
        this.task = task;
        this.dueDate = task.getDueDateTime().toLocalDate();
        this.dueTime = task.getDueDateTime().toLocalTime();
        /*
        * set beginningTime and finishingTime in Utils.java
        * (so there is block.begin which can be assigned to .beginningTime,
        * and therefore can calculate .finishingTime)
        * */
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalTime dueTime) {
        this.dueTime = dueTime;
    }

    public LocalTime getBeginningTime() {
        return beginningTime;
    }

    public void setBeginningTime(LocalTime beginningTime) {
        this.beginningTime = beginningTime;
    }

    public LocalTime getFinishingTime() {
        return finishingTime;
    }

    public void setFinishingTime(LocalTime finishingTime) {
        this.finishingTime = finishingTime;
    }
}