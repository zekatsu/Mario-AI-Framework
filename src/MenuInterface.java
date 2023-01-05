import engine.core.MarioEvent;
import engine.helper.EventType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MenuInterface implements ActionListener {
    private final JTable table;
    private final DefaultTableModel tableModel;

    private final JLabel messageLabel;

    private final ShowResult showResult;

    private final Search search;

    private final Logger logger;

    MenuInterface(Logger logger) {
        this.logger = logger;
        this.search = new Search();

        // GUI initialization
        JComboBox<EventType> eventTypeDropdown = new JComboBox<>(EventType.values());
        JComboBox<EventParam> eventParamDropdown = new JComboBox<>(EventParam.values());

        String[] columnsNames = {"eventType", "param", "select"};
        this.tableModel = new DefaultTableModel(columnsNames, 0);

        this.table = new JTable(this.tableModel);
        this.table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(eventTypeDropdown));
        this.table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(eventParamDropdown));

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

        JFrame frame = new JFrame("Menu Interface");
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
            this.logger.write(Logger.LogType.MenuAddPressed);
        }
        if (cmd.equals("delete")) {
            if (table.getSelectedRow() >= 0) {
                tableModel.removeRow(table.getSelectedRow());
                this.logger.write(Logger.LogType.MenuDeletePressed);
            }
        }
        if (cmd.equals("search")) {
            if (this.tableModel.getRowCount() == 0) {
                this.messageLabel.setText("add event and press search");
                this.messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            } else {
                this.logger.write(Logger.LogType.MenuSearchPressed);
                ArrayList<ArrayList<MarioEvent>> searchResult = search.searchEvent(this.tableModel);
                if (!searchResult.isEmpty()) {
                    this.showResult.show(searchResult);
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
