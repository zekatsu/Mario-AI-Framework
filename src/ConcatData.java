import engine.core.MarioEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class ConcatData {
    public static void main(String[] args) {
        try {
            Path[] dataPathArray = Files.list(Paths.get("data")).filter(path -> {
                try {
                    return !Files.isHidden(path);
                } catch (IOException e) {
                    return false;
                }
            }).sorted().toArray(Path[]::new);
            int len = dataPathArray.length;
            Integer[] imgNumArray = Arrays.stream(dataPathArray).map(path -> {
                try {
                    return (int) Files.list(path.resolve("img")).count();
                } catch (IOException e) {
                    return 0;
                }
            }).toArray(Integer[]::new);
            Integer[] imgNumCumArray = new Integer[len];
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    imgNumCumArray[i] = 0;
                } else {
                    imgNumCumArray[i] = imgNumCumArray[i - 1] + imgNumArray[i - 1];
                }
            }
            ArrayList<MarioEvent> gameEvents = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                FileInputStream f = new FileInputStream(dataPathArray[i].resolve("gameEvents.dat").toFile());
                ObjectInputStream in = new ObjectInputStream(f);
                ArrayList<MarioEvent> i_gameEvents = (ArrayList<MarioEvent>) in.readObject();
                for (int j = 0; j < i_gameEvents.size(); j++) {
                    MarioEvent event = i_gameEvents.get(j);
                    event.setTime(event.getTime() + imgNumCumArray[i]);
                    gameEvents.add(event);
                }
            }
            // save game events
            FileOutputStream f = new FileOutputStream("data/gameEvents.dat");
            BufferedOutputStream b = new BufferedOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(b);
            out.writeObject(gameEvents);
            out.flush();

            // copy img
            Path newImgPath = Path.of("data/img");
            Files.createDirectory(newImgPath);
            for (int i = 0; i < len; i++) {
                Path imgPath = dataPathArray[i].resolve("img");
                int imgNum = imgNumArray[i];
                for (int imgIndex = 0; imgIndex < imgNum; imgIndex++) {
                    Files.copy(imgPath.resolve(String.format("%d.png", imgIndex)), newImgPath.resolve(String.format("%d.png", imgIndex + imgNumCumArray[i])));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
