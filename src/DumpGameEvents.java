import engine.core.MarioEvent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class DumpGameEvents {
    DumpGameEvents() {
        try {
            FileInputStream f = new FileInputStream("data/gameEvents.dat");
            ObjectInputStream in = new ObjectInputStream(f);
            ArrayList<MarioEvent> gameEvents = (ArrayList<MarioEvent>) in.readObject();
            for (MarioEvent event: gameEvents) {
                System.out.print(event.getEventType());
                System.out.print(", ");
                System.out.println(event.getEventParam());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static public void main(String[] args) {
        new DumpGameEvents();
    }
}
