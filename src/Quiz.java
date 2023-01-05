import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Quiz implements ActionListener {

    private int[] startTimes = {0, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000};
    private int index = 0;
    private int clipLength = 5;

    private ImageComponent imageComponent;

    private Logger logger;

    private MenuInterface menuInterface;
    private TextInterface textInterface;

    private  JLabel messageLabel = new JLabel(String.format("%d / %d", this.index + 1, startTimes.length));

    Quiz() {
        this.imageComponent = new ImageComponent(2);
        this.setStartImage();

        this.logger = new Logger();

        this.menuInterface = new MenuInterface(this.logger);
        this.textInterface = new TextInterface(this.logger);

        // init GUI
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

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(this.imageComponent);
        panel.add(buttonPanel);
        panel.add(this.messageLabel);

        JFrame frame = new JFrame("Quiz");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String cmd = actionEvent.getActionCommand();
        if (cmd.equals("play")) {
            logger.write(Logger.LogType.PlayPressed, this.index);
            this.playClip();
        }
        if (cmd.equals("previous")) {
            if (this.index > 0) {
                this.index--;
                this.updateMessage();
                this.setStartImage();
            }
        }
        if (cmd.equals("next")) {
            if (this.index < this.startTimes.length - 1) {
                this.index++;
                this.updateMessage();
                this.setStartImage();
            }
        }
    }

    private void setStartImage() {
        int startTime = this.startTimes[this.index];
        this.imageComponent.set(startTime);
    }

    private void playClip() {
        int startTime = this.startTimes[this.index];
        this.imageComponent.play(startTime, startTime + 24 * this.clipLength);
    }

    private void updateMessage() {
        this.messageLabel.setText(String.format("%d / %d", this.index + 1, startTimes.length));
    }

    public static void main(String[] args) {
        new Quiz();
    }
}
