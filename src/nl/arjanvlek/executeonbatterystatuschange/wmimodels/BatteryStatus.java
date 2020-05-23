package nl.arjanvlek.executeonbatterystatuschange.wmimodels;

import java.util.Arrays;

/**
 * See BatteryStatus on https://docs.microsoft.com/en-us/windows/win32/cimwin32prov/win32-battery
 */
public enum BatteryStatus {
    /** The battery is discharging. */
    OTHER(1),

    /** The system has access to AC so no battery is being discharged. However, the battery is not necessarily charging. */
    UNKNOWN(2),

    FULLY_CHARGED(3),

    LOW(4),

    CRITICAL(5),

    CHARGING(6),

    CHARGING_AND_HIGH(7),

    CHARGING_AND_LOW(8),

    CHARGING_AND_CRITICAL(9),

    UNDEFINED(10),

    PARTIALLY_CHARGED(11),

    /** Used in this program only */
    INTERNAL_UNKNOWN(-1),

    /** Used in this program only for when crappy Windows tablets report an empty string while discharging between 95%-100%... */
    INTERNAL_DISCHARGING_AND_HIGH(-2);

    private final int value;

    BatteryStatus(int value) {
        this.value = value;
    }

    public static BatteryStatus forValue(int value) {
        return Arrays.stream(values())
                .filter(v -> v.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported battery batterystatus number: " + value));
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name() + " (" + value + ")";
    }
}
