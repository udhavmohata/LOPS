package com.hackathon.Lops.repository;

import com.hackathon.Lops.beans.AggregatedDetails;
import com.hackathon.Lops.beans.AggregatedDetailsId;
import com.hackathon.Lops.dto.HistoryRequestDTO;
import com.hackathon.Lops.models.TrendRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hackathon.Lops.utils.Utils.getParsedDate;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AggregatedDetailsCustomRepo {

    private final MongoTemplate mongoTemplate;
    private static final String PARENT_ID = "_id";
    private static final String DOT = ".";

    public List<AggregatedDetails> findBySellerTenantFromToStatusDate(HistoryRequestDTO trendRequestDTO) {
        Query query = getQuery(trendRequestDTO);
        return mongoTemplate.find(query, AggregatedDetails.class);
    }

    public Query getQuery(HistoryRequestDTO trendRequestDTO) {

        Criteria criteria = new Criteria();
        criteria.and(PARENT_ID+DOT+AggregatedDetailsId.Fields.sellerId).is(trendRequestDTO.getSellerId());
        criteria.and(PARENT_ID+DOT+AggregatedDetailsId.Fields.aggregatedDateTime).gte(getParsedDate(trendRequestDTO.getFromDate()))
                .lte(getParsedDate(trendRequestDTO.getToDate()));

        if (trendRequestDTO.getFromPinCode() != null) {
            criteria.and(PARENT_ID+DOT+AggregatedDetailsId.Fields.fromPincode).is(trendRequestDTO.getFromPinCode());
        }
        if (trendRequestDTO.getToPinCode() != null) {
            criteria.and(PARENT_ID+DOT+AggregatedDetailsId.Fields.toPincode).is(trendRequestDTO.getToPinCode());
        }
        return new Query(criteria);
    }

}
