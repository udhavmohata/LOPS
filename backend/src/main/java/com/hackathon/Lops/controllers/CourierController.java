package com.hackathon.Lops.controllers;

import com.hackathon.Lops.beans.CourierDetails;
import com.hackathon.Lops.services.CourierDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/courier")
@CrossOrigin("http://localhost:4200")
public class CourierController {
    @Autowired
    CourierDetailsService courierDetailsService;

    @GetMapping(value = "/{courierId}")
    public ResponseEntity<Object> getCourierById(@PathVariable(value = "courierId")
                                                 String courierId){
        return courierDetailsService.fetchCourier(courierId);
    }
    @GetMapping
    public ResponseEntity<List<CourierDetails>> getAllCourier(){
        return courierDetailsService.fetchAllCourier();
    }

    @PostMapping
    public ResponseEntity<String> addCourier(@RequestBody CourierDetails courierDetails){
        return courierDetailsService.addNewCourier(courierDetails);
    }

    @PutMapping
    public ResponseEntity<String> updateCourier(@RequestBody CourierDetails courierDetails){
        return courierDetailsService.updateExistingCourier(courierDetails);
    }


    @DeleteMapping(value = "/{courierId}")
    public ResponseEntity<String> deleteCourierById(@PathVariable(value = "courierId")
                                                 String courierId){
        return courierDetailsService.deleteCourier(courierId);
    }

    @GetMapping(value = "/seller/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourierDetails>> fechBySellerId(@PathVariable String sellerId){
        return courierDetailsService.getCourierDetailsBySellerId(sellerId);
    }

}
