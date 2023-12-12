package com.hackathon.Lops.beans;


import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
@Document(collection = "seller_details")
public class SellerDetails {
    @MongoId
    String id;
    String sellerName;
    String sellerId;
    Boolean enable;

    public SellerDetails(String sellerId) {
        this.sellerId = sellerId;
    }

}

