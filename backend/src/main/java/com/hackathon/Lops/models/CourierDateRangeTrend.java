package com.hackathon.Lops.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourierDateRangeTrend {
    LocalDateTime aggregatedDateTime;
    Long totalShipments;
    Long delayedShipments;
    Map<String, Long> reasons;
}
