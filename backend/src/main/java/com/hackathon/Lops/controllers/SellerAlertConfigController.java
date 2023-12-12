package com.hackathon.Lops.controllers;

import com.hackathon.Lops.beans.SellerAlertConfig;
import com.hackathon.Lops.services.SellerAlertConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alert")
@CrossOrigin("http://localhost:4200")
public class SellerAlertConfigController {

    @Autowired
    private SellerAlertConfigService sellerAlertConfigService;

    @PostMapping("/create")
    public SellerAlertConfig createSellerAlert(@RequestBody SellerAlertConfig sellerAlertConfig){
        return sellerAlertConfigService.createSellerCourierAlert(sellerAlertConfig);
    }

    @GetMapping
    public SellerAlertConfig getAllAlertForSeller(@RequestParam("sellerId") String sellerId,@RequestParam(value = "courierId",required = true) String courierId){
        return sellerAlertConfigService.getSellerAlertDetails(sellerId,courierId);
    }

}
