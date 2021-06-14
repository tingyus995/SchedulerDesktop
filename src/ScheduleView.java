import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


public class ScheduleView extends JPanel{
    private JPanel mContent;
    private JPanel mActionBar;
    private NotScheduledTaskList notScheduledTaskList;
    private BufferedImage mCursorImg;
    protected VerticalIconButton mScheduleAction;

    ScheduleView(){
        super();
        setLayout(new BorderLayout());
        setBackground(ThemeColors.background);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        // action bar
        mActionBar = new JPanel();
        mActionBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        mActionBar.setBackground(Color.WHITE);
        mScheduleAction = new VerticalIconButton("Schedule", "assets\\daily-schedule.png");
        mActionBar.add(mScheduleAction);

        mContent = new JPanel();
        mContent.setOpaque(false);
        mContent.setLayout(new FlowLayout());
        notScheduledTaskList = new NotScheduledTaskList();
        mContent.add(notScheduledTaskList);



        add(mActionBar, BorderLayout.NORTH);
        add(mContent, BorderLayout.CENTER);





    }

    void addTimeBlock(TimeBlock block){
        mContent.add(new TimeBlockItem(block));
        revalidate();
    }

    void removeAllTimeBlocks(){
        for(Component c : mContent.getComponents()){
            if(c instanceof TimeBlockItem){
                mContent.remove(c);
            }
        }

        mContent.revalidate();
        mContent.repaint();
    }

    void updateNotScheduledTaskList(TimeBlock[] blocks, Task[] tasks){
        notScheduledTaskList.updateData(blocks, tasks);
        mContent.revalidate();
        mContent.repaint();
    }

    void setDragCursorImage(BufferedImage img, DragSource source){
        source.addDragSourceMotionListener(new DragSourceAdapter() {
            @Override
            public void dragMouseMoved(DragSourceDragEvent dsde) {
                repaint();
            }
        });

        source.addDragSourceListener(new DragSourceAdapter() {
            @Override
            public void dragDropEnd(DragSourceDropEvent dsde) {
                mCursorImg = null;
                repaint();
            }
        });


        mCursorImg = img;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if(mCursorImg != null){
            Point loc = getMousePosition();
            g.drawImage(mCursorImg, loc.x, loc.y, null);
        }
    }

    // getters and setters


    public JPanel getContent() {
        return mContent;
    }

    public JPanel getActionBar() {
        return mActionBar;
    }

    public VerticalIconButton getScheduleAction() {
        return mScheduleAction;
    }
}


class ScheduledTaskItem extends TaskItem implements Transferable, DragGestureListener {

    private DragSource dragSource;
    public static final DataFlavor DATA_FLAVOR = new DataFlavor(Task.class, "Task");

    ScheduledTaskItem(Task task) {
        super(task);
        dragSource = new DragSource();
        //dragSource.addDragSourceListener(dragSourceListener);
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
    }

    public void addDragSourceListener(DragSourceListener listener){
        dragSource.addDragSourceListener(listener);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {DATA_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return getTask();
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        System.out.println("Drag gesture recognized!");
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        paint(g2d);
        g2d.dispose();

        Component parent = this;
        while(!(parent instanceof ScheduleView)){
            parent = parent.getParent();
        }
        ScheduleView view = (ScheduleView) parent;



        dge.startDrag(DragSource.DefaultMoveDrop, this);
        view.setDragCursorImage(img, dge.getDragSource());
    }
}

interface TaskMoveListener{
    void onTaskRemoved(Task t);
    void onTaskAdded(Task t, int index);
}

class ScheduledTaskList extends JPanel{
    private DragSourceListener dragSourceListener;
    private DropTargetListener dropTargetListener;
    private DropTarget dropTarget;
    private TaskMoveListener taskMoveListener;
    private int taskCount;
    private Point dropLocation;
    private int targetIndex;

