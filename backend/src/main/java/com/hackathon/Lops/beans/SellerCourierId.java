package com.hackathon.Lops.beans;

import lombok.Data;

@Data
public class SellerCourierId {

    String sellerId;

    String courierId;


    SellerCourierId(){

    }

    public SellerCourierId(String sellerId, String courierId){
        this.sellerId =sellerId;
        this.courierId = courierId;
    }
}
