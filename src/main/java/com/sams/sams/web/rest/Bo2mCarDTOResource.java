package com.sams.sams.web.rest;

import com.sams.sams.domain.Bo2mCarDTO;
import com.sams.sams.service.Bo2mCarDTOService;
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
 * REST controller for managing {@link com.sams.sams.domain.Bo2mCarDTO}.
 */
@RestController
@RequestMapping("/api")
public class Bo2mCarDTOResource {

    private final Logger log = LoggerFactory.getLogger(Bo2mCarDTOResource.class);

    private static final String ENTITY_NAME = "samsBo2MCarDto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Bo2mCarDTOService bo2mCarDTOService;

    public Bo2mCarDTOResource(Bo2mCarDTOService bo2mCarDTOService) {
        this.bo2mCarDTOService = bo2mCarDTOService;
    }

    /**
     * {@code POST  /bo-2-m-car-dtos} : Create a new bo2mCarDTO.
     *
     * @param bo2mCarDTO the bo2mCarDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bo2mCarDTO, or with status {@code 400 (Bad Request)} if the bo2mCarDTO has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bo-2-m-car-dtos")
    public ResponseEntity<Bo2mCarDTO> createBo2mCarDTO(@Valid @RequestBody Bo2mCarDTO bo2mCarDTO) throws URISyntaxException {
        log.debug("REST request to save Bo2mCarDTO : {}", bo2mCarDTO);
        if (bo2mCarDTO.getId() != null) {
            throw new BadRequestAlertException("A new bo2mCarDTO cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bo2mCarDTO result = bo2mCarDTOService.save(bo2mCarDTO);
        return ResponseEntity.created(new URI("/api/bo-2-m-car-dtos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bo-2-m-car-dtos} : Updates an existing bo2mCarDTO.
     *
     * @param bo2mCarDTO the bo2mCarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bo2mCarDTO,
     * or with status {@code 400 (Bad Request)} if the bo2mCarDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bo2mCarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bo-2-m-car-dtos")
    public ResponseEntity<Bo2mCarDTO> updateBo2mCarDTO(@Valid @RequestBody Bo2mCarDTO bo2mCarDTO) throws URISyntaxException {
        log.debug("REST request to update Bo2mCarDTO : {}", bo2mCarDTO);
        if (bo2mCarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Bo2mCarDTO result = bo2mCarDTOService.save(bo2mCarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bo2mCarDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bo-2-m-car-dtos} : get all the bo2mCarDTOS.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bo2mCarDTOS in body.
     */
    @GetMapping("/bo-2-m-car-dtos")
    public ResponseEntity<List<Bo2mCarDTO>> getAllBo2mCarDTOS(Pageable pageable) {
        log.debug("REST request to get a page of Bo2mCarDTOS");
        Page<Bo2mCarDTO> page = bo2mCarDTOService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bo-2-m-car-dtos/:id} : get the "id" bo2mCarDTO.
     *
     * @param id the id of the bo2mCarDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bo2mCarDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bo-2-m-car-dtos/{id}")
    public ResponseEntity<Bo2mCarDTO> getBo2mCarDTO(@PathVariable Long id) {
        log.debug("REST request to get Bo2mCarDTO : {}", id);
        Optional<Bo2mCarDTO> bo2mCarDTO = bo2mCarDTOService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bo2mCarDTO);
    }

    /**
     * {@code DELETE  /bo-2-m-car-dtos/:id} : delete the "id" bo2mCarDTO.
     *
     * @param id the id of the bo2mCarDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bo-2-m-car-dtos/{id}")
    public ResponseEntity<Void> deleteBo2mCarDTO(@PathVariable Long id) {
        log.debug("REST request to delete Bo2mCarDTO : {}", id);
        bo2mCarDTOService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
