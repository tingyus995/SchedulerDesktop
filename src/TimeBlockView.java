import javax.swing.*;
import java.awt.*;

public class TimeBlockView extends JPanel {
    JScrollPane scrollPane;
    TimeBlockView(){
        super();
         scrollPane = new JScrollPane();
         // set proper scrolling speed
         scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        WeekScheduleSelector selector = new WeekScheduleSelector();
        scrollPane.setViewportView(selector);
        add(scrollPane, BorderLayout.CENTER);
    }
}
