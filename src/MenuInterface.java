import engine.core.MarioEvent;
import engine.helper.EventType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MenuInterface implements ActionListener {
    private final JTable table;
    private DefaultTableModel tableModel;
    private final JComboBox<EventType> eventTypeDropdown;
    private final JComboBox<EventParam> eventParamDropdown;

    private final JLabel messageLabel;

    private final ShowResult showResult;

    private ArrayList<MarioEvent> gameEvents;

    private Search search;
    private ArrayList<ArrayList<MarioEvent>> searchResult;
    private int searchResultIndex;

    private Logger logger;

    MenuInterface(Logger logger) {
        this.logger = logger;
        // read gameEvents.dat
        try {
            FileInputStream f = new FileInputStream("data/gameEvents.dat");
            ObjectInputStream in = new ObjectInputStream(f);
            this.gameEvents = (ArrayList<MarioEvent>) in.readObject();
            this.search = new Search(this.gameEvents);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // GUI initialization
        this.eventTypeDropdown = new JComboBox<>(EventType.values());
        this.eventParamDropdown = new JComboBox<>(EventParam.values());

        String[] columnsNames = {"eventType", "param", "select"};
        this.tableModel = new DefaultTableModel(columnsNames, 0);

        this.table = new JTable(this.tableModel);
        this.table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(this.eventTypeDropdown));
        this.table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(this.eventParamDropdown));

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

        this.messageLabel = new JLabel("add event and press search");
        this.messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(editButtonPanel);
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
        frame.setLocation(800, 200);

        // create showImage window
        this.showResult = new ShowResult(this.logger);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String cmd = actionEvent.getActionCommand();
        if (cmd.equals("add")) {
            Object[] defaultData = {EventType.BUMP, EventParam.NONE};
            this.tableModel.addRow(defaultData);
        }
        if (cmd.equals("delete")) {
            if (table.getSelectedRow() >= 0) {
                tableModel.removeRow(table.getSelectedRow());
            }
        }
        if (cmd.equals("search")) {
            if (this.tableModel.getRowCount() == 0) {
                this.messageLabel.setText("add event and press search");
                this.messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            } else {
                this.searchResult = search.searchEvent(this.tableModel);
                if (!searchResult.isEmpty()) {
                    this.searchResultIndex = 0;
                    this.showResult.show(this.searchResult);
                } else {
                    this.messageLabel.setText("No results found");
                    this.messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                }
            }
        }
    }

    public static void main(String[] args) {
        new MenuInterface(new Logger());
    }
}
