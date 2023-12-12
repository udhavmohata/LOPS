package com.hackathon.Lops.controllers;

import com.hackathon.Lops.beans.Pincodes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/pincodes")
@CrossOrigin("http://localhost:4200")
public class PincodeController {
    @GetMapping
    public ResponseEntity<List<String>> getAllPindoes(){
        List<String> pincodesList = Arrays.asList("560001","560066", "122003",
                "12204","560016");
        return ResponseEntity.ok().body(pincodesList);
    }

}
