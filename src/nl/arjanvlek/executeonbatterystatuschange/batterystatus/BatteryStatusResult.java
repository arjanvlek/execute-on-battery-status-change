package nl.arjanvlek.executeonbatterystatuschange.batterystatus;

import java.util.Objects;

import nl.arjanvlek.executeonbatterystatuschange.wmimodels.Availability;
import nl.arjanvlek.executeonbatterystatuschange.wmimodels.BatteryStatus;

public class BatteryStatusResult {

    private final Availability availability;
    private final BatteryStatus batteryStatus;

    public BatteryStatusResult(Availability availability, BatteryStatus batteryStatus) {
        if (availability == null || batteryStatus == null) {
            throw new IllegalArgumentException("availability and batteryStatus cannot be null!");
        }

        this.availability = availability;
        this.batteryStatus = batteryStatus;
    }

    public Availability getAvailability() {
        return availability;
    }

    public BatteryStatus getBatteryStatus() {
        return batteryStatus;
    }

    @Override
    public String toString() {
        return "BatteryStatus: {\n"
                + "  availability: " + availability.toString() + ",\n"
                + "  batteryStatus: " + batteryStatus.toString() + "\n"
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
                batteryStatus == that.batteryStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availability, batteryStatus);
    }
}