    ScheduledTaskList(){
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        targetIndex = 0;
        taskCount = 0;

        dragSourceListener = new DragSourceAdapter() {
            @Override
            public void dragDropEnd(DragSourceDropEvent event) {
                if(event.getDropSuccess() && event.getDropAction() == DnDConstants.ACTION_MOVE){
                    DragSourceContext context = event.getDragSourceContext();
                    ScheduledTaskItem item = (ScheduledTaskItem) context.getComponent();
                    // remove from this pane
                    remove(item);
                    if(taskMoveListener != null){
                        taskMoveListener.onTaskRemoved(item.getTask());
                    }
                    revalidate();
                }
            }
        };



        dropTargetListener = new DropTargetAdapter() {

            @Override
            public void dragEnter(DropTargetDragEvent dtde) {

            }

            @Override
            public void dragOver(DropTargetDragEvent event) {
                Point newDropLocation = event.getLocation();
                if(dropLocation == null || !(dropLocation.equals(newDropLocation))){
                    dropLocation = newDropLocation;
                    repaint();

                    ScheduleView view = (ScheduleView) Utils.findParent(ScheduledTaskList.this, ScheduleView.class);
                    view.repaint();
                }
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {

            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                dropLocation = null;
            }

            @Override
            public void drop(DropTargetDropEvent event) {
                dropLocation = null;
                Transferable transferable = event.getTransferable();

                try {
                    Task task = (Task) transferable.getTransferData(ScheduledTaskItem.DATA_FLAVOR);
                    addTask(task, targetIndex);
                    if(taskMoveListener != null){
                        taskMoveListener.onTaskAdded(task, targetIndex);
                    }
                    event.acceptDrop(DnDConstants.ACTION_MOVE);
                    event.dropComplete(true);

                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }



            }
        };

        dropTarget = new DropTarget(this, dropTargetListener);


    }

    int findClosetComponent(Point point, Class type){

        int closetIdx = -1;
        double minDist = Integer.MAX_VALUE;
        double dist;

        Component[] components = getComponents();
        for(int i = 0; i < components.length; ++i){
            Component component = components[i];
            if(type.isInstance(component)){
                dist = component.getLocation().distance(point);
                if(dist < minDist){
                    minDist = dist;
                    closetIdx = i;
                }
            }
        }

        return closetIdx;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(dropLocation != null){
            int index = findClosetComponent(dropLocation, ScheduledTaskItem.class);
            Component closet = getComponent(index);
            int threshY = closet.getY() + closet.getHeight() / 2;

            g.setColor(Color.DARK_GRAY);
            if(dropLocation.getY() <= threshY){
                g.fillRect(0, closet.getY(), getWidth(), 5);
                targetIndex = index;
            }else{
                g.fillRect(0, closet.getY() + closet.getHeight(), getWidth(), 5);
                targetIndex = index + 1;
            }
        }
    }

    void addTask(Task t){
        ScheduledTaskItem item = new ScheduledTaskItem(t);
        add(item);
        item.addDragSourceListener(dragSourceListener);
        revalidate();
    }

    void addTask(Task t, int index){
        ScheduledTaskItem item = new ScheduledTaskItem(t);
        add(item, index);
        item.addDragSourceListener(dragSourceListener);
        revalidate();
    }

    public void setTaskMoveListener(TaskMoveListener taskMoveListener) {
        this.taskMoveListener = taskMoveListener;
    }
}


class TimeBlockItem extends JPanel implements DropTargetListener{
    private TimeBlock mTimeBlock;
    private JPanel mContent;
    private JPanel mHeader;
    private JLabel dateLabel;
    private JLabel timeLabel;
    private ScheduledTaskList mTaskList;
    private DropTarget dropTarget;
    private  boolean hoverByTask;

    TimeBlockItem(TimeBlock timeBlock){
        super();
        mTimeBlock = timeBlock;
        dropTarget = new DropTarget(this, DnDConstants.ACTION_MOVE, this);


        hoverByTask = false;


        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setOpaque(false);

        // content
        mContent = new JPanel();
        mContent.setLayout(new BorderLayout());
        // <header>
        mHeader = new JPanel();
        // <date>
        dateLabel = new JLabel();
        mHeader.add(dateLabel);
        // </date>
        // <time>
        timeLabel = new JLabel();
        mHeader.add(timeLabel);
        // </time>
        mContent.add(mHeader, BorderLayout.NORTH);
        // </header>

        mTaskList = new ScheduledTaskList();
        mTaskList.setTaskMoveListener(new TaskMoveListener(){

            @Override
            public void onTaskRemoved(Task t) {
                System.out.println("Task removed!");
                mTimeBlock.removeTask(t);
                mTimeBlock.save();
            }

            @Override
            public void onTaskAdded(Task t, int index) {
                System.out.println("Task added!");
                mTimeBlock.addTask(t, index);
                mTimeBlock.save();
            }
        });
        mTaskList.setOpaque(false);
        mContent.add(mTaskList, BorderLayout.CENTER);




        VerticalIconButton hint = new VerticalIconButton("Drag task here","assets/collapse-plus.png");
        hint.setOpaque(false);
        mContent.add(hint, BorderLayout.SOUTH);


        add(mContent, BorderLayout.CENTER);

        updateData();

    }

