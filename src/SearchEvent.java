import engine.core.MarioEvent;
import engine.helper.EventType;
import engine.helper.SpriteType;
import engine.sprites.Mario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class SearchEvent implements ActionListener {
    private final JTable table;
    private DefaultTableModel tableModel;
    private final JComboBox<EventType> eventTypeDropdown;
    private final JComboBox<SpriteType> spriteTypeDropdown;

    private final JLabel messageLabel;

    private final ShowImage showImage;

    private ArrayList<MarioEvent> gameEvents;

    private ArrayList<ArrayList<MarioEvent>> searchResult;
    private int searchResultIndex;

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

        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(250, 90));

        JButton addButton = new JButton("add");
        JButton deleteButton = new JButton("delete");
        JButton searchButton = new JButton("search");
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        searchButton.addActionListener(this);

        JPanel editButtonPanel = new JPanel();
        editButtonPanel.setLayout(new FlowLayout());
        editButtonPanel.add(addButton);
        editButtonPanel.add(deleteButton);
        editButtonPanel.add(searchButton);

        JButton previousButton = new JButton("previous");
        JButton nextButton = new JButton("next");
        previousButton.addActionListener(this);
        nextButton.addActionListener(this);

        JPanel navigateButtonPanel = new JPanel();
        navigateButtonPanel.setLayout(new FlowLayout());
        navigateButtonPanel.add(previousButton);
        navigateButtonPanel.add(nextButton);

        this.messageLabel = new JLabel("test");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(editButtonPanel);
        buttonPanel.add(navigateButtonPanel);
        buttonPanel.add(messageLabel);

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

    public void actionPerformed(ActionEvent actionEvent) {
        String cmd = actionEvent.getActionCommand();
        if (cmd.equals("add")) {
            Object[] defaultData = {EventType.BUMP, SpriteType.NONE};
            this.tableModel.addRow(defaultData);
        }
        if (cmd.equals("delete")) {
            if (table.getSelectedRow() >= 0) {
                tableModel.removeRow(table.getSelectedRow());
            }
        }
        if (cmd.equals("search")) {
            this.searchResult = searchEvent();
            for (ArrayList<MarioEvent> events: this.searchResult) {
                System.out.println(events.get(0).getTime());
            }
            if (!searchResult.isEmpty()) {
                this.searchResultIndex = 0;
                this.updateSearchResult();
            } else {
                this.messageLabel.setText("No results found");
            }
        }
        if (cmd.equals("previous")) {
            if (this.searchResultIndex > 0) {
                this.searchResultIndex--;
                this.updateSearchResult();
            }
        }
        if (cmd.equals("next")) {
            if (this.searchResultIndex < this.searchResult.size() - 1) {
                this.searchResultIndex++;
                this.updateSearchResult();
            }
        }
    }

    private void updateSearchResult() {
        showImage.set(searchResult.get(this.searchResultIndex).get(0).getTime());
        this.messageLabel.setText(String.format("%d / %d", this.searchResultIndex + 1, this.searchResult.size()));
    }

    private ArrayList<ArrayList<MarioEvent>> searchEvent() {
        int rowCount = tableModel.getRowCount();
        ArrayList<ArrayList<MarioEvent>> ret = new ArrayList<>();
        if (rowCount == 0) {
            return ret;
        }
        for (int p = 0; p < gameEvents.size(); p++) {
            ArrayList<MarioEvent> result = searchEvent(0, p);
            if (!result.isEmpty()) {
                int start = result.get(0).getTime();
                int end = result.get(result.size() - 1).getTime();
                if (end - start <= 5 * 24) {
                    // check if the last element is unique
                    if (!ret.isEmpty()) {
                        ArrayList<MarioEvent> previousResult = ret.get(ret.size() - 1);
                        if (end == previousResult.get(previousResult.size() - 1).getTime()) {
                            ret.remove(ret.size() - 1);
                        }
                    }
                    ret.add(result);
                }
            }
        }
        return ret;
    }

    private ArrayList<MarioEvent> searchEvent(int i, int pos) {
        int rowCount = tableModel.getRowCount();
        ArrayList<MarioEvent> ret = new ArrayList<>();
        EventType targetEventType = (EventType) tableModel.getValueAt(i, 0);
        SpriteType targetSpriteType = (SpriteType) tableModel.getValueAt(i, 1);
        MarioEvent event = gameEvents.get(pos);
        if (event.getEventType() == targetEventType.getValue() && event.getEventParam() == targetSpriteType.getValue()) {
            if (i == rowCount - 1) {
                ret.add(event);
                return ret;
            } else {
                for (int p = pos + 1; p < gameEvents.size(); p++) {
                    ArrayList<MarioEvent> result = searchEvent(i + 1, p);
                    if (!result.isEmpty()) {
                        ret.add(event);
                        ret.addAll(result);
                        return ret;
                    }
                }
                return ret;
            }
        } else {
            return ret;
        }
    }

    public static void main(String[] args) {
        new SearchEvent();
    }
}
