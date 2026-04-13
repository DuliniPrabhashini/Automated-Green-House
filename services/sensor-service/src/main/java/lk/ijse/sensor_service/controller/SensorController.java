package lk.ijse.sensor_service.controller;

import lk.ijse.sensor_service.service.TelemetrySchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {
    private final TelemetrySchedulerService telemetryService;

    @GetMapping("/latest")
    public ResponseEntity<Map<String, Object>> getLatestSensorData() {
        return ResponseEntity.ok(telemetryService.getLatestReading());
    }
}