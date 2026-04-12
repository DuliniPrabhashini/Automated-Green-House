package lk.ijse.crop_server.service;

import lk.ijse.crop_server.client.ZoneServiceClient;
import lk.ijse.crop_server.entity.Crop;
import lk.ijse.crop_server.entity.CropStatus;
import lk.ijse.crop_server.repository.CropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CropService {

    private final CropRepository cropRepository;
    private final ZoneServiceClient zoneServiceClient;

    public Crop registerCrop(Long zoneId, String name) {

        boolean isZoneValid = zoneServiceClient.checkZoneExists(zoneId);

        if (!isZoneValid) {
            throw new IllegalArgumentException("Zone ID " + zoneId + " does not exist!");
        }

        Crop crop = new Crop();
        crop.setName(name);
        crop.setZoneId(zoneId);
        crop.setPlantDate(LocalDate.now());
        crop.setStatus(CropStatus.SEEDLING);

        return cropRepository.save(crop);
    }

    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    public Crop updateCropStatus(Long cropId, CropStatus newStatus) {
        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new IllegalArgumentException("Crop not found"));

        crop.setStatus(newStatus);
        return cropRepository.save(crop);
    }
}