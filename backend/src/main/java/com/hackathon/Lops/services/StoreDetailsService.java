package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.StoreDetails;
import com.hackathon.Lops.repository.StoreDetailsRepo;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class StoreDetailsService {
    @Autowired
    StoreDetailsRepo storeDetailsRepo;
    public ResponseEntity<String> createSeller(StoreDetails storeDetails) {
        storeDetailsRepo.save(storeDetails);
        return ResponseEntity.ok().body("done");
    }

    public ResponseEntity<List<StoreDetails>> fetchAllStores() {
        return ResponseEntity.ok().body(storeDetailsRepo.findAll());
    }
}
