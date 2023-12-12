package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.SchedulerCombinationDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface SchedulerCombinationRepo extends MongoRepository<SchedulerCombinationDto, String> {
    //boolean existsBySellerId(String sellerId);

    boolean existsBySellerIdAndFromStatusAndToStatus(String sellerId, String picked, String delivered);

    boolean existsBySellerIdAndFromStatusAndToStatusAndFromPinCodeAndToPinCodeAndFromDateAndToDate(String sellerId,
                                                                                                   String fromStatus,
                                                                                                   String toStatus,
                                                                                                   String fromPinCode,
                                                                                                   String toPinCode,
                                                                                                   String fromDate,
                                                                                                   String toDate);
}
