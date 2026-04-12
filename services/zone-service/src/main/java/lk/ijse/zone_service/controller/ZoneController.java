package lk.ijse.zone_service.controller;


import lk.ijse.zone_service.entity.Zone;
import lk.ijse.zone_service.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping
    public ResponseEntity<?> createZone(@RequestBody Zone zone) {
        try {
            Zone createdZone = zoneService.createZone(zone);
            return ResponseEntity.ok(createdZone);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getZone(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(zoneService.getZone(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateZone(@PathVariable Long id, @RequestParam Double minTemp, @RequestParam Double maxTemp) {
        try {
            return ResponseEntity.ok(zoneService.updateZone(id, minTemp, maxTemp));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.ok("Zone deleted successfully.");
    }

    @GetMapping("/{zoneId}/exists")
    public boolean checkZoneExists(@PathVariable Long zoneId) {
        try {
            zoneService.getZone(zoneId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Zone> getZoneByName(@PathVariable String name) {
        return ResponseEntity.ok(zoneService.getZoneByName(name));
    }
}