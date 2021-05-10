import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDateTime;


public class TasksView extends JPanel {
    private TaskList taskList;
    private JPanel actionBar;
    protected VerticalIconButton createAction;

    TasksView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // action bar
        actionBar = new JPanel();
        actionBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionBar.setBackground(Color.WHITE);
        createAction = new VerticalIconButton("Create", "assets\\add-file.png");
        actionBar.add(createAction);

        // task list
        taskList = new TaskList(10, 10);
        // create 10px margin
        //taskList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportView(taskList);
        add(scrollPane, BorderLayout.CENTER);





        add(actionBar, BorderLayout.NORTH);
    }



    TaskItem addTask(Task task) {
        TaskItem item = new TaskItem(task);
        taskList.add(item);
        taskList.invalidate();
        return item;
    }

    public TaskItem insertTask(Task task){
        TaskItem item = new TaskItem(task);
        taskList.add(item, 0);
        taskList.revalidate();
        return item;
    }

    void removeTask(TaskItem item){
        for(Component c : taskList.getComponents()){
            if(c == item){
                taskList.remove(c);
                taskList.revalidate();
                break;
            }
        }
    }
}

class TaskList extends JPanel implements Scrollable{
    private int hgap;
    private int vgap;

    TaskList(int hgap, int vgap){
        this.hgap = hgap;
        this.vgap = vgap;

        setBackground(Color.WHITE);
        setLayout(new FlowLayout(FlowLayout.CENTER, this.hgap, this.vgap));
    }

    @Override
    public Dimension getPreferredSize() {
        int itemCount = getComponents().length;
        int width = getWidth();
        if(itemCount > 0 && width > 0){
            Dimension firstItem = getComponent(0).getSize();

            int itemWidth = (int) firstItem.getWidth();
            if(itemWidth > 0){
                int itemHeight = (int) firstItem.getHeight();


                int rowItems = width / itemWidth;
                // when the window has become too small,
                // we still treat it as 1 column.
                if(rowItems == 0) rowItems = 1;

                int rowsRequired = itemCount / rowItems;

                if (itemCount % rowItems > 0) {
                    rowsRequired++;
                }

                int heightRequired = (itemHeight + vgap) * rowsRequired;
                return new Dimension(width, heightRequired);
            }

        }

        return super.getPreferredSize();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(1280,720);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}


class TaskItem extends JPanel {

    private Task task;
    private JPanel topBorder;
    private JPanel content;
    private JPanel actionButtonPane;
    private JPanel taskInfoPane;
    private JLabel nameLabel;
    private JLabel timeRequiredLabel;
    private JLabel timeLeftLabel;
    protected IconButton editButton;
    protected IconButton deleteButton;

    TaskItem(Task task) {
        super();
        this.task = task;
        // set layout
        setLayout(new BorderLayout());




        nameLabel = new JLabel();
        timeRequiredLabel = new JLabel();
        LocalDateTime now = LocalDateTime.now();



        timeLeftLabel = new JLabel();



        // top border
        topBorder = new JPanel();
        topBorder.setPreferredSize(new Dimension(0, 5));

        add(topBorder, BorderLayout.NORTH);

        // content pane
        content = new JPanel();

        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));



        // task info
        taskInfoPane = new JPanel();
        taskInfoPane.setOpaque(false);
        taskInfoPane.setLayout(new GridLayout(3,1));

        taskInfoPane.add(nameLabel);
        taskInfoPane.add(timeRequiredLabel);
        taskInfoPane.add(timeLeftLabel);

        actionButtonPane = new JPanel();
        actionButtonPane.setOpaque(false);
        editButton = new IconButton("edit", "assets/edit-list.png", 20);
        actionButtonPane.add(editButton);
        deleteButton = new IconButton("delete", "assets/trash-can.png", 20);
        actionButtonPane.add(deleteButton);

        content.add(taskInfoPane, BorderLayout.CENTER);
        content.add(actionButtonPane, BorderLayout.EAST);


        add(content, BorderLayout.CENTER);
        updateData();

    }

    JPanel getContent(){
        return content;
    }

    public void updateData(){
        // set background based on type
        Color backgroundColor = Color.GRAY;
        switch (task.getTaskType()) {
            case URGENT_IMPORTANT:
                backgroundColor = ThemeColors.urgent_important_bg;
                break;
            case URGENT_UNIMPORTANT:
                backgroundColor = ThemeColors.urgent_unimportant_bg;
                break;
            case NON_URGENT_IMPORTANT:
                backgroundColor = ThemeColors.non_urgent_important_bg;
                break;
            case NON_URGENT_UNIMPORTANT:
                backgroundColor = ThemeColors.non_urgent_unimportant_bg;
                break;
        }
        content.setBackground(backgroundColor);
        topBorder.setBackground(ThemeColors.darken(backgroundColor, 50));


        nameLabel.setText(task.getName());
        timeRequiredLabel.setText(task.getTimeRequired() + " minutes");

        Duration d = Duration.between(LocalDateTime.now(), task.getDueTime());
        timeLeftLabel.setText(formatDuration(d) + " left");

    }

    public Task getTask(){
        return task;
    }

    private String formatDuration(Duration d) {
        long days;
        int hours;
        int minutes;
        long durationSeconds = d.getSeconds();
        StringBuffer output = new StringBuffer();

        days = durationSeconds / 86400;
        if (days > 0)
            if (days == 1)
                output.append(days + " day ");
            else
                output.append(days + " days ");
        durationSeconds %= 86400;

        hours = (int) durationSeconds / 3600;
        if (hours > 0)
            if (hours == 1)
                output.append(hours + " hour ");
            else
                output.append(hours + " hours ");
        durationSeconds %= 3600;

        minutes = (int) durationSeconds / 60;
        if (minutes > 0)
            if (minutes == 1)
                output.append(minutes + " minute");
            else
                output.append(minutes + " minutes");


        if (output.length() == 0)
            return "0 second";
        else
            return output.toString();
    }
}