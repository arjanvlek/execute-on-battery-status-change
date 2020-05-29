package nl.arjanvlek.executeonbatterystatuschange.batterystatus;

import java.util.Objects;

import nl.arjanvlek.executeonbatterystatuschange.wmimodels.Availability;
import nl.arjanvlek.executeonbatterystatuschange.wmimodels.BatteryStatus;

public class BatteryStatusResult {

    private final Availability availability;
    private final BatteryStatus batteryStatus;
    private final int chargeLevel;

    public BatteryStatusResult(Availability availability, BatteryStatus batteryStatus, int chargeLevel) {
        if (availability == null || batteryStatus == null) {
            throw new IllegalArgumentException("availability and batteryStatus cannot be null!");
        }

        this.availability = availability;
        this.batteryStatus = batteryStatus;
        this.chargeLevel = chargeLevel;
    }

    public Availability getAvailability() {
        return availability;
    }

    public BatteryStatus getBatteryStatus() {
        return batteryStatus;
    }

    public int getChargeLevel() {
        return chargeLevel;
    }

    @Override
    public String toString() {
        return "BatteryStatus: {\n"
                + "  availability: " + availability.toString() + ",\n"
                + "  availability: " + availability.toString() + ",\n"
                + "  chargeLevel: " + chargeLevel + "\n"
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BatteryStatusResult that = (BatteryStatusResult) o;
        return availability == that.availability &&
                batteryStatus == that.batteryStatus &&
                chargeLevel == that.chargeLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availability, batteryStatus, chargeLevel);
    }
}
