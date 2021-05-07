import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;

class ToggleButton extends JPanel {
    private boolean state;

    ToggleButton() {
        state = false;
        setMinimumSize(new Dimension(50, 30));
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(ThemeColors.accent, 2));
    }

    void setState(boolean state) {
        if (state) {
            setBackground(ThemeColors.accent);
        } else {
            setBackground(Color.white);
        }
        this.state = state;
    }

    boolean getState() {
        return state;
    }

    void toggle() {
        setState(!state);
    }
}

class TimeSlotToggleButton extends ToggleButton {
    private TimeSlot block;
    // private JLabel label;

    TimeSlotToggleButton(TimeSlot slot) {
        super();
        setLayout(new BorderLayout());
        block = slot;
        // label = new JLabel(slot.getBeginDateTime().toString());
        // add(label, BorderLayout.CENTER);
    }

    public void setSlot(TimeSlot slot){
        block = slot;
        // label.setText(slot.getBeginDateTime().toString());
    }

    TimeSlot getSlot() {
        return block;
    }
}

interface ScheduleChangedListener{
    void scheduleChanged();
}

public class WeekScheduleSelector extends JPanel {
    private String[] days;
    private int startTime;
    private int endTime;
    private int delta;
    private boolean dragSelect;
    private Week mWeek;
    private ArrayList<ArrayList<TimeSlotToggleButton>> toggleButtons;
    private ArrayList<ScheduleChangedListener> scheduleChangedListeners;


    WeekScheduleSelector(Week week) {
        // initialize member variables
        days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        startTime = 0;
        endTime = 1440;
        delta = 15;
        dragSelect = true;
        mWeek = week;
        toggleButtons = new ArrayList<>(7);
        for (int i = 0; i < 7; ++i) {
            toggleButtons.add(new ArrayList<>());
        }
        scheduleChangedListeners = new ArrayList<>();


        // this should also be the number of rows required since we should add one row for the header and minus one due
        // to "tree-planting problem"
        int slots = (endTime - startTime) / delta;

        setLayout(new GridLayout(slots, 8, 3, 3));
        setBackground(Color.WHITE);
        int row;
        int col;
        int day;
        int currentTime = startTime;
        for (row = 0; row < slots; ++row) {

            for (col = 0; col < 8; ++col) {
                day = col - 1;
                if (row == 0) {
                    // top row
                    if (col == 0) {
                        add(new JButton("Time"));
                    } else {
                        add(new JButton(days[day]));
                    }
                } else {
                    if (col == 0) {
                        add(new JButton(toTime(currentTime) + "~" + toTime(currentTime + delta)));
                    } else {
                        TimeSlot slot = new TimeSlot(mWeek.getStartDay().plusDays(day), minutesToLocalTime(currentTime), minutesToLocalTime(currentTime + delta));
                        TimeSlotToggleButton btn = new TimeSlotToggleButton(slot);
                        toggleButtons.get(day).add(btn);
                        btn.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                super.mouseClicked(e);
                                btn.setState(!btn.getState());
                                notifyScheduleChanged();
                            }

                            @Override
                            public void mouseEntered(MouseEvent e) {
                                if (dragSelect && SwingUtilities.isLeftMouseButton(e)) {
                                    btn.setState(!btn.getState());
                                    notifyScheduleChanged();
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

    public Week getWeek(){
        return mWeek;
    }

    public void setWeek(Week week){
        Period period = Period.between(mWeek.getStartDay(), week.getStartDay());
        long delta = period.getDays();

        for (Component c : getComponents()) {
            if (c instanceof TimeSlotToggleButton) {
                TimeSlotToggleButton btn = (TimeSlotToggleButton) c;
                TimeSlot oldSlot = btn.getSlot();
                //System.out.println("old: " + oldSlot.getBeginDateTime());
                TimeSlot newSlot = new TimeSlot(oldSlot.getDate().plusDays(delta), oldSlot.getBegin(), oldSlot.getEnd());
                //System.out.println("new: " + newSlot.getBeginDateTime());
                btn.setSlot(newSlot);
                btn.setState(false);
            }
        }
        mWeek = week;
    }

    TimeSlot[] getTimeSlots() {
        ArrayList<TimeSlot> blocks = new ArrayList<>();

        for (Component c : getComponents()) {
            if (c instanceof TimeSlotToggleButton) {
                TimeSlotToggleButton btn = (TimeSlotToggleButton) c;
                if (btn.getState()) {
                    blocks.add(btn.getSlot());
                }
            }
        }

        return blocks.toArray(new TimeSlot[0]);
    }

    TimeBlock[] getTimeBlocks() {
        int day;
        int time;
        int timeSize = toggleButtons.get(0).size();

        ArrayList<TimeBlock> blocks = new ArrayList<>();

        for (day = 0; day < 7; ++day) {
            TimeBlock last = null;
            for (time = 0; time < timeSize; ++time) {
                TimeSlotToggleButton btn = toggleButtons.get(day).get(time);
                if (btn.getState()) {
                    TimeSlot slot = btn.getSlot();
                    if (last != null) {

                        if (last.getEnd().compareTo(slot.getBegin()) == 0) {
                            last.setEnd(slot.getEnd());

                            continue;
                        } else {
                            blocks.add(last);
                        }
                    }
                    last = new TimeBlock(slot.getDate(), slot.getBegin(), slot.getEnd());
                }
            }
            if (last != null) {
                blocks.add(last);
            }
        }
        return blocks.toArray(new TimeBlock[0]);
    }

    public void loadTimeBlocks(TimeBlock[] blocks){
        int day;
        int time;
        int timeSize = toggleButtons.get(0).size();

        for (day = 0; day < 7; ++day) {
            for (time = 0; time < timeSize; ++time) {
                TimeSlotToggleButton btn = toggleButtons.get(day).get(time);
                TimeSlot slot = btn.getSlot();
                for(TimeBlock block : blocks){
                    if(block.cover(slot.getBeginDateTime(), slot.getEndDateTime())){
                        btn.setState(true);
                    }
                }
            }
        }
    }

    private LocalTime minutesToLocalTime(int minutes) {
        if (minutes == 1440) minutes -= 1;
        int h = minutes / 60;
        int m = minutes % 60;
        return LocalTime.of(h, m);
    }

    private String toTime(int minutes) {
        int hours;
        int seconds;
        hours = minutes / 60;
        seconds = minutes % 60;

        return String.format("%02d:%02d", hours, seconds);
    }

    public void setDragSelect(boolean enable) {
        dragSelect = enable;
    }

    public void addListener(ScheduleChangedListener listener){
        scheduleChangedListeners.add(listener);
    }

    private void notifyScheduleChanged(){
        for(ScheduleChangedListener listener : scheduleChangedListeners){
            listener.scheduleChanged();
        }
    }
}
