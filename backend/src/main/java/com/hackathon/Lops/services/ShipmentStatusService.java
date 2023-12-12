package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.ShipmentStatus;
import com.hackathon.Lops.repository.ShipmentStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentStatusService {
    @Autowired
    ShipmentStatusRepo shipmentStatusRepo;

    public ResponseEntity<String> addNewStatus(List<ShipmentStatus> shipmentStatusList) {
        shipmentStatusRepo.saveAll(shipmentStatusList);
        return ResponseEntity.ok().body("done");
    }
}
