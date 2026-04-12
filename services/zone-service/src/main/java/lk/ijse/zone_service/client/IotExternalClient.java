package lk.ijse.zone_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "external-iot-api", url = "${iot.api.url}")
public interface IotExternalClient {

    @PostMapping("/auth/login")
    Map<String, String> login(@RequestBody Map<String, String> credentials);

    @PostMapping("/devices")
    Map<String, Object> registerDevice(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody Map<String, String> devicePayload
    );
}