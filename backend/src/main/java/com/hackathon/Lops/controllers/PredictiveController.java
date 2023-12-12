package com.hackathon.Lops.controllers;

import com.hackathon.Lops.models.CourierPredictiveDTO;
import com.hackathon.Lops.services.PredictiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/api")
@CrossOrigin("http://localhost:4200")
public class PredictiveController {

    @Autowired
    PredictiveService predictiveService;

    @PostMapping("/predict-courier")
    public ResponseEntity<?> predictBestCourier(@RequestBody CourierPredictiveDTO courierPredictiveDTO) {
        return ResponseEntity.ok(predictiveService.predictBestCourier(courierPredictiveDTO));
    }
}
