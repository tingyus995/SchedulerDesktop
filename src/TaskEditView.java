import javax.swing.*;
import java.awt.*;

import java.time.LocalTime;

public class TaskEditView extends JPanel {
    protected JTextField nameField;
    protected TaskTypeSelector taskTypeSelector;
    protected  TimeChooser timeChooser;
    JSpinner timeRequiredSpinner;
    protected JButton dateChooseBtn;
    private GridBagConstraints gbc;
    private int rowCounter;

    TaskEditView(){
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        gbc = new GridBagConstraints();
        rowCounter = 0;
        gbc.weighty = 1;

        nameField = new JTextField();
        addLabeledRow(rowCounter++, "Task name:", nameField);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel();
        spinnerModel.setMaximum(1440);
        spinnerModel.setMinimum(1);
        spinnerModel.setValue(10);
        timeRequiredSpinner = new JSpinner(spinnerModel);
        addLabeledRow(rowCounter++, "Time Required:", timeRequiredSpinner);

        dateChooseBtn = new JButton("Select");
        addLabeledRow(rowCounter++, "Due Date:", dateChooseBtn);

        timeChooser = new TimeChooser(LocalTime.of(0, 0), LocalTime.of(23, 59));
        addLabeledRow(rowCounter++, "Due Time:", timeChooser);

        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = rowCounter++;
        gbc.gridwidth = 2;
        taskTypeSelector = new TaskTypeSelector();
        add(taskTypeSelector, gbc);
    }

    private void addLabeledRow(int row, String label, Component c){
        // label column
        gbc.weightx = 0.1;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);

        // component column
        gbc.weightx = 0.9;
        gbc.gridx = 1;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        add(c, gbc);

        // restore
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
    }
}
