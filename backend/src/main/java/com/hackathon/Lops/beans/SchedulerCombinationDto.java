package com.hackathon.Lops.beans;

import com.hackathon.Lops.dto.HistoryRequestDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
@Document(collection = "scheduler_combination")
public class SchedulerCombinationDto {
    @MongoId
    String id;
    String fromStatus;
    String toStatus;
    String fromPinCode;
    String toPinCode;
    String fromDate;
    String toDate;
    String sellerId;

}
