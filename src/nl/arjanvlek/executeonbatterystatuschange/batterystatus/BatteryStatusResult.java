package nl.arjanvlek.executeonbatterystatuschange.batterystatus;

import java.util.Objects;

import nl.arjanvlek.executeonbatterystatuschange.wmimodels.Availability;
import nl.arjanvlek.executeonbatterystatuschange.wmimodels.BatteryStatus;

public class BatteryStatusResult {

    private final Availability availability;
    private final BatteryStatus batteryStatus;
    private final int chargeLevel;
    private final double voltage;

    public BatteryStatusResult(Availability availability, BatteryStatus batteryStatus, int chargeLevel, double voltage) {
        if (availability == null || batteryStatus == null) {
            throw new IllegalArgumentException("availability and batteryStatus cannot be null!");
        }

        this.availability = availability;
        this.batteryStatus = batteryStatus;
        this.chargeLevel = chargeLevel;
        this.voltage = voltage;
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

    public double getVoltage() {
        return voltage;
    }

    @Override
    public String toString() {
        return "BatteryStatus: {\n"
                + "  availability: " + availability.toString() + ",\n"
                + "  batteryStatus: " + batteryStatus.toString() + ",\n"
                + "  chargeLevel: " + chargeLevel + ",\n"
                + "  voltage: " + voltage + "\n"
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
                chargeLevel == that.chargeLevel &&
                voltage == that.voltage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availability, batteryStatus, chargeLevel, voltage);
    }
}
