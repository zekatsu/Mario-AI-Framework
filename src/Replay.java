import engine.core.MarioGame;
import engine.core.MarioResult;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Replay {
    private final ImageComponent imageComponent;
    private final ArrayList<BufferedImage> snapshots;

    public static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private class onChange implements ChangeListener {
        JSlider slider;

        public void stateChanged(ChangeEvent e) {
            imageComponent.setImage(snapshots.get(slider.getValue()));
        }

        onChange(JSlider slider) {
            this.slider = slider;
        }
    }

    Replay(ArrayList<BufferedImage> snapshots) {
        this.snapshots = snapshots;

        //set UI
        this.imageComponent = new ImageComponent(2);

        JSlider slider = new JSlider(0, snapshots.size() - 1);
        slider.addChangeListener(new onChange(slider));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(this.imageComponent, BorderLayout.CENTER);
        panel.add(slider, BorderLayout.PAGE_END);

        JFrame frame = new JFrame("Replay");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        slider.setValue(0);
    }

    public static void main(String[] args) {
        MarioGame game = new MarioGame();
        MarioResult result = game.playGame(getLevel("./levels/original/lvl-1.txt"), 200, 0);
//        MarioResult result = game.runGame(new agents.robinBaumgarten.Agent(), getLevel("./levels/original/lvl-1.txt"), 20, 0, true);
        try {
            FileOutputStream f = new FileOutputStream("data/gameEvents.dat");
            BufferedOutputStream b = new BufferedOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(b);
            out.writeObject(result.getGameEvents());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<BufferedImage> video = result.getSnapshots();
        try {
            for (int i = 0; i < video.size(); i++) {
                BufferedImage image = video.get(i);
                ImageIO.write(image, "png", new File(String.format("data/img/%d.png", i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Replay(result.getSnapshots());
    }
}
