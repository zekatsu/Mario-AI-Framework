import engine.core.MarioEvent;
import engine.helper.EventType;
import engine.helper.SpriteType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class SearchEvent {
    private final JTable table;
    private DefaultTableModel tableModel;
    private final JComboBox<EventType> eventTypeDropdown;
    private final JComboBox<SpriteType> spriteTypeDropdown;
    private ArrayList<MarioEvent> gameEvents;
    private final ShowImage showImage;

    SearchEvent() {
        // read gameEvents.dat
        try {
            FileInputStream f = new FileInputStream("data/gameEvents.dat");
            ObjectInputStream in = new ObjectInputStream(f);
            this.gameEvents = (ArrayList<MarioEvent>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // GUI initialization
        this.eventTypeDropdown = new JComboBox<>(EventType.values());
        this.spriteTypeDropdown = new JComboBox<>(SpriteType.values());

        String[] columnsNames = {"eventType", "param", "select"};
        this.tableModel = new DefaultTableModel(columnsNames, 0);

        this.table = new JTable(this.tableModel);
        this.table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(this.eventTypeDropdown));
        this.table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(this.spriteTypeDropdown));

        JButton addButton = new JButton("add");
        JButton deleteButton = new JButton("delete");
        JButton searchButton = new JButton("search");
        addButton.addActionListener(new AddActionListener());
        deleteButton.addActionListener(new DeleteActionListener());
        searchButton.addActionListener(new SearchActionListener());

        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(250, 90));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.PAGE_END);

        JFrame frame = new JFrame();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // create showImage window
        this.showImage = new ShowImage();
    }

    private class AddActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            Object[] defaultData = {EventType.BUMP, SpriteType.NONE};
            tableModel.addRow(defaultData);
        }
    }

    private class DeleteActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (table.getSelectedRow() >= 0) {
                tableModel.removeRow(table.getSelectedRow());
            }
        }
    }

    private class SearchActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            ArrayList<MarioEvent> searchResult = searchEvent();
            for (MarioEvent event : searchResult) {
                System.out.println(event.getTime());
            }
            if (!searchResult.isEmpty()) {
                showImage.set(searchResult.get(0).getTime());
            }
        }

        public ArrayList<MarioEvent> searchEvent() {
            EventType targetEventType = (EventType) tableModel.getValueAt(0, 0);
            SpriteType targetSpriteType = (SpriteType) tableModel.getValueAt(0, 1);
            ArrayList<MarioEvent> ret = new ArrayList<>();
            for (MarioEvent event : gameEvents) {
                if (event.getEventType() == targetEventType.getValue() && event.getEventParam() == targetSpriteType.getValue()) {
                    ret.add(event);
                }
            }
            return ret;
        }
    }

    public static void main(String[] args) {
        new SearchEvent();
    }
}
