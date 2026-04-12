package lk.ijse.zone_service.service;


import lk.ijse.zone_service.client.IotExternalClient;
import lk.ijse.zone_service.entity.Zone;
import lk.ijse.zone_service.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ZoneService {
    private final ZoneRepository zoneRepository;
    private final IotExternalClient iotClient;

    @Value("${iot.api.username}")
    private String iotUsername;

    @Value("${iot.api.password}")
    private String iotPassword;

    public Zone createZone(Zone zoneRequest) {
        if (zoneRepository.findByName(zoneRequest.getName()).isPresent()) {
            throw new IllegalArgumentException("A Zone with the name '" + zoneRequest.getName() + "' already exists.");
        }

        if (zoneRequest.getMinTemp() >= zoneRequest.getMaxTemp()) {
            throw new IllegalArgumentException("Minimum temperature must be strictly less than maximum temperature.");
        }

        try {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", iotUsername);
            credentials.put("password", iotPassword);

            Map<String, String> authResponse = iotClient.login(credentials);
            String token = "Bearer " + authResponse.get("accessToken");

            Map<String, String> devicePayload = new HashMap<>();
            devicePayload.put("name", zoneRequest.getName() + "-Sensor");
            devicePayload.put("zoneId", zoneRequest.getName());

            Map<String, Object> deviceResponse = iotClient.registerDevice(token, devicePayload);

            String externalDeviceId = (String) deviceResponse.get("deviceId");
            zoneRequest.setDeviceId(externalDeviceId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to External IoT API: " + e.getMessage());
        }

        return zoneRepository.save(zoneRequest);
    }

    public Zone getZone(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found with ID: " + id));
    }

    public Zone getZoneByName(String name) {
        return zoneRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found with Name: " + name));
    }

    public Zone updateZone(Long id, Double minTemp, Double maxTemp) {
        if (minTemp >= maxTemp) {
            throw new IllegalArgumentException("Minimum temperature must be strictly less than maximum temperature.");
        }
        Zone zone = getZone(id);
        zone.setMinTemp(minTemp);
        zone.setMaxTemp(maxTemp);
        return zoneRepository.save(zone);
    }

    public void deleteZone(Long id) {
        zoneRepository.deleteById(id);
    }
}