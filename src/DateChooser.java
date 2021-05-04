import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

public class DateChooser extends JPanel {
    String[] months;
    String[] days;
    private int selectedDay;
    private LocalDate begin;
    private LocalDate end;
    private JComboBox<String> yearCombo;
    private JComboBox<String> monthCombo;
    private MouseAdapter mouseAdapter;

    private JPanel calendar;

    DateChooser(LocalDate begin, LocalDate end) {
        super();
        months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        days = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        selectedDay = 1;
        this.begin = begin;
        this.end = end;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        // year and month panel
        JPanel comboPanel = new JPanel(new FlowLayout());
        comboPanel.setBackground(ThemeColors.accent);
        // year combo
        comboPanel.add(new JLabel("Year:"));
        yearCombo = new JComboBox();
        populateYearCombo(begin.getYear(), end.getYear());

        comboPanel.add(yearCombo);
        // month combo
        comboPanel.add(new JLabel("Month:"));
        monthCombo = new JComboBox();
        populateMonthCombo();
        comboPanel.add(monthCombo);

        // combo events
        yearCombo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateMonthCombo();
            }
        });

        monthCombo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (monthCombo.getItemCount() > 0) {
                    populateCalendar();
                    selectDay(1);
                }
            }
        });

        // mouse event handler for DayItem
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                for (Component p : calendar.getComponents()) {
                    if (p instanceof DayItem) {
                        DayItem dayItem = (DayItem) p;
                        if (dayItem.getContent() == e.getComponent()) {
                            dayItem.setSelected(true);
                            selectedDay = dayItem.getDay();
                        } else {
                            dayItem.setSelected(false);
                        }
                    }
                }
            }
        };

        add(comboPanel, BorderLayout.NORTH);

        calendar = new JPanel();
        calendar.setBackground(ThemeColors.background);
        populateCalendar();
        selectDay(1);

        add(calendar, BorderLayout.CENTER);
    }

    public int getYear() {
        return Integer.parseInt(yearCombo.getSelectedItem().toString());
    }

    public int getMonth() {
        String monthString = monthCombo.getSelectedItem().toString();
        for (int i = 0; i < months.length; ++i) {
            if (months[i].equals(monthString))
                return i + 1;
        }

        return -1;
    }

    public int getDay() {
        return selectedDay;
    }

    public LocalDate getSelectedDate() {
        return LocalDate.of(getYear(), getMonth(), getDay());
    }

    private void selectDay(int day) {
        // select a specific day of month
        for (Component p : calendar.getComponents()) {
            if (p instanceof DayItem) {
                DayItem dayItem = (DayItem) p;
                if (dayItem.getDay() == day) {
                    dayItem.setSelected(true);
                    selectedDay = day;
                    break;
                }
            }
        }
    }

    private void populateYearCombo(int begin, int end) {
        yearCombo.removeAllItems();
        String[] yearsArray = new String[end - begin + 1];
        for (int i = 0; i < yearsArray.length; ++i) {
            yearsArray[i] = String.valueOf(i + begin);
        }
        yearCombo.setModel(new DefaultComboBoxModel(yearsArray));
    }

    private void populateMonthCombo() {
        int startMonth;
        int endMonth;
        if (Objects.requireNonNull(yearCombo.getSelectedItem()).toString().equals(String.valueOf(begin.getYear()))) {
            startMonth = begin.getMonthValue();
            endMonth = 12;
        } else if (Objects.requireNonNull(yearCombo.getSelectedItem()).toString().equals(String.valueOf(end.getYear()))) {
            startMonth = 1;
            endMonth = end.getMonthValue();
        } else {
            startMonth = 1;
            endMonth = 12;
        }
        monthCombo.removeAllItems();
        for (int i = startMonth; i <= endMonth; ++i) {
            monthCombo.addItem(months[i - 1]);
        }
    }


    private void populateCalendar() {
        calendar.removeAll();

        LocalDate date = LocalDate.of(getYear(), getMonth(), 1);
        DayOfWeek day = date.getDayOfWeek();
        int beginCol = dayToColumnNumber(day);
        int lengthOfMonth = YearMonth.of(date.getYear(), date.getMonth()).lengthOfMonth();

        // estimate the amount of rows required
        int withoutFirstWeek = lengthOfMonth - (7 - beginCol);
        int rowsRequired = (withoutFirstWeek / 7);
        if (withoutFirstWeek % 7 != 0) rowsRequired += 1;
        rowsRequired += 2; // header row & first week

        calendar.setLayout(new GridLayout(rowsRequired, 7));

        // create the calendar
        int row;
        int col;

        boolean started = false;
        boolean finished = false;
        int currentDay = 1;

        for (row = 0; row < rowsRequired; ++row) {
            for (col = 0; col < 7; ++col) {
                if (row == 0) {
                    calendar.add(new LabelItem(days[col]));
                } else {
                    if (col == beginCol) started = true;
                    if (started) {
                        DayItem item = new DayItem(currentDay);
                        item.getContent().addMouseListener(mouseAdapter);

                        calendar.add(item);
                        if (currentDay == lengthOfMonth) {
                            finished = true;
                            break;
                        }
                        currentDay += 1;
                    } else {
                        calendar.add(new DayItem(-1));
                    }
                }
            }
            if (finished) break;
        }
        calendar.repaint();
        calendar.revalidate();
    }

    private int dayToColumnNumber(DayOfWeek day) {
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
}

class LabelItem extends JPanel {
    LabelItem(String str) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel(str, SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}

class DayItem extends JPanel {
    private int day; // day of the month
    private JPanel content; // content panel
    private boolean selected; // whether the item is selected or not
    private int thickness = 2; // thickness of the border
    private Border border; // border when not hovered by mouse
    private Border hoverBorder; // border when hovered by mouse
    private Border selectedBorder; // border when selected by user

    DayItem(int day) {
        super();
        this.day = day;
        selected = false;
        setLayout(new BorderLayout());
        setBackground(ThemeColors.background);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        content = new JPanel();
        content.setBackground(ThemeColors.background);
        content.setLayout(new BorderLayout());

        border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, thickness);
        hoverBorder = BorderFactory.createLineBorder(Color.GRAY, thickness);
        selectedBorder = BorderFactory.createLineBorder(ThemeColors.accent, thickness);

        content.setBorder(border);

        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    content.setBorder(hoverBorder);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    content.setBorder(border);
                }
            }
        });

        JLabel label = new JLabel((day == -1) ? "" : Integer.toString(day), SwingConstants.CENTER);
        label.setBackground(ThemeColors.background);
        content.add(label, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    public int getDay() {
        return day;
    }

    public JPanel getContent() {
        return content;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (this.selected) {
            content.setBorder(selectedBorder);
        } else {
            content.setBorder(border);
        }
    }
}

class DateChooserTest extends JFrame {
    DateChooserTest() {
        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        DateChooser chooser = new DateChooser(LocalDate.of(2000, 5, 1), LocalDate.now());
        add(chooser);
        setVisible(true);
    }

    public static void main(String[] args) {
        new DateChooserTest();
    }
}