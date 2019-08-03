package com.sams.sams.web.rest;

import com.sams.sams.domain.Bo2mCar;
import com.sams.sams.service.Bo2mCarService;
import com.sams.sams.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sams.sams.domain.Bo2mCar}.
 */
@RestController
@RequestMapping("/api")
public class Bo2mCarResource {

    private final Logger log = LoggerFactory.getLogger(Bo2mCarResource.class);

    private static final String ENTITY_NAME = "samsBo2MCar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Bo2mCarService bo2mCarService;

    public Bo2mCarResource(Bo2mCarService bo2mCarService) {
        this.bo2mCarService = bo2mCarService;
    }

    /**
     * {@code POST  /bo-2-m-cars} : Create a new bo2mCar.
     *
     * @param bo2mCar the bo2mCar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bo2mCar, or with status {@code 400 (Bad Request)} if the bo2mCar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bo-2-m-cars")
    public ResponseEntity<Bo2mCar> createBo2mCar(@Valid @RequestBody Bo2mCar bo2mCar) throws URISyntaxException {
        log.debug("REST request to save Bo2mCar : {}", bo2mCar);
        if (bo2mCar.getId() != null) {
            throw new BadRequestAlertException("A new bo2mCar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bo2mCar result = bo2mCarService.save(bo2mCar);
        return ResponseEntity.created(new URI("/api/bo-2-m-cars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bo-2-m-cars} : Updates an existing bo2mCar.
     *
     * @param bo2mCar the bo2mCar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bo2mCar,
     * or with status {@code 400 (Bad Request)} if the bo2mCar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bo2mCar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bo-2-m-cars")
    public ResponseEntity<Bo2mCar> updateBo2mCar(@Valid @RequestBody Bo2mCar bo2mCar) throws URISyntaxException {
        log.debug("REST request to update Bo2mCar : {}", bo2mCar);
        if (bo2mCar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Bo2mCar result = bo2mCarService.save(bo2mCar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bo2mCar.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bo-2-m-cars} : get all the bo2mCars.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bo2mCars in body.
     */
    @GetMapping("/bo-2-m-cars")
    public ResponseEntity<List<Bo2mCar>> getAllBo2mCars(Pageable pageable) {
        log.debug("REST request to get a page of Bo2mCars");
        Page<Bo2mCar> page = bo2mCarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bo-2-m-cars/:id} : get the "id" bo2mCar.
     *
     * @param id the id of the bo2mCar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bo2mCar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bo-2-m-cars/{id}")
    public ResponseEntity<Bo2mCar> getBo2mCar(@PathVariable Long id) {
        log.debug("REST request to get Bo2mCar : {}", id);
        Optional<Bo2mCar> bo2mCar = bo2mCarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bo2mCar);
    }

    /**
     * {@code DELETE  /bo-2-m-cars/:id} : delete the "id" bo2mCar.
     *
     * @param id the id of the bo2mCar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bo-2-m-cars/{id}")
    public ResponseEntity<Void> deleteBo2mCar(@PathVariable Long id) {
        log.debug("REST request to delete Bo2mCar : {}", id);
        bo2mCarService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
