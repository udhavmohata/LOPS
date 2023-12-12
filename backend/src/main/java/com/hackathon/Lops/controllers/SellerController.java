package com.hackathon.Lops.controllers;

import com.hackathon.Lops.beans.SellerDetails;
import com.hackathon.Lops.repository.SellersDetailsRepo;
import com.hackathon.Lops.services.SellerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/seller")
@CrossOrigin("http://localhost:4200")
public class SellerController {

    @Autowired
    SellerDetailsService sellerDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<SellerDetails> getSeller(@PathVariable String sellerId) {
        return sellerDetailsService.getSellerDetails(sellerId);
    }

    @PostMapping
    public ResponseEntity<String> createSeller(@RequestBody SellerDetails sellerDetails) {
        return sellerDetailsService.createSeller(sellerDetails);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SellerDetails>> getAllSeller() {
        return sellerDetailsService.fetchAllSeller();
    }

}
