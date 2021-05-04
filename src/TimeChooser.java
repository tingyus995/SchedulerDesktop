import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;

public class TimeChooser extends JPanel {

    private JPanel content;
    private JComboBox<String> hourCombo;
    private JComboBox<String> minuteCombo;
    private LocalTime begin;
    private LocalTime end;

    TimeChooser(LocalTime begin, LocalTime end){
        super();

        this.begin = begin;
        this.end = end;

        setLayout(new BorderLayout());
        content = new JPanel();
        content.setBackground(ThemeColors.background);

        // hour combo
        hourCombo = new JComboBox<String>();
        populateHourCombo(begin.getHour(), end.getHour());
        content.add(new JLabel("Hour: "));
        content.add(hourCombo);

        // minute combo
        minuteCombo = new JComboBox<String>();
        populateMinuteCombo();
        content.add(new JLabel("Minute: "));
        content.add(minuteCombo);

        // events
        hourCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateMinuteCombo();
            }
        });

        add(content, BorderLayout.CENTER);
    }

    public int getHour(){
        return Integer.parseInt(hourCombo.getSelectedItem().toString());
    }

    public int getMinute(){
        return Integer.parseInt(minuteCombo.getSelectedItem().toString());
    }

    void setHour(int hour){
        for(int i = 0; i < hourCombo.getItemCount(); ++i){
            int h = Integer.parseInt(hourCombo.getItemAt(i));
            if(h == hour){
                hourCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    void setMinute(int minute){
        for(int i = 0; i < minuteCombo.getItemCount(); ++i){
            int m = Integer.parseInt(minuteCombo.getItemAt(i));
            if(m == minute){
                minuteCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    public LocalTime getTime(){
        return LocalTime.of(getHour(), getMinute(), 0);
    }

    void setTime(LocalTime time){
        setHour(time.getHour());
        setMinute(time.getMinute());
    }

    private void populateHourCombo(int begin, int end) {

        hourCombo.removeAllItems();
        String[] hoursArray = new String[end - begin + 1];
        for (int i = 0; i < hoursArray.length; ++i) {
            hoursArray[i] = String.valueOf(i + begin);
        }
        hourCombo.setModel(new DefaultComboBoxModel(hoursArray));
    }

    private void populateMinuteCombo() {

        int hour = getHour();
        boolean isBeginHour = (hour == begin.getHour());
        boolean isEndHour = (hour == end.getHour());

        int beginMinute = -1;
        int endMinute = -1;

        if (isBeginHour && isEndHour) {
            beginMinute = begin.getMinute();
            endMinute = end.getMinute();
        }else if(isBeginHour){
            beginMinute = begin.getMinute();
            endMinute = 59;
        }else if(isEndHour){
            beginMinute = 0;
            endMinute = end.getMinute();
        }else{
            beginMinute = 0;
            endMinute = 59;
        }

        minuteCombo.removeAllItems();
        String[] minutesArray = new String[endMinute - beginMinute + 1];
        for (int i = 0; i < minutesArray.length; ++i) {
            minutesArray[i] = String.valueOf(i + beginMinute);
        }
        minuteCombo.setModel(new DefaultComboBoxModel(minutesArray));
    }

}
