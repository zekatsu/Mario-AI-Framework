import engine.core.MarioGame;
import engine.core.MarioResult;
import engine.helper.GameStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class RecordGamePlay {

    public static void main(String[] args) {
        new RecordGamePlay();
    }

    RecordGamePlay() {
        MarioGame game = new MarioGame();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String level_name = scanner.next();
            String level = getLevel("./levels/original/lvl-" + level_name + ".txt");
            while (true) {
                MarioResult result = game.runGame(new agents.human.Agent(), level, 300, 0, true, 24);
                saveData(result);
                game.dispose();
                if (result.getGameStatus() == GameStatus.WIN) break;
            }
        }
    }

    private void saveData(MarioResult result) {
        try {
            SimpleDateFormat fileNameFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
            String dateString = fileNameFormat.format(Calendar.getInstance().getTime());
            String dirString = "data/" + dateString;
            Files.createDirectory(Paths.get(dirString));
            Files.createDirectory(Paths.get(dirString + "/img"));

            // save game events
            FileOutputStream f = new FileOutputStream(dirString + "/gameEvents.dat");
            BufferedOutputStream b = new BufferedOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(b);
            out.writeObject(result.getGameEvents());
            out.flush();

            // save screenshots
            ArrayList<BufferedImage> video = result.getSnapshots();
            for (int i = 0; i < video.size(); i++) {
                BufferedImage image = video.get(i);
                ImageIO.write(image, "png", new File(String.format(dirString + "/img/%d.png", i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
