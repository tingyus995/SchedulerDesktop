import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TaskTypeSelector extends JPanel {

    ArrayList<TaskTypeButton> buttons;
    Task.TaskType selectedType;

    TaskTypeSelector() {
        super();
        buttons = new ArrayList<TaskTypeButton>();
        setType(Task.TaskType.URGENT_IMPORTANT);

        setLayout(new GridLayout(2, 2));


        TaskTypeButton btn1 = new TaskTypeButton("Non-urgent", "Important",
                Task.TaskType.NON_URGENT_IMPORTANT, ThemeColors.non_urgent_important_bg);
        add(btn1);
        buttons.add(btn1);

        TaskTypeButton btn2 = new TaskTypeButton("Urgent", "Important",
                Task.TaskType.URGENT_IMPORTANT, ThemeColors.urgent_important_bg);
        add(btn2);
        buttons.add(btn2);

        TaskTypeButton btn3 = new TaskTypeButton("Non-urgent", "Unimportant",
                Task.TaskType.NON_URGENT_UNIMPORTANT, ThemeColors.non_urgent_unimportant_bg);
        add(btn3);
        buttons.add(btn3);

        TaskTypeButton btn4 = new TaskTypeButton("Urgent", "Unimportant",
                Task.TaskType.URGENT_UNIMPORTANT, ThemeColors.urgent_unimportant_bg);
        add(btn4);
        buttons.add(btn4);

        for(TaskTypeButton btn : buttons){
            btn.getContent().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    for(TaskTypeButton b : buttons){
                        if(b == btn){
                            b.setSelected(true);
                            selectedType = b.getType();
                        }else {
                            b.setSelected(false);
                        }

                    }
                }
            });
        }
    }

    Task.TaskType getType(){
        return selectedType;
    }

    void setType(Task.TaskType type){
        this.selectedType = type;
        System.out.println(selectedType);
        for(TaskTypeButton b: buttons){
            b.setSelected((b.getType() == selectedType));
        }
    }
}


class TaskTypeButton extends JPanel {
    private Task.TaskType type;
    private JPanel content;
    private boolean selected;

    TaskTypeButton(String text1, String text2, Task.TaskType type, Color background) {
        super();
        this.type = type;
        selected = false;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setBackground(ThemeColors.background);

        content = new JPanel();
        content.setBackground(background);
        content.add(new JLabel(text1));
        content.add(new JLabel(text2));
        Color hoverBackground = ThemeColors.lighten(background, 15);
        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                content.setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                content.setBackground(background);
            }
        });
        setSelected(false);

        add(content, BorderLayout.CENTER);
    }

    public JPanel getContent(){
        return content;
    }

    public Task.TaskType getType() {
        return type;
    }

    public void setType(Task.TaskType type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if(selected){
            content.setBorder(BorderFactory.createLineBorder(ThemeColors.accent, 3));
        }else {
            content.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        }

    }
}
