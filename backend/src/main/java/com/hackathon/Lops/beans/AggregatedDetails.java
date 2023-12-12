package com.hackathon.Lops.beans;

import com.hackathon.Lops.models.AggregatedCourierData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
@Document(collection = "aggregated_details")
public class AggregatedDetails {

    @MongoId
    AggregatedDetailsId id;

    @Builder.Default
    Map<String, List<AggregatedCourierData>> fromStatusToStatus = new HashMap<>();

}
