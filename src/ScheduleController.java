import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Random;

public class ScheduleController implements Controller, BeforeShowEventListener{
    private ScheduleView view;
    ScheduleController(){
        view = new ScheduleView();
        Random random = new Random();

        view.getScheduleAction().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                TimeBlockModel.removeAllTasks();
                Utils.scheduleAlgorithm();
                loadData();
//                for(Component c : mContent.getComponents()){
//                    if(c instanceof TimeBlockItem){
//                        TimeBlockItem item = (TimeBlockItem) c;
//                        item.addTask(new Task("Abc" + random.nextInt(50), LocalDateTime.now(), 80, Task.TaskType.NON_URGENT_IMPORTANT));
//                    }
//                }
            }
        });

    }

    private void loadData(){
        // remove TimeBlocks that are already in the view.
        view.removeAllTimeBlocks();
        // get new TimeBlocks from Model
        TimeBlock[] blocks = TimeBlockModel.getAll();
        //TimeBlock[] blocks = Utils.scheduleAlgorithm();

        for(TimeBlock block : blocks){
            view.addTimeBlock(block);
            System.out.println(block.getDate().toString());
        }
    }
    @Override
    public JPanel getView() {
        return view;
    }

    @Override
    public void beforeShow() {
        System.out.println("Before show called!!!");
        // load TimeBlocks and show on the screen.
        loadData();
    }
}
