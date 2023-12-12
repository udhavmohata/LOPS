package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.SellerDetails;
import com.hackathon.Lops.repository.SellersDetailsRepo;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerDetailsService {
    @Autowired
    SellersDetailsRepo sellersDetailsRepo;
    public ResponseEntity<SellerDetails> getSellerDetails(String sellerId) {
        return ResponseEntity.ok().body(sellersDetailsRepo.findBySellerId(sellerId));
    }

    public ResponseEntity<String> createSeller(SellerDetails sellerDetails) {
        sellersDetailsRepo.save(sellerDetails);
        return ResponseEntity.ok().body("Done");
    }

    public ResponseEntity<List<SellerDetails>> fetchAllSeller(){
        return ResponseEntity.ok().body(sellersDetailsRepo.findAll());
    }
}
