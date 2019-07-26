package com.sams.sams.repository.search;

import com.sams.sams.domain.Bo2mOwnerDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Bo2mOwnerDTO} entity.
 */
public interface Bo2mOwnerDTOSearchRepository extends ElasticsearchRepository<Bo2mOwnerDTO, Long> {
}
