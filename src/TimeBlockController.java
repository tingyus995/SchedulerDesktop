import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TimeBlockController implements Controller{

    TimeBlockView view;

    TimeBlockController(){
        view = new TimeBlockView();

        TimeBlock[] blocks = TimeBlockModel.getWeekTimeBlocks(view.selector.getWeek());
        for(TimeBlock block: blocks){
            System.out.println("begin: " + block.getBegin() + " end: " + block.getEnd());
        }
        view.selector.loadTimeBlocks(blocks);
        view.selector.addListener(new ScheduleChangedListener() {
            @Override
            public void scheduleChanged() {

                TimeBlock[] oldBlocks = TimeBlockModel.getWeekTimeBlocks(view.selector.getWeek());
                for(TimeBlock block: oldBlocks){
                    block.delete();
                }
                TimeBlock[] blocks = view.selector.getTimeBlocks();
                for(TimeBlock block : blocks){
                    block.save();
                }
            }
        });

        view.weekCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Week selectedWeek = (Week) view.weekCombo.getSelectedItem();
                if(selectedWeek != null){
                    TimeBlock[] blocks = TimeBlockModel.getWeekTimeBlocks(selectedWeek);
                    view.selector.setWeek(selectedWeek);
                    view.selector.loadTimeBlocks(blocks);
                }
            }
        });
    }
    @Override
    public JPanel getView() {
        return view;
    }
}
