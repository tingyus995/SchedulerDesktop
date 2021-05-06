import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TimeBlockView extends JPanel {
    JScrollPane scrollPane;
    TimeBlockView(){
        super();
         scrollPane = new JScrollPane();
         // set proper scrolling speed
         scrollPane.getVerticalScrollBar().setUnitIncrement(16);
         // clear the border
         scrollPane.setBorder(BorderFactory.createEmptyBorder());


        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        WeekScheduleSelector selector = new WeekScheduleSelector();
        selector.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
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
