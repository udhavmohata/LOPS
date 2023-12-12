package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.StoreDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreDetailsRepo extends MongoRepository<StoreDetails, String> {
}
