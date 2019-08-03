package com.sams.sams.service;

import com.sams.sams.domain.Bo2mCarDTO;
import com.sams.sams.repository.Bo2mCarDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Bo2mCarDTO}.
 */
@Service
@Transactional
public class Bo2mCarDTOService {

    private final Logger log = LoggerFactory.getLogger(Bo2mCarDTOService.class);

    private final Bo2mCarDTORepository bo2mCarDTORepository;

    public Bo2mCarDTOService(Bo2mCarDTORepository bo2mCarDTORepository) {
        this.bo2mCarDTORepository = bo2mCarDTORepository;
    }

    /**
     * Save a bo2mCarDTO.
     *
     * @param bo2mCarDTO the entity to save.
     * @return the persisted entity.
     */
    public Bo2mCarDTO save(Bo2mCarDTO bo2mCarDTO) {
        log.debug("Request to save Bo2mCarDTO : {}", bo2mCarDTO);
        return bo2mCarDTORepository.save(bo2mCarDTO);
    }

    /**
     * Get all the bo2mCarDTOS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Bo2mCarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bo2mCarDTOS");
        return bo2mCarDTORepository.findAll(pageable);
    }


    /**
     * Get one bo2mCarDTO by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Bo2mCarDTO> findOne(Long id) {
        log.debug("Request to get Bo2mCarDTO : {}", id);
        return bo2mCarDTORepository.findById(id);
    }

    /**
     * Delete the bo2mCarDTO by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bo2mCarDTO : {}", id);
        bo2mCarDTORepository.deleteById(id);
    }
}
