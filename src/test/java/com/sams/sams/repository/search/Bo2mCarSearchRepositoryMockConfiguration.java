package com.sams.sams.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link Bo2mCarSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class Bo2mCarSearchRepositoryMockConfiguration {

    @MockBean
    private Bo2mCarSearchRepository mockBo2mCarSearchRepository;

}
