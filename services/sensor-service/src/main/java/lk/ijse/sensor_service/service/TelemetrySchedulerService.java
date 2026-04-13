package lk.ijse.sensor_service.service;


import lk.ijse.sensor_service.client.AutomationClient;
import lk.ijse.sensor_service.client.IotSensorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetrySchedulerService {
    private final IotSensorClient iotSensorClient;
    private final AutomationClient automationClient;
    private Map<String, Object> latestReading;

    @Value("${iot.api.username}")
    private String iotUsername;

    @Value("${iot.api.password}")
    private String iotPassword;

    @Scheduled(fixedRate = 10000)
    public void fetchAndProcessTelemetry() {
        log.info("--- Waking up to fetch telemetry ---");

        try {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", iotUsername);
            credentials.put("password", iotPassword);

            Map<String, String> authResponse = iotSensorClient.login(credentials);
            String token = "Bearer " + authResponse.get("accessToken");

            List<Map<String, Object>> devices = iotSensorClient.getAllDevices(token);

            if (devices.isEmpty()) {
                log.info("No active devices found in the system yet.");
                return;
            }

            for (Map<String, Object> device : devices) {
                String deviceId = (String) device.get("deviceId");
                String zoneId = (String) device.get("zoneId");

                Map<String, Object> telemetry = iotSensorClient.getDeviceTelemetry(token, deviceId);

                Object sensorData = telemetry.get("value");

                log.info("Zone: {} | Device: {} | Live Data: {}", zoneId, deviceId, sensorData);

                Map<String, Object> sensorDataMap = (Map<String, Object>) sensorData;

                Map<String, Object> formattedPayload = new HashMap<>();
                formattedPayload.put("zoneId", zoneId);
                formattedPayload.put("temperature", sensorDataMap.get("temperature"));

                try {
                    automationClient.sendTelemetry(formattedPayload);
                    this.latestReading = formattedPayload;
                } catch (Exception e) {
                    log.error("Failed to reach Automation Service: {}", e.getMessage());
                }

            }

        } catch (Exception e) {
            log.error("Failed to fetch telemetry: {}", e.getMessage());
        }
    }

    public Map<String, Object> getLatestReading() {
        return this.latestReading != null ? this.latestReading : Map.of("message", "No data fetched yet");
    }
}