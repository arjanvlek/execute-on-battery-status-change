package nl.arjanvlek.executeonbatterystatuschange;

import static nl.arjanvlek.executeonbatterystatuschange.batterystatus.BatteryStatusChecker.BATTERY_STATUS_UNKNOWN;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import nl.arjanvlek.executeonbatterystatuschange.batterystatus.BatteryStatusChecker;
import nl.arjanvlek.executeonbatterystatuschange.batterystatus.BatteryStatusResult;
import nl.arjanvlek.executeonbatterystatuschange.wmimodels.BatteryStatus;

public class Main {

    private static final BatteryStatusResult BATTERY_STATUS_NOT_SET = null;
    private static final AtomicBoolean DEBUG = new AtomicBoolean(false);

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar execute-on-battery-batterystatus-change.jar <program_to_execute> <program_to_execute_args>");
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
            final AtomicBoolean previousExecutionFailed = new AtomicBoolean(false);

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Quitting...");
                    System.exit(0);
                }
                BatteryStatusResult batteryStatus = BatteryStatusChecker.getBatteryStatus();
                if (DEBUG.get()) {
                    System.out.println(batteryStatus.toString());
                }

                // If no batterystatus could be obtained, try one more time or otherwise quit.
                if (batteryStatus == BATTERY_STATUS_UNKNOWN) {
                    if (!previousExecutionFailed.compareAndSet(false, true)) {
                        System.err.println("Unable to obtain power state");
                        System.exit(2);
                    }
                    System.err.println("Unable to obtain power state, trying one more time");
                    continue;
                }

                // On 1st run, only notify that program is running. Otherwise it will always have "changed".
                if (previousBatteryStatus == BATTERY_STATUS_NOT_SET) {
                    previousBatteryStatus = batteryStatus;
                    System.out.println("Now monitoring for power state changes...");
                    continue;
                }

                // On subsequent runs, if the batteryStatus or availability changes, execute the specified program.
                if (previousExecutionFailed.get() || !previousBatteryStatus.equals(batteryStatus)) {
                    try {
                        previousBatteryStatus = batteryStatus;
                        System.out.println("Power state has changed!\n\nFrom:\n" + previousBatteryStatus + "\n\nTo:\n+ " + batteryStatus + "\n\nExecuting the program now...");
                        Runtime.getRuntime().exec(programToExecuteOnStatusChange);
                        previousExecutionFailed.set(false);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        if (!previousExecutionFailed.compareAndSet(false, true)) {
                            System.err.println("Error executing program on power state change");
                            System.exit(3);
                            // stop if the previous execution failed as well.
                        } else {
                            System.err.println("Error executing program on power state change, trying one more time");
                        }
                        // Keep running otherwise
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void startExitWatcher() {
        new Thread(() -> {
            System.out.println("To exit this program, type 'exit', 'quit' or ':q'...");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String typedCharacters = scanner.nextLine();
                if (typedCharacters.contains("exit") || typedCharacters.contains(":q") || typedCharacters.contains("quit")) {
                    System.out.println("Quitting...");
                    System.exit(0);
                }
            }
        }).start();
    }
}
