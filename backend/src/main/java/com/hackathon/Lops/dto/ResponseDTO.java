package com.hackathon.Lops.dto;

import com.hackathon.Lops.beans.CourierDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseDTO {
    Long totalCount;
    Long delayedCount;
    CourierDetails courierDetails;
    Map<String, Long> reason;
}
