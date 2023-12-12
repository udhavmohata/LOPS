package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.HubDetails;
import com.hackathon.Lops.repository.HubDetailsRepo;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HubDetallsService {
    @Autowired
    HubDetailsRepo hubDetailsRepo;
    public ResponseEntity<Object> getHubById(String hubId) {
        HubDetails hubDetails = hubDetailsRepo.getById(hubId);
        if (!ObjectUtils.isEmpty(hubDetails)){
            return ResponseEntity.ok().body(hubDetails);
        }else {
            return ResponseEntity.badRequest().body("hub don't exists");
        }
    }

    public ResponseEntity<String> createHub(HubDetails hubDetails) {
        if (hubDetailsRepo.existsById(hubDetails.getId())){
            return ResponseEntity.badRequest().body("hub already exists");
        }else {
            hubDetailsRepo.save(hubDetails);
            return ResponseEntity.ok().body("ok");
        }
    }

    public ResponseEntity<String> deleteHub(String hubId) {
        if (hubDetailsRepo.existsById(hubId)){
            hubDetailsRepo.deleteById(hubId);
            return ResponseEntity.ok().body("done");
        }else {
            return ResponseEntity.ok().body("hub doesn't exists");
        }
    }

    public ResponseEntity<String> updateHub(HubDetails updatedHub) {
        HubDetails existingHubDetails = hubDetailsRepo.getById(updatedHub.getId());
        if (ObjectUtils.isEmpty(existingHubDetails)){
            return ResponseEntity.badRequest().body("hub don't exists");
        }else {
            existingHubDetails.setHubName(updatedHub.getHubName());
            hubDetailsRepo.save(existingHubDetails);
            return ResponseEntity.ok().body("done");
        }
    }

    public ResponseEntity<List<HubDetails>> getAllHubs() {
        return ResponseEntity.ok().body(hubDetailsRepo.findAll());
    }
}
