package scripts;

import engine.core.MarioEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class DumpGameEvents {
    DumpGameEvents() {
        try {
            FileInputStream f = new FileInputStream("data/gameEvents.dat");
            ObjectInputStream in = new ObjectInputStream(f);
            ArrayList<MarioEvent> gameEvents = (ArrayList<MarioEvent>) in.readObject();

            String fileName = "data/gameEvents.csv";
            Files.createFile(Path.of(fileName));
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            for (MarioEvent event: gameEvents) {
                writer.println(String.format("%d, %d, %d", event.getTime(), event.getEventType(), event.getEventParam()));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static public void main(String[] args) {
        new DumpGameEvents();
    }
}
