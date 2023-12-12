package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.CourierDetails;
import com.hackathon.Lops.beans.DelayedShipments;
import com.hackathon.Lops.beans.SellerAlertConfig;
import com.hackathon.Lops.beans.ShipmentHistory;
import com.hackathon.Lops.client.SlackClient;
import com.hackathon.Lops.enums.RedisOps;
import com.hackathon.Lops.redis.RedisHandler;
import com.hackathon.Lops.repository.CourierDetailsRepo;
import com.hackathon.Lops.repository.DelayedShipmentsRepo;
import com.hackathon.Lops.utill.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RedisEventListenerService {

    @Autowired
    private RedisHandler redisHandler;

    @Autowired
    private ShipmentHistoryDetailsService shipmentHistoryDetailsService;

    @Autowired
    private SellerAlertConfigService sellerAlertConfigService;

    @Autowired
    private SlackClient slackClient;

    @Autowired
    private CourierDetailsRepo courierDetailsRepo;
    @Autowired
    private DelayedShipmentsRepo delayedShipmentsRepo;

    LoggerUtil loggerUtil =LoggerUtil.getLogger(RedisEventListenerService.class);

    public boolean processRedisKeyExpireEvent(String key) throws Exception {

        String value = redisHandler.redisOps(RedisOps.GET,key);

        if(key!=null){

            List<ShipmentHistory> shipmentHistoryList = shipmentHistoryDetailsService.getShipmentDetailsByShipmentId(key);

            if(!CollectionUtils.isEmpty(shipmentHistoryList)){

                ShipmentHistory shipmentHistory =shipmentHistoryList.get(0);
                SellerAlertConfig sellerAlertConfig = sellerAlertConfigService.getSellerAlertConfigBySellerIdAndCourierId(shipmentHistory.getSellerId(),shipmentHistory.getCourierId());

                CourierDetails courierDetails=null;
                Optional<CourierDetails> optionalCourierDetails =courierDetailsRepo.findOneByCourierId(shipmentHistory.getCourierId());
                if(optionalCourierDetails.isPresent()){
                    courierDetails =optionalCourierDetails.get();
                }

                if(courierDetails==null){
                    courierDetails =new CourierDetails();
                    courierDetails.setCourierName(shipmentHistory.getCourierId());
                }
                String message = "Your Shipment *"+key+"* is delayed by courier partner *"+courierDetails.getCourierName()+"*" +
                        "and the reason is "+ shipmentHistory.getReasonId();
                delayedShipmentsRepo.save(DelayedShipments.builder()
                                .sellerId(shipmentHistory.getSellerId())
                                .shipmentId(shipmentHistory.getShipmentId())
                                .message(message)
                                .status(shipmentHistory.getStatus())
                                .sentTime(LocalDateTime.now())
                        .build());
                if(sellerAlertConfig!=null){
                    slackClient.sendMessageToSlack( message);

                    if(LocalDateTime.now().toLocalDate().compareTo(sellerAlertConfig.getUpdatedAt().toLocalDate())==0){


                        if(sellerAlertConfig.getAlertCountConfig()> sellerAlertConfig.getFailureCount()+1){
                            loggerUtil.info(" not reached threshold "+sellerAlertConfig.getFailureCount());
                        }else{
                            message = "Courier partner *"+courierDetails.getCourierName()+"* exceed threshold limit *"+sellerAlertConfig.getAlertCountConfig()+"*";

                            sellerAlertConfig.setActive(false);
                            slackClient.sendMessageToSlack(message);
                        }
                        sellerAlertConfig.setFailureCount(sellerAlertConfig.getFailureCount()+1);
                    }else{
                        sellerAlertConfig.setFailureCount(1l);
                        sellerAlertConfig.setActive(true);

                    }
                    sellerAlertConfig.setUpdatedAt(LocalDateTime.now());
                    sellerAlertConfigService.updateAlert(sellerAlertConfig);

                }else{
                    loggerUtil.info("Seller Alert Not configured for courier id "+shipmentHistory.getCourierId());
                }
            }else{
                loggerUtil.info("Shipment Details Not found for Shipment "+key);
            }

        }else{
            loggerUtil.info("Key Not found in Redis "+key);
        }

        return true;

    }
}
