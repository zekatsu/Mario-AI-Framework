import engine.core.MarioEvent;
import engine.helper.EventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TextInterface implements ActionListener {

    ArrayList<EventType> eventTypes;
    ArrayList<EventParam> eventParams;

    private final Search search;

    private final JTextArea textArea;
    private final JLabel messageLabel;
    private final JTextArea lmResponse;

    private final ShowResult showResult;

    private final Logger logger;

    TextInterface(Logger logger) {
        this.logger = logger;
        this.search = new Search();

        // GUI initialization
        this.textArea = new JTextArea(4, 15);
        textArea.setLineWrap(true);
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        JButton searchButton = new JButton("search");
        searchButton.addActionListener(this);
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.messageLabel = new JLabel("please enter text and press search");
        this.messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.lmResponse = new JTextArea(4, 15);
        this.lmResponse.setEditable(false);
        this.lmResponse.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(this.textArea);
        panel.add(searchButton);
        panel.add(this.messageLabel);
        panel.add(this.lmResponse);

        JFrame frame = new JFrame("Text Interface");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocation(800, 500);

        // create showImage window
        this.showResult = new ShowResult(this.logger);
        this.eventTypes = new ArrayList<>();
        this.eventParams = new ArrayList<>();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String cmd = actionEvent.getActionCommand();
        if (cmd.equals("search")) {
            try {
                this.logger.write(Logger.LogType.TextSearchPressed, this.textArea.getText());
                ApiRequest request = new ApiRequest(this.textArea.getText());
                this.lmResponse.setText(request.result);
                this.logger.write(Logger.LogType.TextLMResponse, request.result);
                this.parseText(request.result);
                ArrayList<ArrayList<MarioEvent>> searchResult = this.search.searchEvent(this.eventTypes, this.eventParams);
                if (!searchResult.isEmpty()) {
                    this.showResult.show(searchResult);
                } else {
                    this.messageLabel.setText("No results found");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseText(String text) {
        this.eventTypes.clear();
        this.eventParams.clear();
        Scanner scanner = new Scanner(text);
        while (scanner.hasNext()) {
            String[] line_split = scanner.nextLine().split("[.,] ");
//            int index = Integer.parseInt(line_split[0]);
            EventType type = EventType.valueOf(line_split[1]);
            EventParam param = EventParam.valueOf(line_split[2]);
            this.eventTypes.add(type);
            this.eventParams.add(param);
        }
    }

    public static void main(String[] args) {
        new TextInterface(new Logger());
    }
}
