import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class TimeBlockView extends JPanel {
    JPanel controlPane;
    JScrollPane scrollPane;
    JComboBox<Week> weekCombo;
    protected WeekScheduleSelector selector;

    TimeBlockView() {
        super();


        scrollPane = new JScrollPane();
        // set proper scrolling speed
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        // clear the border
        scrollPane.setBorder(BorderFactory.createEmptyBorder());


        setLayout(new BorderLayout());
        setBackground(Color.WHITE);


        controlPane = new JPanel();

        Week[] weeks = new Week[]{
                new Week(LocalDate.now()),
                new Week(LocalDate.now().plusDays(7)),
                new Week(LocalDate.now().plusDays(14)),
        };
        weekCombo = new JComboBox<Week>(weeks);

        controlPane.add(weekCombo);
        add(controlPane, BorderLayout.NORTH);

        Week week = new Week(LocalDate.now());
        selector = new WeekScheduleSelector(week);

        selector.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scrollPane.setViewportView(selector);
        add(scrollPane, BorderLayout.CENTER);

        scrollPane.getVerticalScrollBar().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selector.setDragSelect(false);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selector.setDragSelect(true);
            }
        });
    }
}

class Week {
    private LocalDate start;

    Week(LocalDate day) {
        int deltaToSunday = Utils.dayToColumnNumber(day.getDayOfWeek());
        start = day.minusDays(deltaToSunday);
    }

    @Override
    public String toString() {
        LocalDate end = start.plusDays(6);
        return start.toString() + " to " + end.toString();
    }

    public boolean isInWeek(LocalDate date){
        return start.compareTo(date) <= 0 && start.plusDays(6).compareTo(date) >= 0;
    }

    public LocalDate getStartDay(){
        return start;
    }
}

class TimeBlock extends Schema {
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private ArrayList<String> taskIds;

    TimeBlock(LocalDate date, LocalTime begin, LocalTime end){
        int cmp = begin.compareTo(end);
        if(cmp == 0){
            throw new InvalidParameterException("begin and end time should not be the same.");
        }

        if(cmp > 0){
            throw new InvalidParameterException("begin should not be later than end.");
        }
        rootDir = "db/blocks";
        this.date = date;
        this.begin = begin;
        this.end = end;
        taskIds = new ArrayList<>();
    }

    public void addTask(Task t){
        taskIds.add(t.getID());
    }

    public void addTask(Task t, int index){
        taskIds.add(index, t.getID());
    }

    public void removeTask(Task t){
        taskIds.remove(t.getID());
    }

    public Task[] getTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        for(String id : taskIds){
            Task task = TaskModel.getById(id);
            tasks.add(task);
        }
        return tasks.toArray(new Task[0]);
    }

    public void clearTasks(){
        taskIds.clear();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getBegin() {
        return begin;
    }

    public boolean setBegin(LocalTime begin) {
        if(begin.compareTo(end) >= 0) return false;
        this.begin = begin;
        return true;
    }

    public LocalTime getEnd() {
        return end;
    }

    public boolean setEnd(LocalTime end) {
        if(end.compareTo(begin) <= 0) return false;
        this.end = end;
        return true;
    }

    boolean cover(LocalDateTime beginDateTime, LocalDateTime endDateTime){
        // Note: We assume that our TimeBlock will never span across days.
        if(!beginDateTime.toLocalDate().isEqual(date)) return false;
        if(!endDateTime.toLocalDate().isEqual(date)) return false;

        // check if time is within the range
        LocalTime beginTime = beginDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();

        return begin.compareTo(beginTime) <= 0 && end.compareTo(endTime) >= 0;
    }
}

class TimeSlot {
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;

    TimeSlot(LocalDate date, LocalTime begin, LocalTime end){
        int cmp = begin.compareTo(end);
        if(cmp == 0){
            throw new InvalidParameterException("begin and end time should not be the same.");
        }

        if(cmp > 0){
            throw new InvalidParameterException("begin should not be later than end.");
        }

        this.date = date;
        this.begin = begin;
        this.end = end;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getBegin() {
        return begin;
    }

    public LocalDateTime getBeginDateTime(){
        return LocalDateTime.of(date, begin);
    }

    public boolean setBegin(LocalTime begin) {
        if(begin.compareTo(end) >= 0) return false;
        this.begin = begin;
        return true;
    }

    public LocalTime getEnd() {
        return end;
    }

    public LocalDateTime getEndDateTime(){
        return LocalDateTime.of(date, end);
    }

    public boolean setEnd(LocalTime end) {
        if(end.compareTo(begin) <= 0) return false;
        this.end = end;
        return true;
    }

    boolean cover(LocalDateTime beginDateTime, LocalDateTime endDateTime){
        // Note: We assume that our TimeBlock will never span across days.
        if(!beginDateTime.toLocalDate().isEqual(date)) return false;
        if(!endDateTime.toLocalDate().isEqual(date)) return false;

        // check if time is within the range
        LocalTime beginTime = beginDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();

        return begin.compareTo(beginTime) <= 0 && end.compareTo(endTime) >= 0;
    }
}