package com.sams.sams.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link Bo2mOwnerDTOSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class Bo2mOwnerDTOSearchRepositoryMockConfiguration {

    @MockBean
    private Bo2mOwnerDTOSearchRepository mockBo2mOwnerDTOSearchRepository;

}
