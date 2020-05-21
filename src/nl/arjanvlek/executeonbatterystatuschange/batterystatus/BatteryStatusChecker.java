package nl.arjanvlek.executeonbatterystatuschange.batterystatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;
import java.util.Scanner;

import nl.arjanvlek.executeonbatterystatuschange.wmimodels.Availability;
import nl.arjanvlek.executeonbatterystatuschange.wmimodels.BatteryStatus;

public class BatteryStatusChecker {
    private static final String POWER_STATE_COMMAND = "WMIC Path Win32_Battery Get * /Format:List";
    public static final BatteryStatusResult BATTERY_STATUS_UNKNOWN = new BatteryStatusResult(Availability.INTERNAL_UNKNOWN, BatteryStatus.INTERNAL_UNKNOWN);

    public static BatteryStatusResult getBatteryStatus() {
        Process process;
        try {
            if (System.getProperty("os.name", "false").equals("Mac OS X")) {
                process = Runtime.getRuntime().exec("cat /Users/arjan.vlek/Desktop/test-output.txt");
            } else {
                process = Runtime.getRuntime().exec(POWER_STATE_COMMAND);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return BATTERY_STATUS_UNKNOWN;
        }
        InputStream inputStream = process.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");

        if (!scanner.hasNext()) {
            System.err.println("No output from power state command");
            return BATTERY_STATUS_UNKNOWN;
        }

        String rawOutput = scanner.next();
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(rawOutput));
        } catch (IOException e) {
            System.err.println("Un-parsable output from power state command: " + rawOutput);
        }

        int batteryStatusValue;
        try {
            batteryStatusValue = Integer.parseInt(properties.getProperty("BatteryStatus", String.valueOf(BATTERY_STATUS_UNKNOWN.getBatteryStatus().getValue())));
        } catch (NumberFormatException e) {
            System.err.println("Non-numeric BatteryStatus output from power state command: " + rawOutput);
            return BATTERY_STATUS_UNKNOWN;
        }

        int availabilityValue;
        try {
            availabilityValue = Integer.parseInt(properties.getProperty("Availability", String.valueOf(BATTERY_STATUS_UNKNOWN.getAvailability().getValue())));
        } catch (NumberFormatException e) {
            System.err.println("Non-numeric Availability output from power state command: " + rawOutput);
            return BATTERY_STATUS_UNKNOWN;
        }

        return new BatteryStatusResult(Availability.forValue(availabilityValue), BatteryStatus.forValue(batteryStatusValue));
    }
}
