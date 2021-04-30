import javax.swing.*;
import javax.swing.plaf.DesktopIconUI;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Debug extends JFrame {
    final static String BUTTONPANEL = "Card with JButtons";
    final static String TEXTPANEL = "Card with JTextField";

    Debug() {
        setLayout(new BorderLayout());
        setSize(1280, 720);


        //Where instance variables are declared:
        JPanel cards;


//Where the components controlled by the CardLayout are initialized:
//Create the "cards".
        JButton card1 = new JButton("Card 1");
        JLabel card2 = new JLabel("Card 2");


//Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(card1, BUTTONPANEL);
        cards.add(card2, TEXTPANEL);



        //Where the GUI is assembled:
        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        String comboBoxItems[] = { BUTTONPANEL, TEXTPANEL };
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout layout = (CardLayout) cards.getLayout();
                layout.show(cards, (String) e.getItem());
            }
        });
        comboBoxPane.add(cb);

        add(comboBoxPane, BorderLayout.PAGE_START);
        add(cards, BorderLayout.CENTER);
        setVisible(true);


    }

    public static void main(String[] args) {
        new Debug();
    }
}
