package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.CourierDetails;
import com.hackathon.Lops.beans.SellerDetails;
import com.hackathon.Lops.repository.CourierDetailsRepo;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourierDetailsService {
    @Autowired
    CourierDetailsRepo courierDetailsRepo;
    public ResponseEntity<String> addNewCourier(CourierDetails courierDetails) {
        if (StringUtils.hasLength(courierDetails.getId()) && courierDetailsRepo.findOneByCourierId(courierDetails.getId()).isPresent()){
            return ResponseEntity.badRequest().body("Courier Already Exists");
        }else {
            courierDetailsRepo.save(courierDetails);
            return ResponseEntity.ok().body("Done");
        }
    }

    public ResponseEntity<String> updateExistingCourier(CourierDetails courierDetails) {
        Optional<CourierDetails> optionalCourierDetails = courierDetailsRepo.findOneByCourierId(courierDetails.getId());
        if (optionalCourierDetails.isPresent()){
            CourierDetails existingDetails =optionalCourierDetails.get();
            existingDetails.setCourierName(courierDetails.getCourierName());
            courierDetailsRepo.save(existingDetails);
            return ResponseEntity.ok().body("Updated");
        }else {
            return ResponseEntity.badRequest().body("Courier doesn't Exists");
        }
    }

    public ResponseEntity<Object> fetchCourier(String courierId) {
        Optional<CourierDetails> courierDetails = courierDetailsRepo.findOneByCourierId(courierId);
        if (courierDetails.isPresent()){
            return ResponseEntity.ok().body(courierDetails.get());
        }else {
            return ResponseEntity.badRequest().body("Courier doesn't Exists");
        }
    }

    public ResponseEntity<String> deleteCourier(String courierId) {
        if (courierDetailsRepo.findOneByCourierId(courierId).isPresent()){
            courierDetailsRepo.deleteById(courierId);
            return ResponseEntity.ok().body("Done");
        }else {
            return ResponseEntity.badRequest().body("Courier Not Found");
        }
    }

    public ResponseEntity<List<CourierDetails>> fetchAllCourier() {
        return ResponseEntity.ok().body(courierDetailsRepo.findAll());
    }

    public ResponseEntity<List<CourierDetails>> getCourierDetailsBySellerId(String sellerId) {
        List<CourierDetails> courierDetailsList = courierDetailsRepo.findAllBySellerId(sellerId);
        return ResponseEntity.ok().body(courierDetailsList);
    }
}
