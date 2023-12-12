package com.hackathon.Lops.controllers;


import com.hackathon.Lops.beans.DelayedShipments;
import com.hackathon.Lops.beans.ShipmentHistory;
import com.hackathon.Lops.dto.HistoryGroupDTO;
import com.hackathon.Lops.dto.HistoryRequestDTO;
import com.hackathon.Lops.dto.HistoryResponseDTO;
import com.hackathon.Lops.dto.HubRequestDTO;
import com.hackathon.Lops.dto.HubResponseDTO;
import com.hackathon.Lops.dto.ResponseDTO;
import com.hackathon.Lops.services.ShipmentHistoryDetailsService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shipmentHistory")
@CrossOrigin("http://localhost:4200")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShipmentHistoryController {

    @Autowired
    ShipmentHistoryDetailsService shipmentHistoryDetailsService;

    @PostMapping
    public ResponseEntity<String> createShipmentHistory(@RequestBody List<ShipmentHistory> shipmentHistoryList){
        return shipmentHistoryDetailsService.createShipmentHistory(shipmentHistoryList);
    }

    @PostMapping(value = "/fetch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResponseDTO> getShipmentHistory(@RequestBody HistoryRequestDTO historyRequestDTO){
        return shipmentHistoryDetailsService.getHistory(historyRequestDTO);
    }
    @PostMapping(value = "/hub-performance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HubResponseDTO> getHubPerformance(@RequestBody HubRequestDTO hubRequestDTO){
        return shipmentHistoryDetailsService.getHubPerformance(hubRequestDTO);
    }
    @GetMapping(value = "/shipment-hub-status/{sellerId}/{shipmentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getShipmentHubStatus(@PathVariable(value = "sellerId") String sellerId,
                                             @PathVariable(value = "shipmentId") String shipmentId){
        return shipmentHistoryDetailsService.getShipmentHubStatus(sellerId, shipmentId);
    }
    @PostMapping(value = "/delayed-status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DelayedShipments> getDelayedShipments(@RequestBody Map<String, String> map){
        return shipmentHistoryDetailsService.getDelayedShipments(map.get("fromDate"), map.get("toDate"));
    }

}
