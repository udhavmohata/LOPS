package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.CourierDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierDetailsRepo extends MongoRepository<CourierDetails, String> {
    boolean existsById(String courierId);

    Optional<CourierDetails> findOneByCourierId(String courierId);

    List<CourierDetails> findAllByCourierIdIn(List<String> courierIds);

    CourierDetails getById(String courierId);

    void deleteById(String courierId);

    List<CourierDetails> findAllBySellerId(String sellerId);

}
