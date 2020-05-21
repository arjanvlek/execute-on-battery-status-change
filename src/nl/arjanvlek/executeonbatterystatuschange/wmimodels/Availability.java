package nl.arjanvlek.executeonbatterystatuschange.wmimodels;

import java.util.Arrays;

/**
 * See Availability on https://docs.microsoft.com/en-us/windows/win32/cimwin32prov/win32-battery
 */
public enum Availability {

    OTHER(1),

    UNKNOWN(2),

    /** Running or Full Power */
    RUNNING_FULL_POWER(3),

    WARNING(4),

    IN_TEST(5),

    NOT_APPLICABLE(6),

    POWER_OFF (7),

    OFF_LINE(8),

    OFF_DUTY(9),

    DEGRADED(10),

    NOT_INSTALLED(11),

    INTERNAL_ERROR(12),

    /** The device is known to be in a power save mode, but its exact batterystatus is unknown. */
    POWER_SAVE_UNKNOWN(13),

    /** The device is in a power save state but still functioning, and may exhibit degraded performance. */
    POWER_SAVE_LOW_POWER_MODE(14),

    /** The device is not functioning, but could be brought to full power quickly. */
    POWER_SAVE_STANDBY(15),

    POWER_CYCLE(16),

    /** The device is in a warning state, though also in a power save mode. */
    POWER_SAVE_WARNING(17),

    /** The device is paused. */
    PAUSED(18),

    /** The device is not ready. */
    NOT_READY(19),

    /** The device is not configured. */
    NOT_CONFIGURED(20),

    /** The device is quiet. */
    QUIESCED(21),

    /** Used in this program only */
    INTERNAL_UNKNOWN(-1);

    private final int value;

    Availability(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Availability forValue(int value) {
        return Arrays.stream(values())
                .filter(v -> v.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not get Availability for value: " + value));
    }

    @Override
    public String toString() {
        return name() + " (" + value + ")";
    }
}
