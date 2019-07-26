package com.sams.sams.service;

import com.sams.sams.domain.Bo2mCar;
import com.sams.sams.repository.Bo2mCarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Bo2mCar}.
 */
@Service
@Transactional
public class Bo2mCarService {

    private final Logger log = LoggerFactory.getLogger(Bo2mCarService.class);

    private final Bo2mCarRepository bo2mCarRepository;

    public Bo2mCarService(Bo2mCarRepository bo2mCarRepository) {
        this.bo2mCarRepository = bo2mCarRepository;
    }

    /**
     * Save a bo2mCar.
     *
     * @param bo2mCar the entity to save.
     * @return the persisted entity.
     */
    public Bo2mCar save(Bo2mCar bo2mCar) {
        log.debug("Request to save Bo2mCar : {}", bo2mCar);
        return bo2mCarRepository.save(bo2mCar);
    }

    /**
     * Get all the bo2mCars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Bo2mCar> findAll(Pageable pageable) {
        log.debug("Request to get all Bo2mCars");
        return bo2mCarRepository.findAll(pageable);
    }


    /**
     * Get one bo2mCar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Bo2mCar> findOne(Long id) {
        log.debug("Request to get Bo2mCar : {}", id);
        return bo2mCarRepository.findById(id);
    }

    /**
     * Delete the bo2mCar by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bo2mCar : {}", id);
        bo2mCarRepository.deleteById(id);
    }
}
