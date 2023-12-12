package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.AggregatedDetails;
import com.hackathon.Lops.beans.AggregatedDetailsId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregatedDetailsRepo extends MongoRepository<AggregatedDetails, AggregatedDetailsId> {

}
