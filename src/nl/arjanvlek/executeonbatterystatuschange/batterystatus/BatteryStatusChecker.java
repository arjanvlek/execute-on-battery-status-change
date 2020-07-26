package nl.arjanvlek.executeonbatterystatuschange.batterystatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;
import java.util.Scanner;

import nl.arjanvlek.executeonbatterystatuschange.Logger;
import nl.arjanvlek.executeonbatterystatuschange.wmimodels.Availability;
import nl.arjanvlek.executeonbatterystatuschange.wmimodels.BatteryStatus;

public class BatteryStatusChecker {
    private static final String POWER_STATE_COMMAND = "WMIC Path Win32_Battery Get * /Format:List";
    public static final BatteryStatusResult BATTERY_STATUS_UNKNOWN = new BatteryStatusResult(Availability.INTERNAL_UNKNOWN, BatteryStatus.INTERNAL_UNKNOWN, -1, -1);

    public static BatteryStatusResult getBatteryStatus() {
        Process process;
        try {
            if (System.getProperty("os.name", "false").equals("Mac OS X")) {
                process = Runtime.getRuntime().exec("cat /Users/arjan.vlek/Desktop/test-output.txt");
            } else {
                //process = Runtime.getRuntime().exec("cmd.exe /c \"type C:\\Users\\arjan\\IdeaProjects\\execute-on-battery-status-change\\sample-output-100percent-discharging.txt\"");
                process = Runtime.getRuntime().exec(POWER_STATE_COMMAND);
            }
        } catch (IOException e) {
            Logger.error("Error when executing battery status process", e);
            return BATTERY_STATUS_UNKNOWN;
        }
        InputStream inputStream = process.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");

        if (!scanner.hasNext()) {
            Logger.error("No output from power state command");
            return BATTERY_STATUS_UNKNOWN;
        }

        String rawOutput = scanner.next();
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(rawOutput));
        } catch (IOException e) {
            Logger.error("Un-parsable output from power state command: " + rawOutput);
        }

        int batteryStatusValue = BATTERY_STATUS_UNKNOWN.getBatteryStatus().getValue();
        boolean emptyBatteryStatus = false;
        String batteryStatusRawValue = properties.getProperty("BatteryStatus", String.valueOf(BATTERY_STATUS_UNKNOWN.getBatteryStatus().getValue()));
        try {
            if (batteryStatusRawValue != null && !batteryStatusRawValue.equals("")) {
                batteryStatusValue = Integer.parseInt(batteryStatusRawValue);
            } else {
                emptyBatteryStatus = true;
            }
        } catch (NumberFormatException e) {
            Logger.error("Non-numeric BatteryStatus output from power state command: " + rawOutput);
            return BATTERY_STATUS_UNKNOWN;
        }

        int availabilityValue = BATTERY_STATUS_UNKNOWN.getAvailability().getValue();
        boolean emptyAvailability = false;
        String availabilityRawValue = properties.getProperty("Availability", String.valueOf(BATTERY_STATUS_UNKNOWN.getAvailability().getValue()));

        try {
            if (availabilityRawValue != null && !availabilityRawValue.equals("")) {
                availabilityValue = Integer.parseInt(availabilityRawValue);
            } else {
                emptyAvailability = true;
            }
        } catch (NumberFormatException e) {
            Logger.error("Non-numeric Availability output from power state command: " + rawOutput);
            return BATTERY_STATUS_UNKNOWN;
        }

        if (emptyAvailability) {
            availabilityValue = Availability.INTERNAL_RUNNING_BUT_RECHARGING_NOT_ALLOWED.getValue();
        }

        if (emptyBatteryStatus) {
            batteryStatusValue = BatteryStatus.INTERNAL_DISCHARGING_AND_HIGH.getValue();
        }

        String chargeLevelRawValue = properties.getProperty("EstimatedChargeRemaining", "-1");
        int chargeLevel = -1;
        try {
            chargeLevel = Integer.parseInt(chargeLevelRawValue);
        } catch (NumberFormatException e) {
            Logger.error("Non-numeric EstimatedChargeLevel output from power state command: " + rawOutput);
        }

        String voltageRawValue = properties.getProperty("DesignVoltage", "-1");
        double voltage = -1;
        try {
            voltage = Double.parseDouble(voltageRawValue);
        } catch (NumberFormatException e) {
            Logger.error("Non-numeric DesignVoltage output from power state command: " + rawOutput);
        }

        return new BatteryStatusResult(Availability.forValue(availabilityValue), BatteryStatus.forValue(batteryStatusValue), chargeLevel, voltage / 1000);
    }
}
