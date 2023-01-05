import engine.core.MarioEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ShowResult implements ActionListener {
    private final ImageComponent imageComponent;
    private final JFrame frame;
    private final JLabel messageLabel;

    private ArrayList<ArrayList<MarioEvent>> result;
    private int index;

    private final Logger logger;

    ShowResult(Logger logger) {
        this.logger = logger;
        this.imageComponent = new ImageComponent(2);

        JButton playButton = new JButton("play");
        JButton previousButton = new JButton("previous");
        JButton nextButton = new JButton("next");
        playButton.addActionListener(this);
        previousButton.addActionListener(this);
        nextButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(playButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.messageLabel = new JLabel("test");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(this.imageComponent);
        panel.add(buttonPanel);
        panel.add(this.messageLabel);

        this.frame = new JFrame("Result");
        this.frame.getContentPane().add(panel);
        this.frame.pack();
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.frame.setLocation(1200, 200);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String cmd = actionEvent.getActionCommand();
        if (cmd.equals("play")) {
            logger.write(Logger.LogType.ResultPlayPressed, this.index);
            this.playClip();
        }
        if (cmd.equals("previous")) {
            if (this.index > 0) {
                this.index--;
                this.logger.write(Logger.LogType.ResultPreviousPressed, this.index);
                this.updateMessage();
                this.setStartImage();
            }
        }
        if (cmd.equals("next")) {
            if (this.index < this.result.size() - 1) {
                this.index++;
                this.logger.write(Logger.LogType.ResultNextPressed, this.index);
                this.updateMessage();
                this.setStartImage();
            }
        }
    }

    public void show(ArrayList<ArrayList<MarioEvent>> result) {
        this.result = result;
        this.frame.setVisible(true);
        this.index = 0;
        this.updateMessage();
        this.setStartImage();
        this.logger.write(Logger.LogType.ResultShown, this.result.size());
    }

    private void setStartImage() {
        int start = this.result.get(this.index).get(0).getTime() - 12;
        this.imageComponent.set(start);
    }

    private void playClip() {
        ArrayList<MarioEvent> data = this.result.get(this.index);
        int start = data.get(0).getTime() - 12;
        int end = data.get(data.size() - 1).getTime() + 12;
        int[] body = {start, end};
        this.logger.write(Logger.LogType.ResultPlayClip, body);
        this.imageComponent.play(start, end);
    }

    private void updateMessage() {
        this.messageLabel.setText(String.format("%d / %d", this.index + 1, this.result.size()));
        this.messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
