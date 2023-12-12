package com.hackathon.Lops.services;

import com.hackathon.Lops.beans.SchedulerCombinationDto;
import com.hackathon.Lops.beans.SellerDetails;
import com.hackathon.Lops.dto.HistoryRequestDTO;
import com.hackathon.Lops.repository.SchedulerCombinationRepo;
import com.hackathon.Lops.repository.SellersDetailsRepo;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hackathon.Lops.utils.Utils.getParsedDate;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
public class SchedulerService {

    @Autowired
    SchedulerCombinationRepo schedulerCombinationRepo;
    @Autowired
    SellersDetailsRepo sellersDetailsRepo;
    @Autowired
    ShipmentHistoryDetailsService shipmentHistoryDetailsService;


//    @PostConstruct()
//    public void scheduleDefaultCombination() {
//        createAndUpdateCombinations();
//    }

    @Scheduled(cron = "0 0 2 * * *")
    public void scheduleNewSellersCombination() {
        createAndUpdateCombinations();
    }

    private void createAndUpdateCombinations() {
        List<SellerDetails> sellerDetails = sellersDetailsRepo.findAll();
        sellerDetails.add(new SellerDetails("GLOBAL"));
        List<SchedulerCombinationDto> schedulerCombinationDtoList = new ArrayList<>();
        sellerDetails.forEach(s -> {
            if (!schedulerCombinationRepo.existsBySellerIdAndFromStatusAndToStatus(s.getSellerId(), "PICKED", "DELIVERED")) {
                shipmentHistoryDetailsService.createAggregatedDetails(getHistoryRequestDto(s.getSellerId(), "PICKED", "DELIVERED"),
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIDNIGHT));
                schedulerCombinationDtoList.add(getSchedulerCombinationDto(s.getSellerId(), "PICKED", "DELIVERED",
                        LocalDateTime.of(LocalDateTime.now().minus(Duration.ofDays(7)).toLocalDate(),
                                LocalTime.MIDNIGHT),
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIDNIGHT)
                ));
            }
        });
        schedulerCombinationRepo.saveAll(schedulerCombinationDtoList);
    }

    private SchedulerCombinationDto getSchedulerCombinationDto(String sellerId, String fromStatus, String toStatus, LocalDateTime fromDate, LocalDateTime toDate) {
        SchedulerCombinationDto schedulerCombinationDto = new SchedulerCombinationDto();
        schedulerCombinationDto.setFromStatus(fromStatus);
        schedulerCombinationDto.setToStatus(toStatus);
        schedulerCombinationDto.setSellerId(sellerId);
        schedulerCombinationDto.setToDate(toDate.toString());
        schedulerCombinationDto.setFromDate(fromDate.toString());
        return schedulerCombinationDto;
    }

    private HistoryRequestDTO getHistoryRequestDto(String sellerId, String fromStatus, String toStatus) {
        HistoryRequestDTO historyRequestDTO = new HistoryRequestDTO();
        historyRequestDTO.setFromStatus(fromStatus);
        historyRequestDTO.setToStatus(toStatus);
        historyRequestDTO.setSellerId(sellerId);
        return historyRequestDTO;
    }

    public void createNewCombination(SchedulerCombinationDto scdto) {
        if (!schedulerCombinationRepo.existsBySellerIdAndFromStatusAndToStatusAndFromPinCodeAndToPinCodeAndFromDateAndToDate(scdto.getSellerId(),
                scdto.getFromStatus(), scdto.getToStatus(), scdto.getFromPinCode(), scdto.getToPinCode(), scdto.getFromDate(), scdto.getToDate())) {
            while (!getParsedDate(scdto.getFromDate()).toInstant().isAfter(getParsedDate(scdto.getToDate()).toInstant())) {
                shipmentHistoryDetailsService.createAggregatedDetails(getHistoryRequestDto(scdto.getSellerId(),
                        scdto.getFromStatus(), scdto.getToStatus()), getParsedDate(scdto.getFromDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                scdto.setFromDate(addOneDayAndFormat(scdto.getFromDate()));
            }
            schedulerCombinationRepo.save(scdto);
        }
    }


    @Scheduled(initialDelay = 1200000, fixedRate = 1800000)
    public void scheduledAggregation() {
        List<SchedulerCombinationDto> scdto = schedulerCombinationRepo.findAll();
        scdto.forEach(s -> {
            if (s.getFromDate() != null && s.getToDate() != null) {
                while (LocalDateTime.parse(s.getToDate()).isAfter(LocalDateTime.parse(s.getFromDate()))) {
                    log.info("started aggregation for seller : {}", s.getSellerId());
                    shipmentHistoryDetailsService.createAggregatedDetails(getHistoryRequestDto(s.getSellerId(),
                            s.getFromStatus(), s.getToStatus()), LocalDateTime.parse(s.getFromDate()));
                    s.setToDate(String.valueOf(getParsedDate(s.getFromDate()).toInstant().plus(1, DAYS)));
                }
            }
        });
    }

    @SneakyThrows
    public static String addOneDayAndFormat(String dateStr) {
        Date originalDate = getParsedDate(dateStr);
        LocalDate localDate = originalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate newDate = localDate.plusDays(1);
        return newDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

}
