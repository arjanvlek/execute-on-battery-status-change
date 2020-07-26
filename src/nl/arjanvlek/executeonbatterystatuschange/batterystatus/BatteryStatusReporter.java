package nl.arjanvlek.executeonbatterystatuschange.batterystatus;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import nl.arjanvlek.executeonbatterystatuschange.Logger;

public class BatteryStatusReporter {

    public static final String BATTERY_STATUS_URL = "http://192.168.178.40:8081/submit-client-status";

    public void reportBatteryStatus(int batteryPercentage, double voltage) {
        try {
            String jsonBody = "{\"batteryLevel\": " + batteryPercentage + ", \"batteryVoltage\": " + voltage + "}";
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(BATTERY_STATUS_URL).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (!scanner.hasNext()) {
                throw new RuntimeException("No output returned from server");
            }

            String response = scanner.next();

            if (response == null || !response.equals("{\"success\":true}")) {
                throw new RuntimeException("Operation was not successful, response was: " + response);
            }
        } catch (Exception e) {
            Logger.error("Error reporting battery status (" + batteryPercentage + "%)!", e);
        }
    }
}
