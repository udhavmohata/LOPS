package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.ShipmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShipmentStatusRepo extends MongoRepository<ShipmentStatus, String> {
}
