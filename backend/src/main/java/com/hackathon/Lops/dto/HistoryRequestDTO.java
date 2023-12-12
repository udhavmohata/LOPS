package com.hackathon.Lops.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryRequestDTO {
    String sellerId;
    String fromStatus;
    String toStatus;
    String fromDate;
    String toDate;
    Long fromPinCode;
    Long toPinCode;
}
