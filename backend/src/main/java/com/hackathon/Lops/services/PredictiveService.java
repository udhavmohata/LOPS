package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.CourierDetails;
import com.hackathon.Lops.beans.SellerAlertConfig;
import com.hackathon.Lops.beans.SellerCourierId;
import com.hackathon.Lops.models.CourierPredictiveDTO;
import com.hackathon.Lops.repository.CourierDetailsRepo;
import com.hackathon.Lops.repository.SellerAlertConfigRepository;
import com.hackathon.Lops.repository.ShipmentHistoryCustomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PredictiveService {

    private static final String DELIVERED = "DELIVERED";

    @Autowired
    ShipmentHistoryCustomRepo shipmentHistoryCustomRepo;

    @Autowired
    CourierDetailsRepo courierDetailsRepo;

    @Autowired
    SellerAlertConfigRepository sellerAlertConfigRepository;

    public List<Object> predictBestCourier(CourierPredictiveDTO courierDTO) {
        double timeDelivery = 0;
        double damageLostWeight = 0;
        Map<String, Double> scoreCourierMap = new HashMap<>();
        List<CourierDetails> courierDetails = courierDetailsRepo.findAllBySellerId(courierDTO.getSellerId());
        Map<String,CourierDetails> courierDetailsMap =new HashMap<>();
        for (CourierDetails courierDetail : courierDetails) {
            Optional<SellerAlertConfig> alertConfig = sellerAlertConfigRepository.findById(new SellerCourierId(courierDTO.getSellerId(), courierDetail.getCourierId()));
            courierDetailsMap.put(courierDetail.getCourierId(),courierDetail);
            if(alertConfig.isPresent() && !alertConfig.get().getActive()) {
                scoreCourierMap.put(courierDetail.getCourierId(), 0D);
                continue;
            }
            if (timeDelayCheck(courierDTO, courierDetail.getCourierId())) {
                timeDelivery = courierDTO.getTimeDeliveryWeight();
            }
            if(shipmentHistoryCustomRepo.countOfLostDamaged(courierDTO.getSellerId(), courierDTO.getFromPincode(), courierDTO.getToPincode(), courierDetail.getCourierId()) <= 0) {
                damageLostWeight = courierDTO.getDamageLostWeight();
            }
            scoreCourierMap.put(courierDetail.getCourierId(), timeDelivery + damageLostWeight);
        }
        List<String> sortedCourierList= getSortedKeysByValueDescending(scoreCourierMap);

        List<Object> response =new ArrayList<>();

        for (String courierId:sortedCourierList){
            response.add(courierDetailsMap.get(courierId));
        }
        return response;
    }

    private boolean timeDelayCheck(CourierPredictiveDTO courierDTO, String courierId) {
        long delayCount = shipmentHistoryCustomRepo.countOfSellerFromToPincodeOfStatus(courierDTO.getSellerId(), courierDTO.getFromPincode(), courierDTO.getToPincode(), DELIVERED, courierId, true);
        long nonDelayCount = shipmentHistoryCustomRepo.countOfSellerFromToPincodeOfStatus(courierDTO.getSellerId(), courierDTO.getFromPincode(), courierDTO.getToPincode(), DELIVERED, courierId, false);
        return (double) delayCount /(delayCount+nonDelayCount) < 0.1;

    }

    public List<String> getSortedKeysByValueDescending(Map<String, Double> unsortedMap) {
        return unsortedMap.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
