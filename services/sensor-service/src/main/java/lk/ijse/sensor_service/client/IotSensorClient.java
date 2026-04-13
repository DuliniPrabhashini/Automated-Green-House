package lk.ijse.sensor_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "external-iot-api", url = "${iot.api.url}")
public interface IotSensorClient {

    @PostMapping("/auth/login")
    Map<String, String> login(@RequestBody Map<String, String> credentials);

    @GetMapping("/devices")
    List<Map<String, Object>> getAllDevices(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/devices/telemetry/{deviceId}")
    Map<String, Object> getDeviceTelemetry(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable String deviceId
    );
}