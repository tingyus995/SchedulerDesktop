import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

class Utils{
    protected static LocalDate showDateChooserDialog(LocalDate start, LocalDate end){
        // container
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        // date chooser
        DateChooser dateChooser = new DateChooser(start, end);
        container.add(dateChooser, BorderLayout.CENTER);

        // OK button
        JButton btnOk = new JButton("OK");
        container.add(btnOk, BorderLayout.SOUTH);


        // dialog
        JDialog dialog = new JDialog(App.getInstance(), "Choose Date", true);

        dialog.setSize(new Dimension(300, 350));

        dialog.getContentPane().add(container);
        // event
        btnOk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // close the dialog when OK button is clicked.
                dialog.dispose();
            }
        });

        // show dialog
        dialog.setVisible(true);

        return dateChooser.getSelectedDate();
    }

    protected static Task showTaskEditDialog(@Nullable Task task){
        boolean isNewTask = false;
        if(task == null){
            isNewTask = true;
            task = new Task(
                    "",
                    LocalDateTime.now().plusDays(3),
                    60,
                    Task.TaskType.URGENT_IMPORTANT
            );
        }

        // container
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        // TaskEditor

        TaskEditController taskEditController = new TaskEditController(task);
        container.add(taskEditController.getView(), BorderLayout.CENTER);

        // OK button
        JButton btnOk = new JButton("OK");
        container.add(btnOk, BorderLayout.SOUTH);

        // dialog
        JDialog dialog = new JDialog(App.getInstance(), (isNewTask ? "Add" : "Edit") + " Task", true);
        dialog.setSize(new Dimension(480, 640));
        dialog.getContentPane().add(container);

        // event
        btnOk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // close the dialog when OK button is clicked.
                taskEditController.saveTask();
                dialog.dispose();
            }
        });

        // show dialog
        dialog.setVisible(true);
        return task;
    }

    public static int dayToColumnNumber(DayOfWeek day) {
        switch (day) {
            case SUNDAY:
                return 0;
            case MONDAY:
                return 1;
            case TUESDAY:
                return 2;
            case WEDNESDAY:
                return 3;
            case THURSDAY:
                return 4;
            case FRIDAY:
                return 5;
            case SATURDAY:
                return 6;
        }
        return 0;
    }

    public static Component findParent(Component current, Class type){

        while(!(type.isInstance(current))){
            current = current.getParent();
        }
        return current;
    }

    public static TimeBlock[] scheduleAlgorithm(){
        TimeBlock[] blocks = TimeBlockModel.getAll();
        Task[] tasks = TaskModel.getAll();

        for(Task task : tasks){
            blocks[0].addTask(task);
        }

        blocks[0].save();
        return blocks;
    }
}

class Spacer extends JPanel {
    // a class that simply squeeze other components to remove spaces between components.
    Spacer(){
        super();
        setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }
}