    void addTask(Task t){
        mTaskList.addTask(t);
        System.out.println("added!");

    }

    private void setHoverByTask(boolean hover){
        hoverByTask = hover;
        if(hover){
            mContent.setBackground(ThemeColors.time_block_drop);
            mHeader.setBackground(ThemeColors.darken(ThemeColors.time_block_drop, 50));
        }else {
            mContent.setBackground(ThemeColors.time_block_color);
            mHeader.setBackground(ThemeColors.darken(ThemeColors.time_block_color, 50));
        }
    }



    void updateData(){
        dateLabel.setText(mTimeBlock.getDate().toString());
        timeLabel.setText(mTimeBlock.getBegin().toString() + "~" + mTimeBlock.getEnd());
        setHoverByTask(false);

        for(Task task : mTimeBlock.getTasks()){
            addTask(task);
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        setHoverByTask(true);
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        setHoverByTask(false);
    }

    @Override
    public void drop(DropTargetDropEvent event) {

        Transferable transferable = event.getTransferable();
        try {
            Task task = (Task) transferable.getTransferData(ScheduledTaskItem.DATA_FLAVOR);
            addTask(task);
            event.acceptDrop(DnDConstants.ACTION_MOVE);
            event.dropComplete(true);
            setHoverByTask(false);
            mTimeBlock.addTask(task);
            mTimeBlock.save();


        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }
}

class NotScheduledTaskList extends JPanel implements DropTargetListener{
    private JPanel mContent;
    private JPanel mHeader;
    private JLabel dateLabel;

    private ScheduledTaskList mTaskList;
    private DropTarget dropTarget;
    private  boolean hoverByTask;

    NotScheduledTaskList(){
        super();
        dropTarget = new DropTarget(this, DnDConstants.ACTION_MOVE, this);
        hoverByTask = false;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setOpaque(false);

        // content
        mContent = new JPanel();
        mContent.setLayout(new BorderLayout());
        // <header>
        mHeader = new JPanel();
        // <date>
        dateLabel = new JLabel();
        mHeader.add(dateLabel);
        // </date>
        mContent.add(mHeader, BorderLayout.NORTH);
        // </header>

        mTaskList = new ScheduledTaskList();
        mTaskList.setOpaque(false);
        mContent.add(mTaskList, BorderLayout.CENTER);

        VerticalIconButton hint = new VerticalIconButton("Drag task here","assets/collapse-plus.png");
        hint.setOpaque(false);
        mContent.add(hint, BorderLayout.SOUTH);
        add(mContent, BorderLayout.CENTER);
        dateLabel.setText("Not scheduled task");
        setHoverByTask(false);
        //updateData();


    }

    void addTask(Task t){
        mTaskList.addTask(t);
        System.out.println("added!");
    }

    private void setHoverByTask(boolean hover){
        hoverByTask = hover;
        if(hover){
            mContent.setBackground(ThemeColors.time_block_drop);
            mHeader.setBackground(ThemeColors.darken(ThemeColors.time_block_drop, 50));
        }else {
            mContent.setBackground(ThemeColors.time_block_color);
            mHeader.setBackground(ThemeColors.darken(ThemeColors.time_block_color, 50));
        }
    }

    void updateData(TimeBlock[] blocks, Task[] tasks){

        for(Component c : mTaskList.getComponents()){
            if(c instanceof TaskItem){
                mTaskList.remove(c);
            }
        }

        ArrayList<String> scheduledIds = new ArrayList<String>();
        for(TimeBlock block : blocks){
            for(Task task : block.getTasks()){
                scheduledIds.add(task.getID());
            }
        }

        for(Task task : tasks){
            if(!scheduledIds.contains(task.getID())){
                addTask(task);
            }
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        setHoverByTask(true);
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        setHoverByTask(false);
    }

    @Override
    public void drop(DropTargetDropEvent event) {

        Transferable transferable = event.getTransferable();
        try {
            Task task = (Task) transferable.getTransferData(ScheduledTaskItem.DATA_FLAVOR);
            addTask(task);
            event.acceptDrop(DnDConstants.ACTION_MOVE);
            event.dropComplete(true);
            setHoverByTask(false);

        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }
}