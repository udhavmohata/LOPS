package com.hackathon.Lops.beans;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
@Document(collection = "shipment_Details")
public class ShipmentHistory {

    @MongoId
    String id;
    String shipmentId;
    String reasonId;
    String status;
    Long fromPinCode;
    Long toPinCode;
    String sellerId;
    String storeId;
    String comment;
    String hubId;
    String shouldBeChangedTo;
    boolean completed;
    LocalDateTime shouldBeChangedBy;
    LocalDateTime expectedCreateDate;
    LocalDateTime actualCreateDate;
    Double cost;
    String courierId;
    boolean delayed;
    String awb;

}
