package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.ReasonCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasonCodeRepo extends MongoRepository<ReasonCode, String> {
}
