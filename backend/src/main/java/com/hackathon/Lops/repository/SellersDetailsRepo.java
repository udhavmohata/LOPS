package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.SellerDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellersDetailsRepo extends MongoRepository<SellerDetails, String> {
    SellerDetails findBySellerId(String sellerId);
}
