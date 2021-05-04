import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ToggleButton extends JPanel{
    private boolean state;

    ToggleButton(){
        state = false;
        setMinimumSize(new Dimension(50, 30));
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(ThemeColors.accent));
    }
    
    void setState(boolean state){
        if(state){
            setBackground(ThemeColors.accent);
        }else{
            setBackground(Color.white);
        }
        this.state = state;
    }
    
    boolean getState(){
        return state;
    }
    
    void toggle(){
        setState(!state);
    }
}

public class WeekScheduleSelector extends JPanel {
    private String[] days;
    private int startTime;
    private int endTime;
    private int delta;


    WeekScheduleSelector(){
        // initialize member variables
        days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        startTime = 0;
        endTime = 1440;
        delta = 15;
        // this should also be the number of rows required since we should add one row for the header and minus one due
        // to "tree-planting problem"
        int slots = (endTime - startTime) / delta;

        setLayout(new GridLayout(slots, 8, 3, 3));
        setBackground(Color.WHITE);
        int row;
        int col;
        int day;
        int currentTime = startTime;
        for(row = 0; row < slots; ++row){

            for(col = 0; col < 8; ++col){
                if(row == 0){
                    // top row
                    if(col == 0){
                        add(new JButton("Time"));
                    }else {
                        day = col - 1;
                        add(new JButton(days[day]));
                    }
                }else{
                    if(col == 0){
                        add(new JButton(toTime(currentTime) + "~" + toTime(currentTime + delta - 1)));
                    }else{
                        ToggleButton btn = new ToggleButton();
                        btn.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseReleased(MouseEvent e) {
                                super.mouseClicked(e);
                                btn.setState(!btn.getState());
                            }

                            @Override
                            public void mouseEntered(MouseEvent e) {
                                //System.out.println("Mouse entered!");

                                if(SwingUtilities.isLeftMouseButton(e)){
                                    btn.setState(!btn.getState());
                                }
                            }
                        });
                        add(btn);
                    }
                }
            }
            currentTime += delta;
        }
    }

    private String toTime(int minutes){
        int hours;
        int seconds;
        hours = minutes / 60;
        seconds = minutes % 60;

        return String.format("%02d:%02d", hours, seconds);
    }
}
