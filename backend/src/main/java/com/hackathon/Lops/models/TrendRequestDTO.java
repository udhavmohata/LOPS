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
public class TrendRequestDTO {
    String sellerId;
    String courierId;
    String fromStatus;
    String toStatus;
    String fromDate;
    String toDate;
    Long fromPincode;
    Long toPincode;
}
