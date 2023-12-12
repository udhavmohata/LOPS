package com.hackathon.Lops.beans;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
@Document(collection = "seller_alert_config")
public class SellerAlertConfig {

    @MongoId
    SellerCourierId id;


    Long alertCountConfig;

    Long  failureCount;

    LocalDateTime updatedAt;

    Boolean active = true;

}
