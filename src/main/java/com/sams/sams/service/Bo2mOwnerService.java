package com.sams.sams.service;

import com.sams.sams.domain.Bo2mOwner;
import com.sams.sams.repository.Bo2mOwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Bo2mOwner}.
 */
@Service
@Transactional
public class Bo2mOwnerService {

    private final Logger log = LoggerFactory.getLogger(Bo2mOwnerService.class);

    private final Bo2mOwnerRepository bo2mOwnerRepository;

    public Bo2mOwnerService(Bo2mOwnerRepository bo2mOwnerRepository) {
        this.bo2mOwnerRepository = bo2mOwnerRepository;
    }

    /**
     * Save a bo2mOwner.
     *
     * @param bo2mOwner the entity to save.
     * @return the persisted entity.
     */
    public Bo2mOwner save(Bo2mOwner bo2mOwner) {
        log.debug("Request to save Bo2mOwner : {}", bo2mOwner);
        return bo2mOwnerRepository.save(bo2mOwner);
    }

    /**
     * Get all the bo2mOwners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Bo2mOwner> findAll(Pageable pageable) {
        log.debug("Request to get all Bo2mOwners");
        return bo2mOwnerRepository.findAll(pageable);
    }


    /**
     * Get one bo2mOwner by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Bo2mOwner> findOne(Long id) {
        log.debug("Request to get Bo2mOwner : {}", id);
        return bo2mOwnerRepository.findById(id);
    }

    /**
     * Delete the bo2mOwner by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bo2mOwner : {}", id);
        bo2mOwnerRepository.deleteById(id);
    }
}
