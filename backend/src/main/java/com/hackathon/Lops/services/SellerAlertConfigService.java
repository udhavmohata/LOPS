package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.SellerAlertConfig;
import com.hackathon.Lops.beans.SellerCourierId;
import com.hackathon.Lops.repository.SellerAlertConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SellerAlertConfigService {

    @Autowired
    private SellerAlertConfigRepository sellerAlertConfigRepository;

    public SellerAlertConfig getSellerAlertConfigBySellerIdAndCourierId(String sellerId, String courierId){
        return sellerAlertConfigRepository.findByIdAndActive(new SellerCourierId(sellerId,courierId),true).get();
    }

    public SellerAlertConfig createSellerCourierAlert(SellerAlertConfig sellerAlertConfig){
        sellerAlertConfig.setUpdatedAt(LocalDateTime.now());
        return sellerAlertConfigRepository.save(sellerAlertConfig);
    }

    public SellerAlertConfig getSellerAlertDetails(String sellerId,String courierId){
        if(sellerId!=null && courierId!=null){
            return sellerAlertConfigRepository.findById(new SellerCourierId(sellerId,courierId)).get();
        }
        return null;
    }

    public SellerAlertConfig updateAlert(SellerAlertConfig sellerAlertConfig){
        return sellerAlertConfigRepository.save(sellerAlertConfig);
    }
}
