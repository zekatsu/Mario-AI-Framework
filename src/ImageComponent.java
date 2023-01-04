import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageComponent extends JComponent {
    int scale;
    private BufferedImage image;

    ImageComponent(int scale) {
        this.scale = scale;
        Dimension size = new Dimension(256 * this.scale, 240 * this.scale);

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }

    public void setImage(final BufferedImage image) {
        this.image = image;
        paintImmediately(0, 0, 256 * this.scale, 240 * this.scale);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.image != null)
            g.drawImage(this.image, 0, 0, 256 * this.scale, 240 * this.scale, this);
    }
}