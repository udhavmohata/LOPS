package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.HubDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HubDetailsRepo extends MongoRepository<HubDetails, String> {
    HubDetails getById(String hubId);
}
