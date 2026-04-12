package lk.ijse.crop_server.controller;

import lk.ijse.crop_server.entity.Crop;
import lk.ijse.crop_server.entity.CropStatus;
import lk.ijse.crop_server.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/crops")
@RequiredArgsConstructor
public class CropController {
    private final CropService cropService;

    @PostMapping
    public ResponseEntity<?> registerBatch(@RequestParam Long zoneId, @RequestParam String name) {
        try {
            Crop savedCrop = cropService.registerCrop(zoneId, name);
            return ResponseEntity.ok(savedCrop);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Crop>> getInventory() {
        return ResponseEntity.ok(cropService.getAllCrops());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam CropStatus status) {
        try {
            Crop updatedCrop = cropService.updateCropStatus(id, status);
            return ResponseEntity.ok(updatedCrop);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}