package nl.arjanvlek.executeonbatterystatuschange;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public synchronized static void info(String text) {
        System.out.println(text);
        logToFile("output.log", text, null);
    }

    public synchronized static void error(String text) {
        error(text, null);
    }

    public synchronized static void error(String text, Throwable throwable) {
        System.err.println(text);
        logToFile("error.log", text, throwable);
    }

    private synchronized static void logToFile(String filename, String text, Throwable throwable) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(filename), true)) {
            LocalDateTime now = LocalDateTime.now();
            String dateString = String.format("[%s]: ", now.format(DateTimeFormatter.ISO_DATE_TIME).replace("T", " "));
            fileOutputStream.write(dateString.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.write(text.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
            if (throwable != null) {
                PrintWriter writer = new PrintWriter(fileOutputStream);
                throwable.printStackTrace(writer);
                writer.flush();
            }
        } catch (Exception e) {
            System.err.println("Error when logging!");
            e.printStackTrace();
            System.err.println("Original log message: " + text);
            if (throwable != null) {
                System.err.println("Original error:");
                throwable.printStackTrace();
            }
        }
    }
}
