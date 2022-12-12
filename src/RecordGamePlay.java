import engine.core.MarioGame;
import engine.core.MarioResult;
import engine.helper.GameStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                if (result.getGameStatus() == GameStatus.WIN) break;
            }
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
