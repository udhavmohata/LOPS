package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.ShipmentHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentHistoryRepo extends MongoRepository<ShipmentHistory, String> {
    ShipmentHistory findOneBySellerIdAndShipmentIdAndStatusAndHubId(String sellerId,
                                                                    String shipmentId,
                                                                    String status,
                                                                    String hubId);


    List<ShipmentHistory> findByShipmentId(String shipmentId);
    List<ShipmentHistory> findAllBySellerIdAndShipmentId(String sellerId, String shipmentId);

    List<ShipmentHistory> findByShipmentIdAndStatus(String shipmentId,String status);

}
