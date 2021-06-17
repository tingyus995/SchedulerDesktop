import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;

import static java.time.temporal.ChronoUnit.MINUTES;



class Utils {
    protected static LocalDate showDateChooserDialog(LocalDate start, LocalDate end) {
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

    protected static Task showTaskEditDialog(@Nullable Task task) {
        boolean isNewTask = false;
        if (task == null) {
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

    public static Component findParent(Component current, Class type) {

        while (!(type.isInstance(current))) {
            current = current.getParent();
        }
        return current;
    }



    public static void sort(ArrayList<Task> tasks, int length) {
        //System.out.println(length);
        for (int i = 0; i < length - 1 ; i++) {
            for (int j = 0; j < length - i - 1; j++) {
                if (tasks.get(j + 1).getTimeRequired() > tasks.get(j).getTimeRequired()) {
                    //System.out.println(tasks.get(j + 1).getName());
                    //System.out.println(tasks.get(j).getName());
                    Collections.swap(tasks, j+1, j);
                }
            }
        }
        //System.out.println(tasks.get(1).getName());
    }

    public static void swapBlock(TimeBlock[] blocks,int i,int j){
        TimeBlock temp;

        temp = blocks[i];
        blocks[i] = blocks[j];
        blocks[j] = temp;
    }
    public static void sort(TimeBlock[] blocks, int length){
        for (int i = 0; i < length - 1 ; i++) {
            for (int j = 0; j < length - i - 1; j++) {
                if (blocks[j + i].getDate().isBefore(blocks[j].getDate())) {
                    swapBlock(blocks, j + 1, j);
                    continue;
                }
                // the dates are equal, or the date of next block isAfter the date of the previous block
                if (blocks[j + i].getDate().isEqual(blocks[j].getDate())) {
                    if (blocks[j + 1].getBegin().isBefore(blocks[j].getBegin())) {
                        swapBlock(blocks, j + 1, j);
                    }
                }
            }
        }
    }



    public static SchedulingBlock[] checkAdd(ArrayList<Task> tasks, SchedulingBlock[] sbs) {

        //first I put 180 minute task in first block, and then I put 120 minutes task
        // maybe the block in the outer loop, and task in the inner loop.

        //boolean firstTask = true; // first task of a block.
        // the finishingTime of the previous task, initialize to null.

        SchedulingTask[] sts = new SchedulingTask[tasks.size()];
        int i = 0;
        for (Task task : tasks){
            SchedulingTask st = new SchedulingTask(task);
            sts[i] = st;
            i++;
        }

        for (SchedulingTask st : sts) {
            // build this in SchedulingBlock extends Time block: LocalTime preEnd = null;

            //build this in SchedulingBlock extends Time block: boolean firstTask = true;
            System.out.println("The name of the task picked this time is " + st.getTask().getName());

            for (SchedulingBlock sb : sbs){



                /*System.out.println("The boolean of firstTask at the beginning is " + block.getFirstTask());
                /*approach 1:
                check the st*/
                System.out.println("The due date of the task is " + st.getDueDate());
                System.out.println("The date of the block is " + sb.getTimeBlock().getDate());
                if (st.getDueDate().isBefore(sb.getTimeBlock().getDate())) {
                    /* if the due date is before the date of timeblock,
                    then don't put task in this block*/
                    continue;
                } else {
                    /*now the due date is after or equal to the date of timeblock.*/
                    /*if it is the first task of the block, take block.begin as its beginning :*/
                    if(sb.getFirstTask()) // sb.getFirstTask always return true, why?
                        st.setBeginningTime(sb.getTimeBlock().getBegin());
                    else {
                        System.out.println("The end of the prev task is " + sb.getPreEnd());
                        st.setBeginningTime(sb.getPreEnd());
                    }
                    st.setFinishingTime(st.getBeginningTime().plusMinutes(st.getTask().getTimeRequired()));
                    System.out.println("The finishingTime of the task after setting is " + st.getFinishingTime());
                    System.out.println("The due time of the task is " + st.getDueTime());

                    if(st.getDueDate().isEqual(sb.getTimeBlock().getDate())) {
                        if (st.getFinishingTime().isAfter(st.getDueTime())) { //check the due time
                            System.out.println("Continue!");
                            continue;
                        }
                    }

                    System.out.println("MINUTES between the beginning of the task and the end of the block " + MINUTES.between(st.getBeginningTime(), sb.getTimeBlock().getEnd()));
                    if (MINUTES.between(st.getBeginningTime(), sb.getTimeBlock().getEnd()) >= st.getTask().getTimeRequired()) {
                        sb.getTimeBlock().addTask(st.getTask());
                        //System.out.println(tasks.get(0));
                        sb.setFirstTask(false);
                        sb.setPreEnd(st.getFinishingTime());
                        sb.getTimeBlock().save(); //
                        /*Don't call sb.save()
                        In the end, you need to convert SchedulingBlock back to TimeBlock
                        And save TimeBlock instead*/

                        //System.out.println(tasks.get(3));
                        System.out.println("after block.setPreEnd(), now the sb.getPreEnd() is " + sb.getPreEnd());

                        break;
                        //not good :block.setBegin(st.getFinishingTime()); //the end of one task is the beginning of another task
                        // if the task has been added to a block, then don't need to test this task on other blocks
                    } else {
                        continue;
                    }
                }
            }
            System.out.println("/*------------------------------------------------------------------------------*/");
        }
        return sbs;
    }

    public static TimeBlock[] scheduleAlgorithm() {
        TimeBlock[] blocks = TimeBlockModel.getAll(); // will not come out in order, still need to sort
        sort(blocks, blocks.length);
        System.out.println("The date of the first block is " + blocks[0].getDate());
        System.out.println("The date of the second block is " + blocks[1].getDate());
        System.out.println("The date of the third block is " + blocks[2].getDate());

        Task[] tasks = TaskModel.getAll();

        /*------*/
        ArrayList<Task> red = new ArrayList<>();
        ArrayList<Task> orange = new ArrayList<>();
        ArrayList<Task> green = new ArrayList<>();
        ArrayList<Task> blue = new ArrayList<>();


        for (Task task : tasks) {
            if (task.getTaskType() == Task.TaskType.URGENT_IMPORTANT)
                red.add(task);
            if (task.getTaskType() == Task.TaskType.URGENT_UNIMPORTANT)
                orange.add(task);
            if (task.getTaskType() == Task.TaskType.NON_URGENT_IMPORTANT)
                green.add(task);
            if (task.getTaskType() == Task.TaskType.NON_URGENT_UNIMPORTANT)
                blue.add(task);

        }
        sort(red, red.size());
        sort(orange, orange.size());
        sort(green, green.size());
        sort(blue, blue.size());

        /*create schedulingBlocks for blocks.*/
        System.out.println("The length of blocks is " + blocks.length);
        SchedulingBlock[] sbs = new SchedulingBlock[blocks.length];

        int i = 0;
        for(TimeBlock block : blocks){
            SchedulingBlock sb = new SchedulingBlock(block);
            sbs[i] = sb;
            i++;
        }

        SchedulingBlock[] rSBs,roSBs,rogSBs,rogbSBs;
        rSBs = checkAdd(red,sbs);
        roSBs = checkAdd(orange,rSBs);
        rogSBs = checkAdd(green,roSBs);
        rogbSBs = checkAdd(blue,rogSBs);

        /*this can be a method instead of writing it in for loop*/
        //ArrayList<Task> tasks
                /*----*/
            /*for (Task task : tasks) {
                blocks[0].addTask(task);
            }

            blocks[0].save();*/
        i = 0;
        for(SchedulingBlock sb : rogbSBs){
            blocks[i] = sb.getTimeBlock();
            i++;
        }
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