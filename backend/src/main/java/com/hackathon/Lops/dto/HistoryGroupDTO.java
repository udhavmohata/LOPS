package com.hackathon.Lops.dto;

import com.hackathon.Lops.beans.ShipmentHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistoryGroupDTO {
    String shipmentId;
    String courierId;
    List<ShipmentHistory> historyList;

    public Long getDelayedCount(){
        if(ObjectUtils.isEmpty(historyList))
            return 0L;
        return (long) historyList.stream().map(ShipmentHistory::getShipmentId).collect(Collectors.toSet()).size();
    }

    public List<ShipmentHistory> getUniqueHistory(){
        if(ObjectUtils.isEmpty(historyList))
            return Collections.EMPTY_LIST;
        return historyList.stream()
                .sorted(Comparator.comparing(ShipmentHistory::getActualCreateDate).reversed())

                        .collect(Collectors.groupingBy(ShipmentHistory::getShipmentId))
                .entrySet().stream().filter(stringListEntry -> !CollectionUtils.isEmpty(stringListEntry.getValue()))
                .map(stringListEntry -> stringListEntry.getValue().iterator().next()).collect(Collectors.toList());
    }

}
