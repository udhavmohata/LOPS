package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.SellerAlertConfig;
import com.hackathon.Lops.beans.SellerCourierId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerAlertConfigRepository extends MongoRepository<SellerAlertConfig, SellerCourierId> {

    Optional<SellerAlertConfig> findByIdAndActive(SellerCourierId sellerCourierId,Boolean active);

}
