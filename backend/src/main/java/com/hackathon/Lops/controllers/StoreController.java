package com.hackathon.Lops.controllers;


import com.hackathon.Lops.beans.StoreDetails;
import com.hackathon.Lops.services.StoreDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/store")
@CrossOrigin("http://localhost:4200")
public class StoreController {
    @Autowired
    StoreDetailsService storeDetailsService;

    @PostMapping
    public ResponseEntity<String> createStore(StoreDetails storeDetails){
        return storeDetailsService.createSeller(storeDetails);
    }

    @GetMapping
    public ResponseEntity<List<StoreDetails>> getAllStores(){
        return storeDetailsService.fetchAllStores();
    }

}
