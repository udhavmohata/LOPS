package com.hackathon.Lops.beans;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
@Document(collection = "courier_details")
public class CourierDetails {
    @MongoId
    String id;
    String courierId;
    String courierName;
    boolean enable;
    String sellerId;
    Long totalVotes;
    Long totalVoteCount;
    int numberOfStatus;
    double airCost;
    double waterCost;
    double landCost;
    double outerSpaceCost;

}
