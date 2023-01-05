import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageComponent extends JComponent {
    int scale;
    private BufferedImage image;

    private int imgCount;

    ImageComponent(int scale) {
        this.scale = scale;
        Dimension size = new Dimension(256 * this.scale, 240 * this.scale);

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        try {
            this.imgCount = (int) Files.list(Paths.get("data/img")).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImage(final BufferedImage image) {
        this.image = image;
        paintImmediately(0, 0, 256 * this.scale, 240 * this.scale);
    }

    public void set(int time) {
        try {
            BufferedImage image = ImageIO.read(new File(String.format("data/img/%d.png", time)));
            this.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(int start, int end) {
        start = Math.max(start, 0);
        end = Math.min(end, this.imgCount);
        try {
            for (int time = start; time < end; time++) {
                BufferedImage image = ImageIO.read(new File(String.format("data/img/%d.png", time)));
                this.setImage(image);
                Thread.sleep(1000 / 24);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.image != null)
            g.drawImage(this.image, 0, 0, 256 * this.scale, 240 * this.scale, this);
    }
}