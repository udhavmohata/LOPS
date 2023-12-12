package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.AggregatedDetails;
import com.hackathon.Lops.beans.AggregatedDetailsId;
import com.hackathon.Lops.beans.CourierDetails;
import com.hackathon.Lops.beans.DelayedShipments;
import com.hackathon.Lops.beans.ShipmentHistory;
import com.hackathon.Lops.dto.CourierTotalCountDTO;
import com.hackathon.Lops.dto.HistoryGroupDTO;
import com.hackathon.Lops.dto.HistoryRequestDTO;
import com.hackathon.Lops.dto.HistoryResponseDTO;
import com.hackathon.Lops.dto.HubGroupDTO;
import com.hackathon.Lops.dto.HubRequestDTO;
import com.hackathon.Lops.dto.HubResponseDTO;
import com.hackathon.Lops.dto.ResponseDTO;
import com.hackathon.Lops.enums.RedisOps;
import com.hackathon.Lops.models.AggregatedCourierData;
import com.hackathon.Lops.redis.RedisHandler;
import com.hackathon.Lops.repository.AggregatedDetailsRepo;
import com.hackathon.Lops.repository.CourierDetailsRepo;
import com.hackathon.Lops.repository.DelayedShipmentsRepo;
import com.hackathon.Lops.repository.ShipmentHistoryRepo;
import com.hackathon.Lops.utils.Utils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.hackathon.Lops.beans.ShipmentHistory.Fields.actualCreateDate;
import static com.hackathon.Lops.beans.ShipmentHistory.Fields.fromPinCode;
import static com.hackathon.Lops.beans.ShipmentHistory.Fields.hubId;
import static com.hackathon.Lops.beans.ShipmentHistory.Fields.shipmentId;
import static com.hackathon.Lops.beans.ShipmentHistory.Fields.status;
import static com.hackathon.Lops.beans.ShipmentHistory.Fields.toPinCode;
import static com.hackathon.Lops.utils.Utils.calculateMinutesDifference;
import static com.hackathon.Lops.utils.Utils.getParsedDate;
import static java.util.stream.Collectors.counting;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShipmentHistoryDetailsService {
    @Autowired
    ShipmentHistoryRepo shipmentHistoryRepo;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    AggregatedDetailsRepo aggregatedDetailsRepo;

    @Autowired
    CourierDetailsRepo courierDetailsRepo;

    @Autowired
    RedisHandler redisHandler;

    @Autowired
    DelayedShipmentsRepo delayedShipmentsRepo;

    public ResponseEntity<String> createShipmentHistory(List<ShipmentHistory> shipmentHistoryList) {
        updateToRedis(shipmentHistoryList);
        shipmentHistoryList.forEach(shipmentHistory -> {
            if (!ObjectUtils.isEmpty(shipmentHistory.getExpectedCreateDate()) && !ObjectUtils.isEmpty(shipmentHistory.getActualCreateDate()))
                shipmentHistory.setDelayed(shipmentHistory.getExpectedCreateDate().isBefore(shipmentHistory.getActualCreateDate()));
        });
        shipmentHistoryRepo.saveAll(shipmentHistoryList);
        return ResponseEntity.ok().body("Created");

    }

    private void updateToRedis(List<ShipmentHistory> shipmentHistoryList) {
        shipmentHistoryList.forEach(shipmentHistory -> {
            try {
                redisHandler.redisOps(RedisOps.DELETE, shipmentHistory.getStatus());
                if (!ObjectUtils.isEmpty(shipmentHistory.getShouldBeChangedTo()) && !ObjectUtils.isEmpty(shipmentHistory.getShouldBeChangedBy())) {
                    Long ttl = shipmentHistory.getShouldBeChangedBy().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() - Instant.now().toEpochMilli();
                    if (ttl > 0 && !shipmentHistory.isCompleted())
                        redisHandler.redisOps(RedisOps.SET_VALUE_WITH_TTL, shipmentHistory.getShipmentId(), shipmentHistory.getShouldBeChangedTo(), ttl, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public List<ShipmentHistory> getShipmentDetailsByShipmentId(String shipmentId) {
        return shipmentHistoryRepo.findByShipmentId(shipmentId);
    }

    public List<ShipmentHistory> getShipmentDetailsByShipmentIdAndStatus(String shipmentId, String status) {
        return shipmentHistoryRepo.findByShipmentIdAndStatus(shipmentId, status);
    }

    public List<ResponseDTO> getHistory(HistoryRequestDTO historyRequestDTO) {
        List<HistoryResponseDTO> historyResponseDTOS = getShipmentHistory(historyRequestDTO);
        List<CourierDetails> courierDetails = courierDetailsRepo.findAllByCourierIdIn(historyResponseDTOS.stream().map(HistoryResponseDTO::getCourierId)
                .filter(s -> !ObjectUtils.isEmpty(s)).collect(Collectors.toList()));
        Map<String, CourierDetails> courierMap = courierDetails.stream().collect(Collectors.toMap(CourierDetails::getCourierId, courier -> courier));
        return historyResponseDTOS.stream().map(historyResponseDTO -> ResponseDTO.builder()
                .totalCount(historyResponseDTO.getTotalCount())
                .delayedCount(historyResponseDTO.getDelayedCount())
                .reason(historyResponseDTO.getReason())
                .courierDetails(courierMap.get(historyResponseDTO.getCourierId()))
                .build()).collect(Collectors.toList());
    }

    public List<HubResponseDTO> getHubPerformance(HubRequestDTO hubRequestDTO) {
        List<HubResponseDTO> hubResponse = getHubHistory(hubRequestDTO);
//        List<CourierDetails> courierDetails = courierDetailsRepo.findAllByCourierIdIn(hubResponse.stream().map(HistoryResponseDTO::getCourierId)
//                .filter(s -> !ObjectUtils.isEmpty(s)).collect(Collectors.toList()));
        return hubResponse.stream().map(historyResponseDTO -> HubResponseDTO.builder()
                .pastAvg(historyResponseDTO.getPastAvg())
                .todayAvg(historyResponseDTO.getTodayAvg())
                .hubId(historyResponseDTO.getHubId())
                .build()).collect(Collectors.toList());
    }

    public List<HistoryResponseDTO> getShipmentHistory(HistoryRequestDTO historyRequestDTO) {

        Criteria criteria = Criteria.where(status).in(Arrays.asList(historyRequestDTO.getFromStatus(),
                        historyRequestDTO.getToStatus()))
                .and(actualCreateDate).gte(getParsedDate(historyRequestDTO.getFromDate())).lte(getParsedDate(historyRequestDTO.getToDate()));

        if (!isEmpty(historyRequestDTO.getSellerId()) && !"GLOBAL".equalsIgnoreCase(historyRequestDTO.getSellerId())) {
            criteria.and(ShipmentHistory.Fields.sellerId).is(historyRequestDTO.getSellerId());
        }
        if (!isEmpty(historyRequestDTO.getFromPinCode()) && !isEmpty(historyRequestDTO.getToPinCode())) {
            criteria.and(fromPinCode).is(historyRequestDTO.getFromPinCode());
            criteria.and(toPinCode).is(historyRequestDTO.getToPinCode());
        }
        MatchOperation matchAggregation = Aggregation.match(criteria);
        Aggregation countAgg = Aggregation.newAggregation(matchAggregation, Aggregation.group("courierId")
                .addToSet("shipmentId").as("uniqueShipments"), Aggregation.project("courierId")
                .and("uniqueShipments").size().as("totalCount"));
        List<CourierTotalCountDTO> count = mongoTemplate.aggregate(countAgg, ShipmentHistory.class, CourierTotalCountDTO.class).getMappedResults();
        Map<String, Long> courierTotCount = count.stream().collect(Collectors.toMap(CourierTotalCountDTO::get_id, CourierTotalCountDTO::getTotalCount));

//        GroupOperation groupOperation = Aggregation.group("courierId", "shipmentId")
        GroupOperation groupOperation = Aggregation.group("courierId")
                .push("$$ROOT").as("historyList");

        ProjectionOperation projectionOperation = Aggregation.project()
                .andExclude("_id")
                .and("_id").as("courierId")
                .and("_id.sellerId").as("sellerId")
                .and("historyList").as("historyList");


        Aggregation aggregation = Aggregation.newAggregation(matchAggregation, groupOperation, projectionOperation);


        List<HistoryGroupDTO> historyGroupDTOS = mongoTemplate.aggregate(aggregation, ShipmentHistory.class, HistoryGroupDTO.class).getMappedResults();
        return historyGroupDTOS.stream().map(groupDTO -> HistoryResponseDTO.builder()
                .delayedCount(groupDTO.getDelayedCount())
                .totalCount(courierTotCount.get(groupDTO.getCourierId()))
                .courierId(groupDTO.getCourierId())
                .reason(groupDTO.getUniqueHistory().stream()
                        .collect(Collectors.groupingBy(ShipmentHistory::getReasonId, counting())))
                .build()).collect(Collectors.toList());

    }

    public List<HubResponseDTO> getHubHistory(HubRequestDTO hubRequestDTO) {

        // Get hub history for last 7 days
        hubRequestDTO.setFromDate(LocalDateTime.of(LocalDateTime.now().minusDays(7).toLocalDate(), LocalTime.MIDNIGHT));
        hubRequestDTO.setToDate(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIDNIGHT));
        List<HubGroupDTO> historyGroupDTOS = getHistoryGroupDTOS(hubRequestDTO);
        Map<String, Long> total = getTotalExecution(historyGroupDTOS);
//        Long count = historyGroupDTOS.stream().mapToLong(groupDTO -> groupDTO.getHistoryList().size()).sum();
        // Get hub history for today
        hubRequestDTO.setFromDate(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIDNIGHT));
        hubRequestDTO.setToDate(LocalDateTime.now());
        List<HubGroupDTO> historyGroupToday = getHistoryGroupDTOS(hubRequestDTO);
        Map<String, Long> totalToday = getTotalExecution(historyGroupToday);
        Long countToday = historyGroupToday.stream().mapToLong(groupDTO -> groupDTO.getHistoryList().size()).sum();

//        return historyGroupDTOS.stream().map(groupDTO -> HubResponseDTO.builder()
//                .pastAvg(!historyGroupToday.isEmpty() ? (totalToday / count) : 0L)
//                .todayAvg(!historyGroupDTOS.isEmpty() ? (total / countToday) : 0L)
////                .courierId(groupDTO.getCourierId())
//                .build()).collect(Collectors.toList());

        return total.entrySet().stream().map(stringLongEntry -> HubResponseDTO.builder()
                        .pastAvg(stringLongEntry.getValue())
                        .todayAvg(!ObjectUtils.isEmpty(totalToday.get(stringLongEntry.getKey())) ? totalToday.get(stringLongEntry.getKey()) : 0)
                        .hubId(stringLongEntry.getKey())
//                .courierId(groupDTO.getCourierId())
                        .build()
        ).collect(Collectors.toList());


    }

    @NotNull
    private Map<String, Long> getTotalExecution(List<HubGroupDTO> historyGroupDTOS) {

        return historyGroupDTOS.stream().collect(
                Collectors.toMap(HubGroupDTO::getHubId, groupDTO -> !ObjectUtils.isEmpty(groupDTO.getHistoryList()) ? groupDTO.getHistoryList().stream().collect(Collectors.groupingBy(ShipmentHistory::getShipmentId))
                                .entrySet().stream().map(shipmentList -> {
                                    LocalDateTime min = groupDTO.getHistoryList().stream().map(ShipmentHistory::getActualCreateDate)
                                            .filter(o -> !isEmpty(o)).min(LocalDateTime::compareTo).get();
                                    LocalDateTime max = groupDTO.getHistoryList().stream().map(ShipmentHistory::getActualCreateDate)
                                            .filter(o -> !isEmpty(o)).max(LocalDateTime::compareTo).get();
                                    return calculateMinutesDifference(min, max);

                                }).reduce(Long::sum).orElse(0L) / groupDTO.getHistoryList().size() : 0L,
                        Long::sum)
        );
    }

    @NotNull
    private List<HubGroupDTO> getHistoryGroupDTOS(HubRequestDTO hubRequestDTO) {
        Criteria criteria = Criteria.where(actualCreateDate).gte(hubRequestDTO.getFromDate()).lte(hubRequestDTO.getToDate())
                .and(hubId).exists(true);

        if (!isEmpty(hubRequestDTO.getSellerId()) && !"GLOBAL".equalsIgnoreCase(hubRequestDTO.getSellerId())) {
            criteria.and(ShipmentHistory.Fields.sellerId).is(hubRequestDTO.getSellerId());
        }
        if (!isEmpty(hubRequestDTO.getFromPinCode()) && !isEmpty(hubRequestDTO.getToPinCode())) {
            criteria.and(fromPinCode).is(hubRequestDTO.getFromPinCode());
            criteria.and(toPinCode).is(hubRequestDTO.getToPinCode());
        }
        MatchOperation matchAggregation = Aggregation.match(criteria);

        GroupOperation groupOperation = Aggregation.group(hubId, shipmentId)
//        GroupOperation groupOperation = Aggregation.group(hubId)
                .push("$$ROOT").as("historyList");

        ProjectionOperation projectionOperation = Aggregation.project()
                .and("_id.hubId").as(hubId)
//                .and("_id.courierId").as(courierId)
                .and("_id.shipmentId").as(shipmentId)
//                .and("_id").as(hubId)
                .and("historyList").as("historyList");


        Aggregation aggregation = Aggregation.newAggregation(matchAggregation, groupOperation, projectionOperation);


        return mongoTemplate.aggregate(aggregation, ShipmentHistory.class, HubGroupDTO.class).getMappedResults();
    }

    public void createAggregatedDetails(HistoryRequestDTO historyRequestDTO, LocalDateTime localDateTime) {
        historyRequestDTO.setFromDate(Utils.formatLocalDateTime(localDateTime));
        historyRequestDTO.setToDate(Utils.formatLocalDateTime(localDateTime.plusDays(1)));
        AggregatedDetailsId aggregatedDetailsId = AggregatedDetailsId.builder()
                .sellerId(historyRequestDTO.getSellerId())
                .aggregatedDateTime(localDateTime)
                .fromPincode(historyRequestDTO.getFromPinCode())
                .fromPincode(historyRequestDTO.getFromPinCode())
                .build();
        Optional<AggregatedDetails> optional = aggregatedDetailsRepo.findById(aggregatedDetailsId);
        AggregatedDetails aggregatedDetails;
        aggregatedDetails = optional.orElseGet(() -> AggregatedDetails.builder()
                .id(aggregatedDetailsId)
                .build());
        List<HistoryResponseDTO> historyResponseDTOS = getShipmentHistory(historyRequestDTO);
        List<AggregatedCourierData> aggregatedCourierData = historyResponseDTOS.stream().map(historyResponseDTO ->
                AggregatedCourierData.builder()
                        .courierId(historyResponseDTO.getCourierId())
                        .totalShipments(historyResponseDTO.getTotalCount())
                        .delayedShipments(historyResponseDTO.getDelayedCount())
                        .reasons(historyResponseDTO.getReason())
                        .build()).collect(Collectors.toList());
        aggregatedDetails.getFromStatusToStatus()
                .put(historyRequestDTO.getFromStatus() + "To" + historyRequestDTO.getToStatus(), aggregatedCourierData);
        aggregatedDetailsRepo.save(aggregatedDetails);

    }

    public String getShipmentHubStatus(String sellerId, String shipmentId) {
        List<ShipmentHistory> shipmentHistoryList = shipmentHistoryRepo.findAllBySellerIdAndShipmentId(sellerId, shipmentId);
        ShipmentHistory shipmentHistory = shipmentHistoryList.stream().sorted(Comparator.comparing(ShipmentHistory::getActualCreateDate).reversed()).findFirst().get();
        String MSG = "Your shipment is in ";
        return MSG.concat(ObjectUtils.isEmpty(shipmentHistory.getHubId()) ? "Transit" : "hub ".concat(shipmentHistory.getHubId()));
    }


//    public String generateCsvFile(List<String> header, List<List<String>> data, String groupId){
//        try (StringWriter writer = new StringWriter();
//             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL.builder().setHeader(header.toArray(new String[0])).build())) {
//
//            for (List<String> record : data)
//                csvPrinter.printRecord(record);
//
//            return s3Utils.sendPdfToS3(groupId, writer.toString().getBytes(), ACTION_LOGS);
//        } catch (IOException e) {
//            log.error("CSV file generation failed for groupId: {}", groupId);
//            throw  e;
//        }
//    }

    public List<DelayedShipments> getDelayedShipments(String fromDate, String toDate){
        return delayedShipmentsRepo.findAllBySentTimeBetween(Utils.stringToLocalDateTime(fromDate), Utils.stringToLocalDateTime(toDate));
    }

}
