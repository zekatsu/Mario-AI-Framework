import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

    public enum LogType {
        QuizPlayPressed,
        QuizPlayClip,
        MenuAddPressed,
        MenuDeletePressed,
        MenuSearchPressed,
        TextSearchPressed,
        TextLMResponse,
        ResultShown,
        ResultPlayPressed,
        ResultPlayClip,
        ResultPreviousPressed,
        ResultNextPressed,
    }

    PrintWriter writer;

    public static String arrayToString(int[] a) {
        String[] ret = new String[a.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = String.valueOf(a[i]);
        }
        return String.join(" ", ret);
    }

    public void write(LogType logType) {
        write(logType, "");
    }

    public void write(LogType logType, int x) {
        write(logType, String.valueOf(x));
    }

    public void write(LogType logType, int[] a) {
        write(logType, arrayToString(a));
    }

    public void write(LogType logType, String body) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        String time = dateFormat.format(Calendar.getInstance().getTime());
        String log_type = logType.toString();
        body = body.replaceAll("\n", " ");
        writer.println(String.format("%s,%s,\"%s\"", time, log_type, body));
        writer.flush();
    }

    Logger() {
        try {
            SimpleDateFormat fileNameFormat = new SimpleDateFormat("MM-dd-HH-mm-ss");
            String dateString = fileNameFormat.format(Calendar.getInstance().getTime());
            String fileName = String.format("logs/%s.csv", dateString);
            Files.createFile(Paths.get(fileName));
            writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            writer.println("time,log_type,body");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}