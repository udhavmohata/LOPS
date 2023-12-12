package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.AggregatedDetails;
import com.hackathon.Lops.beans.AggregatedDetailsId;
import com.hackathon.Lops.dto.HistoryRequestDTO;
import com.hackathon.Lops.models.AggregatedCourierData;
import com.hackathon.Lops.models.CourierDateRangeTrend;
import com.hackathon.Lops.models.CourierTrendResponse;
import com.hackathon.Lops.models.TrendRequestDTO;
import com.hackathon.Lops.repository.AggregatedDetailsRepo;
import com.hackathon.Lops.repository.AggregatedDetailsCustomRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AggregatedDetailsService {


    @Autowired
    AggregatedDetailsRepo aggregatedDetailsRepo;

    @Autowired
    AggregatedDetailsCustomRepo aggregatedDetailsCustomRepo;


    public void addNewAggregated(AggregatedDetails aggregatedDetails) {
        aggregatedDetailsRepo.save(aggregatedDetails);
    }

    public List<AggregatedCourierData> sellerCourierTrendDetails(HistoryRequestDTO trendRequestDTO) {
        List<AggregatedDetails> aggregatedDetails = aggregatedDetailsCustomRepo.findBySellerTenantFromToStatusDate(trendRequestDTO);
        return getSellerCourierTrendDetails(aggregatedDetails,trendRequestDTO);
    }

    public List<CourierTrendResponse> sellerTrendDetails(HistoryRequestDTO trendRequestDTO) {

        List<AggregatedDetails> aggregatedDetails = aggregatedDetailsCustomRepo.findBySellerTenantFromToStatusDate(trendRequestDTO);
        List<CourierTrendResponse> courierTrendResponseList = new ArrayList<>();
        String statusKey = trendRequestDTO.getFromStatus() + "To" + trendRequestDTO.getToStatus();

        aggregatedDetails.forEach(aggregatedDetails1 -> {
            if(aggregatedDetails1.getFromStatusToStatus().get(statusKey)!=null) {
                List<AggregatedCourierData> aggregatedCourierData = aggregatedDetails1.getFromStatusToStatus().get(statusKey).stream().collect(Collectors.toList());

                Map<String, List<AggregatedCourierData>> courierAggMap = aggregatedCourierData.stream()
                        .collect(Collectors.groupingBy(AggregatedCourierData::getCourierId));
               courierTrendResponseList.addAll( getSellerTradeDetails(courierAggMap, trendRequestDTO,aggregatedDetails1.getId()));
            }
        });

        Map<String, List<CourierTrendResponse>> responseMap = courierTrendResponseList.stream()
                .collect(Collectors.groupingBy(CourierTrendResponse::getCourierId));

        List<CourierTrendResponse> responseList = new ArrayList<>();
        responseMap.forEach((courierId, courierTrendResponses) -> {
            CourierTrendResponse response = new CourierTrendResponse();
            response.setCourierId(courierId);
            response.setDateRange(courierTrendResponses.stream().map(CourierTrendResponse::getDateRange).flatMap(Collection::stream).toList());
            responseList.add(response);
        });

        return responseList;
    }

    private List<AggregatedCourierData> getSellerCourierTrendDetails(List<AggregatedDetails> aggregatedDetails, HistoryRequestDTO trendRequest) {
        String statusKey = trendRequest.getFromStatus() + "To" + trendRequest.getFromStatus();
        List<AggregatedCourierData> aggregatedCourierData = aggregatedDetails.stream()
                .filter(ad -> ad.getFromStatusToStatus().containsKey(statusKey))
                .map(ad -> ad.getFromStatusToStatus().get(statusKey))
                .flatMap(Collection::stream).toList();

        Map<String, List<AggregatedCourierData>> courierAggMap = aggregatedCourierData.stream()
                .collect(Collectors.groupingBy(AggregatedCourierData::getCourierId));

       return getAggregatedCourierData(courierAggMap);
    }

    private List<AggregatedCourierData>  getAggregatedCourierData(Map<String, List<AggregatedCourierData>> aggregatedDetails) {

        List<AggregatedCourierData> aggregatedCourierData = new ArrayList<>();
        aggregatedDetails.values().forEach(aggregatedCourierDataList -> {
            AggregatedCourierData courierData =new AggregatedCourierData();
            courierData.setTotalShipments(0L);
            courierData.setDelayedShipments(0L);
            courierData.setReasons(new HashMap<>());
            aggregatedCourierDataList.forEach(aggregatedCourierData1 -> {
                courierData.setTotalShipments(courierData.getTotalShipments()+aggregatedCourierData1.getTotalShipments());
                courierData.setDelayedShipments(courierData.getDelayedShipments()+aggregatedCourierData1.getDelayedShipments());
                courierData.setCourierId(aggregatedCourierData1.getCourierId());
                aggregatedCourierData1.getReasons().forEach((reasonName,reasonId)->{
                    if(!courierData.getReasons().containsKey(reasonName)){
                        courierData.getReasons().put(reasonName,reasonId);
                    }
                });
            });
            aggregatedCourierData.add(courierData);
        });

        return aggregatedCourierData;
    }


    private List<CourierTrendResponse> getSellerTradeDetails( Map<String, List<AggregatedCourierData>> courierAggMap , HistoryRequestDTO trendRequest, AggregatedDetailsId aggregatedDetailsId) {

        List<CourierTrendResponse>  courierTrendResponses = new ArrayList<>();
        courierAggMap.values().forEach(aggregatedCourierDataList -> {
            CourierTrendResponse courierData =new CourierTrendResponse();
            courierData.setDateRange(new ArrayList<>());
            aggregatedCourierDataList.forEach(aggregatedCourierData1 -> {
                CourierDateRangeTrend courierDateRangeTrend =new CourierDateRangeTrend();
                courierDateRangeTrend.setDelayedShipments(aggregatedCourierData1.getDelayedShipments());
                courierDateRangeTrend.setTotalShipments(aggregatedCourierData1.getTotalShipments());
                courierDateRangeTrend.setReasons(aggregatedCourierData1.getReasons());
                courierDateRangeTrend.setAggregatedDateTime(aggregatedDetailsId.getAggregatedDateTime());
                courierData.setCourierId(aggregatedCourierData1.getCourierId());
                courierData.getDateRange().add(courierDateRangeTrend);
            });
            courierTrendResponses.add(courierData);
        });

        return courierTrendResponses;
    }
}
