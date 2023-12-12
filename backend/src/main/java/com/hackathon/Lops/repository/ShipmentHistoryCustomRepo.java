package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.ShipmentHistory;
import com.hackathon.Lops.models.DelayCount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShipmentHistoryCustomRepo {

    private final MongoTemplate mongoTemplate;
    private static final String PARENT_ID = "_id";
    private static final String DOT = ".";
    private static final String DAMAGED = "DAMAGED";
    private static final String LOST = "LOST";

    public long countOfSellerFromToPincodeOfStatus(String sellerId, Long fromPin, Long toPin, String status, String courierId, boolean delayed) {

        Query query = new Query(Criteria.where(ShipmentHistory.Fields.sellerId).is(sellerId)
                .and(ShipmentHistory.Fields.fromPinCode).is(fromPin)
                .and(ShipmentHistory.Fields.toPinCode).is(toPin)
                .and(ShipmentHistory.Fields.status).is(status)
                .and(ShipmentHistory.Fields.courierId).is(courierId)
                .and(ShipmentHistory.Fields.delayed).is(delayed));
        return mongoTemplate.count(query, ShipmentHistory.class);
    }

    public long countOfLostDamaged(String sellerId, Long fromPin, Long toPin, String courierId) {

        Query query = new Query(Criteria.where(ShipmentHistory.Fields.sellerId).is(sellerId)
                .and(ShipmentHistory.Fields.fromPinCode).is(fromPin)
                .and(ShipmentHistory.Fields.toPinCode).is(toPin)
                .and(ShipmentHistory.Fields.courierId).is(courierId)
                .and(ShipmentHistory.Fields.status).in(DAMAGED, LOST));

        return mongoTemplate.count(query, ShipmentHistory.class);
    }
}
