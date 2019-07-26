package com.sams.sams.repository.search;

import com.sams.sams.domain.Bo2mOwner;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Bo2mOwner} entity.
 */
public interface Bo2mOwnerSearchRepository extends ElasticsearchRepository<Bo2mOwner, Long> {
}
