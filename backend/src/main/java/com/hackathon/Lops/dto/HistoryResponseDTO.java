package com.hackathon.Lops.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HistoryResponseDTO {
    Long totalCount;
    Long delayedCount;
    String courierId;
    Map<String, Long> reason;
}
