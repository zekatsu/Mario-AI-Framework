import engine.core.MarioEvent;
import engine.helper.EventType;
import engine.helper.SpriteType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class SearchEvent {
    private JComboBox<EventType> eventTypeDropdown;
    private JComboBox<SpriteType> spriteTypeDropdown;
    private ArrayList<MarioEvent> gameEvents;
    SearchEvent() {
        // read gameEvents.dat
        try {
            FileInputStream f = new FileInputStream("data/gameEvents.dat");
            ObjectInputStream in = new ObjectInputStream(f);
            gameEvents = (ArrayList<MarioEvent>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // GUI initialization
        eventTypeDropdown = new JComboBox<>(EventType.values());
        spriteTypeDropdown = new JComboBox<>(SpriteType.values());
        JButton searchButton = new JButton("search");
        searchButton.addActionListener(new searchAction());

        JPanel panel = new JPanel();
        panel.add(eventTypeDropdown);
        panel.add(spriteTypeDropdown);
        panel.add(searchButton);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.getContentPane().add(panel);
    }

    private class searchAction implements ActionListener {
        public ArrayList<MarioEvent> searchEvent(EventType eventType) {
            ArrayList<MarioEvent> ret = new ArrayList<>();
            for (MarioEvent event: gameEvents) {
                if (event.getEventType() == eventType.getValue()) {
                    ret.add(event);
                }
            }
            return ret;
        }
        public void actionPerformed(ActionEvent actionEvent) {
            EventType targetEventType = (EventType) eventTypeDropdown.getSelectedItem();
            for (MarioEvent event: searchEvent(targetEventType)) {
                System.out.println(event.getTime());
            }
        }
    }

    public static void main(String[] args) {
        new SearchEvent();
    }
}
