package lk.ijse.crop_server.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ZONE-SERVICE")
public interface ZoneServiceClient {

    @GetMapping("/api/zones/{zoneId}/exists")
    boolean checkZoneExists(@PathVariable Long zoneId);

}