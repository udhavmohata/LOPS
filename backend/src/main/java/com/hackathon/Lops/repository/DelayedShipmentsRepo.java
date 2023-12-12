package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.DelayedShipments;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DelayedShipmentsRepo extends MongoRepository<DelayedShipments, String> {
    List<DelayedShipments> findAllBySentTimeBetween(LocalDateTime from, LocalDateTime to);
}
