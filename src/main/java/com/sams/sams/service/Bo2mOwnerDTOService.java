package com.sams.sams.service;

import com.sams.sams.domain.Bo2mOwnerDTO;
import com.sams.sams.repository.Bo2mOwnerDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Bo2mOwnerDTO}.
 */
@Service
@Transactional
public class Bo2mOwnerDTOService {

    private final Logger log = LoggerFactory.getLogger(Bo2mOwnerDTOService.class);

    private final Bo2mOwnerDTORepository bo2mOwnerDTORepository;

    public Bo2mOwnerDTOService(Bo2mOwnerDTORepository bo2mOwnerDTORepository) {
        this.bo2mOwnerDTORepository = bo2mOwnerDTORepository;
    }

    /**
     * Save a bo2mOwnerDTO.
     *
     * @param bo2mOwnerDTO the entity to save.
     * @return the persisted entity.
     */
    public Bo2mOwnerDTO save(Bo2mOwnerDTO bo2mOwnerDTO) {
        log.debug("Request to save Bo2mOwnerDTO : {}", bo2mOwnerDTO);
        return bo2mOwnerDTORepository.save(bo2mOwnerDTO);
    }

    /**
     * Get all the bo2mOwnerDTOS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Bo2mOwnerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bo2mOwnerDTOS");
        return bo2mOwnerDTORepository.findAll(pageable);
    }


    /**
     * Get one bo2mOwnerDTO by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Bo2mOwnerDTO> findOne(Long id) {
        log.debug("Request to get Bo2mOwnerDTO : {}", id);
        return bo2mOwnerDTORepository.findById(id);
    }

    /**
     * Delete the bo2mOwnerDTO by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bo2mOwnerDTO : {}", id);
        bo2mOwnerDTORepository.deleteById(id);
    }
}
