import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShowImage implements ChangeListener {
    private final ImageComponent imageComponent;
    private final JSlider slider;
    private final JFrame frame;

    ShowImage() {
        this.imageComponent = new ImageComponent(2);

        this.slider = new JSlider(0, 887);
        this.slider.addChangeListener(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(this.imageComponent, BorderLayout.CENTER);
        panel.add(this.slider, BorderLayout.PAGE_END);

        this.frame = new JFrame("ShowImage");
        this.frame.getContentPane().add(panel);
        this.frame.pack();
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public void set(int i) {
        this.frame.setVisible(true);
        this.slider.setValue(i);
    }

    public void stateChanged(ChangeEvent changeEvent) {
        JSlider slider = (JSlider) changeEvent.getSource();
        try {
            BufferedImage image = ImageIO.read(new File(String.format("data/%d.png", slider.getValue())));
            this.imageComponent.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
