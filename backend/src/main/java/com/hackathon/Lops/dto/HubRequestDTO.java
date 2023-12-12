package com.hackathon.Lops.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HubRequestDTO {
    String sellerId;
    String fromStatus;
    String toStatus;
    LocalDateTime fromDate;
    LocalDateTime toDate;
    Long fromPinCode;
    Long toPinCode;
}
