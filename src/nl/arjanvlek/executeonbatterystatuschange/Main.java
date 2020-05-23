package nl.arjanvlek.executeonbatterystatuschange;

import static nl.arjanvlek.executeonbatterystatuschange.batterystatus.BatteryStatusChecker.BATTERY_STATUS_UNKNOWN;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import nl.arjanvlek.executeonbatterystatuschange.batterystatus.BatteryStatusChecker;
import nl.arjanvlek.executeonbatterystatuschange.batterystatus.BatteryStatusResult;

public class Main {

    private static final BatteryStatusResult BATTERY_STATUS_NOT_SET = null;
    private static final AtomicBoolean DEBUG = new AtomicBoolean(false);

    public static void main(String[] args) {
        Logger.info("Application started with arguments " + String.join(" ", args));
        if (args.length < 1) {
            Logger.error("Usage: java -jar execute-on-battery-batterystatus-change.jar <program_to_execute> <program_to_execute_args>");
            System.exit(1);
        }

        if (args[args.length - 1].equals("--DEBUG")) {
            DEBUG.set(true);
        }

        String programToExecute = String.join(" " , args);

        if (DEBUG.get()) {
            programToExecute = programToExecute.replace("--DEBUG", "");
        }

        startExitWatcher();
        startBatteryStatusWatcher(programToExecute);
    }

    private static void startBatteryStatusWatcher(String programToExecuteOnStatusChange) {
        try {
            BatteryStatusResult previousBatteryStatus = BATTERY_STATUS_NOT_SET;

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Logger.error("Warning: Sleep execution was aborted!");
                    continue;
                }
                BatteryStatusResult batteryStatus = BatteryStatusChecker.getBatteryStatus();
                if (DEBUG.get()) {
                    Logger.info(batteryStatus.toString());
                }

                // If no batterystatus could be obtained, try one more time or otherwise quit.
                if (batteryStatus == BATTERY_STATUS_UNKNOWN) {
                    Logger.error("Unable to obtain power state");
                    continue;
                }

                // On 1st run, only notify that program is running. Otherwise it will always have "changed".
                if (previousBatteryStatus == BATTERY_STATUS_NOT_SET) {
                    previousBatteryStatus = batteryStatus;
                    Logger.info("Now monitoring for power state changes...");
                    continue;
                }

                // On subsequent runs, if the batteryStatus or availability changes, execute the specified program.
                if (!previousBatteryStatus.equals(batteryStatus)) {
                    try {
                        Logger.info("Power state has changed!\n\nFrom:\n" + previousBatteryStatus + "\n\nTo:\n+ " + batteryStatus + "\n\nExecuting the program now...");
                        previousBatteryStatus = batteryStatus;
                        Runtime.getRuntime().exec(programToExecuteOnStatusChange);
                    } catch (IOException e) {
                        Logger.error("Error executing program on power state change", e);
                    }
                }
            }
        } catch (Throwable t) {
            Logger.error("An unknown error has occurred!", t);
            startBatteryStatusWatcher(programToExecuteOnStatusChange);
        }
    }

    private static void startExitWatcher() {
        new Thread(() -> {
            Logger.info("To exit this program, type 'exit', 'quit' or ':q'...");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String typedCharacters = scanner.nextLine();
                if (typedCharacters.contains("exit") || typedCharacters.contains(":q") || typedCharacters.contains("quit")) {
                    Logger.info("Quitting...");
                    System.exit(0);
                }
            }
        }).start();
    }
}
