import engine.core.MarioEvent;
import engine.helper.EventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class TextInterface implements ActionListener {

    private ArrayList<MarioEvent> gameEvents;
    ArrayList<EventType> eventTypes;
    ArrayList<EventParam> eventParams;

    private Search search;
    private ArrayList<ArrayList<MarioEvent>> searchResult;
    private int searchResultIndex;

    private JTextArea textArea;
    private JTextArea message;

    private final ShowImage showImage;

    TextInterface() {
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
        this.textArea = new JTextArea(4, 20);
        textArea.setLineWrap(true);
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        JButton submitButton = new JButton("submit");
        submitButton.addActionListener(this);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.message = new JTextArea(4, 20);
        this.message.setEditable(false);
        this.message.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(this.textArea);
        panel.add(submitButton);
        panel.add(this.message);

        JFrame frame = new JFrame("TextSearch");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // create showImage window
        this.showImage = new ShowImage();
        this.eventTypes = new ArrayList<>();
        this.eventParams = new ArrayList<>();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String cmd = actionEvent.getActionCommand();
        if (cmd.equals("submit")) {
            try {
                ApiRequest request = new ApiRequest(this.textArea.getText());
                this.message.setText(request.result);
                this.parseText(request.result);
                this.searchResult = this.search.searchEvent(this.eventTypes, this.eventParams);
                if (!searchResult.isEmpty()) {
                    this.searchResultIndex = 0;
                    this.updateSearchResult();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
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

    private void updateSearchResult() {
        showImage.set(searchResult.get(this.searchResultIndex).get(0).getTime());
    }

    public static void main(String[] args) {
        new TextInterface();
    }
}
