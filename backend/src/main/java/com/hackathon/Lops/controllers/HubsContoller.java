package com.hackathon.Lops.controllers;

import com.hackathon.Lops.beans.HubDetails;
import com.hackathon.Lops.services.HubDetallsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/hubs")
@CrossOrigin("http://localhost:4200")
public class HubsContoller {
    @Autowired
    HubDetallsService hubDetallsService;


    @GetMapping("/{hubId}")
    public ResponseEntity<Object> getHubById(@PathVariable String hubId) {
        return hubDetallsService.getHubById(hubId);
    }

    @GetMapping
    public ResponseEntity<List<HubDetails>> getAllubs() {
        return hubDetallsService.getAllHubs();
    }

    @PostMapping
    public ResponseEntity<String> createHub(@RequestBody HubDetails hubDetails) {
        return hubDetallsService.createHub(hubDetails);
    }

    @PutMapping
    public ResponseEntity<String> updateHub(@RequestBody HubDetails updatedHub) {
        return hubDetallsService.updateHub(updatedHub);
    }

    @DeleteMapping("/{hubId}")
    public ResponseEntity<String> deleteHub(@PathVariable String hubId) {
        return hubDetallsService.deleteHub(hubId);
    }
}
