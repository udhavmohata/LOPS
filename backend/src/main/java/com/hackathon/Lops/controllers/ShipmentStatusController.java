package com.hackathon.Lops.controllers;

import com.hackathon.Lops.beans.ShipmentStatus;
import com.hackathon.Lops.repository.ShipmentStatusRepo;
import com.hackathon.Lops.services.ShipmentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/status")
@CrossOrigin("http://localhost:4200")
public class ShipmentStatusController {

    @Autowired
    ShipmentStatusService shipmentStatusService;

    @Autowired
    ShipmentStatusRepo shipmentStatusRepo;

    @PostMapping
    public ResponseEntity<String> addStatus(@RequestBody List<ShipmentStatus> shipmentStatusList){
        return shipmentStatusService.addNewStatus(shipmentStatusList);
    }

    @GetMapping
    public ResponseEntity<List<ShipmentStatus>> fetchAllStatus(){
        return ResponseEntity.ok().body(shipmentStatusRepo.findAll());
    }
}
