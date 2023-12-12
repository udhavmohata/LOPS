package com.hackathon.Lops.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourierPredictiveDTO {
    String sellerId;
    Long fromPincode;
    Long toPincode;
    double timeDeliveryWeight = 4;
    double damageLostWeight = 5;
    double successDeliveredUpdateWeight = 2;
}
