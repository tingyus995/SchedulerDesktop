import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

public class TasksView extends JPanel {
    private JPanel taskList;

    TasksView() {
        super();
        setLayout(new BorderLayout());
        // create 10px margin
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        taskList = new JPanel();
        taskList.setBackground(Color.WHITE);
        taskList.setLayout(new BoxLayout(taskList, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(taskList);
        add(scrollPane, BorderLayout.CENTER);
    }

    void addTask(Task task) {
        taskList.add(new TaskItem(task));
        // vertical 10px padding
        taskList.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}


class TaskItem extends JPanel {

    private Task task;
    private JPanel topBorder;
    private JPanel content;

    TaskItem(Task task) {
        super();
        // set layout
        setLayout(new BorderLayout());
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


        setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        JLabel nameLabel = new JLabel(task.getName());
        JLabel timeRequiredLabel = new JLabel(task.getTimeRequired() + " minutes");
        LocalDateTime now = LocalDateTime.now();

        Duration d = Duration.between(LocalDateTime.now(), task.getDueTime());

        JLabel timeLeft = new JLabel(formatDuration(d) + " left");

        // top border
        topBorder = new JPanel();
        topBorder.setPreferredSize(new Dimension(0, 5));
        topBorder.setBackground(ThemeColors.darken(backgroundColor, 50));
        add(topBorder, BorderLayout.NORTH);

        // content
        content = new JPanel();
        content.setBackground(backgroundColor);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        content.add(nameLabel);
        content.add(timeRequiredLabel);
        content.add(timeLeft);

        add(content, BorderLayout.CENTER);

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
                output.append(days + " hour ");
            else
                output.append(days + " hours ");
        durationSeconds %= 3600;

        minutes = (int) durationSeconds / 60;
        if (minutes > 0)
            if (minutes == 1)
                output.append(days + " minute");
            else
                output.append(days + " minutes");


        if (output.length() == 0)
            return "0 second";
        else
            return output.toString();
    }
}