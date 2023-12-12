package com.hackathon.Lops.controllers;

import com.hackathon.Lops.beans.AggregatedDetails;
import com.hackathon.Lops.dto.HistoryRequestDTO;
import com.hackathon.Lops.models.AggregatedCourierData;
import com.hackathon.Lops.models.CourierTrendResponse;
import com.hackathon.Lops.models.TrendRequestDTO;
import com.hackathon.Lops.services.AggregatedDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/aggregated-details")
@CrossOrigin("http://localhost:4200")
public class AggregatedDetailsController {

    @Autowired
    AggregatedDetailsService aggregatedDetailsService;

    @PostMapping("/create")
    public void addAggregatedDetails(@RequestBody AggregatedDetails aggregatedDetails){
        aggregatedDetailsService.addNewAggregated(aggregatedDetails);
    }

    @PostMapping(value = "/trend/seller-courier", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AggregatedCourierData>> sellerCourierTrendDetails(@RequestBody HistoryRequestDTO trendRequestDTO) {
        return ResponseEntity.ok(aggregatedDetailsService.sellerCourierTrendDetails(trendRequestDTO));
    }

    @PostMapping(value = "/trend/seller", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourierTrendResponse>> sellerTrendDetails(@RequestBody HistoryRequestDTO trendRequestDTO) {
        return ResponseEntity.ok(aggregatedDetailsService.sellerTrendDetails(trendRequestDTO));
    }
}
