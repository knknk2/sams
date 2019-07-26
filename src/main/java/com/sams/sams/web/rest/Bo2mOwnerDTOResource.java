package com.sams.sams.web.rest;

import com.sams.sams.domain.Bo2mOwnerDTO;
import com.sams.sams.service.Bo2mOwnerDTOService;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sams.sams.domain.Bo2mOwnerDTO}.
 */
@RestController
@RequestMapping("/api")
public class Bo2mOwnerDTOResource {

    private final Logger log = LoggerFactory.getLogger(Bo2mOwnerDTOResource.class);

    private static final String ENTITY_NAME = "samsBo2MOwnerDto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Bo2mOwnerDTOService bo2mOwnerDTOService;

    public Bo2mOwnerDTOResource(Bo2mOwnerDTOService bo2mOwnerDTOService) {
        this.bo2mOwnerDTOService = bo2mOwnerDTOService;
    }

    /**
     * {@code POST  /bo-2-m-owner-dtos} : Create a new bo2mOwnerDTO.
     *
     * @param bo2mOwnerDTO the bo2mOwnerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bo2mOwnerDTO, or with status {@code 400 (Bad Request)} if the bo2mOwnerDTO has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bo-2-m-owner-dtos")
    public ResponseEntity<Bo2mOwnerDTO> createBo2mOwnerDTO(@RequestBody Bo2mOwnerDTO bo2mOwnerDTO) throws URISyntaxException {
        log.debug("REST request to save Bo2mOwnerDTO : {}", bo2mOwnerDTO);
        if (bo2mOwnerDTO.getId() != null) {
            throw new BadRequestAlertException("A new bo2mOwnerDTO cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bo2mOwnerDTO result = bo2mOwnerDTOService.save(bo2mOwnerDTO);
        return ResponseEntity.created(new URI("/api/bo-2-m-owner-dtos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bo-2-m-owner-dtos} : Updates an existing bo2mOwnerDTO.
     *
     * @param bo2mOwnerDTO the bo2mOwnerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bo2mOwnerDTO,
     * or with status {@code 400 (Bad Request)} if the bo2mOwnerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bo2mOwnerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bo-2-m-owner-dtos")
    public ResponseEntity<Bo2mOwnerDTO> updateBo2mOwnerDTO(@RequestBody Bo2mOwnerDTO bo2mOwnerDTO) throws URISyntaxException {
        log.debug("REST request to update Bo2mOwnerDTO : {}", bo2mOwnerDTO);
        if (bo2mOwnerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Bo2mOwnerDTO result = bo2mOwnerDTOService.save(bo2mOwnerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bo2mOwnerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bo-2-m-owner-dtos} : get all the bo2mOwnerDTOS.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bo2mOwnerDTOS in body.
     */
    @GetMapping("/bo-2-m-owner-dtos")
    public ResponseEntity<List<Bo2mOwnerDTO>> getAllBo2mOwnerDTOS(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Bo2mOwnerDTOS");
        Page<Bo2mOwnerDTO> page = bo2mOwnerDTOService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bo-2-m-owner-dtos/:id} : get the "id" bo2mOwnerDTO.
     *
     * @param id the id of the bo2mOwnerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bo2mOwnerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bo-2-m-owner-dtos/{id}")
    public ResponseEntity<Bo2mOwnerDTO> getBo2mOwnerDTO(@PathVariable Long id) {
        log.debug("REST request to get Bo2mOwnerDTO : {}", id);
        Optional<Bo2mOwnerDTO> bo2mOwnerDTO = bo2mOwnerDTOService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bo2mOwnerDTO);
    }

    /**
     * {@code DELETE  /bo-2-m-owner-dtos/:id} : delete the "id" bo2mOwnerDTO.
     *
     * @param id the id of the bo2mOwnerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bo-2-m-owner-dtos/{id}")
    public ResponseEntity<Void> deleteBo2mOwnerDTO(@PathVariable Long id) {
        log.debug("REST request to delete Bo2mOwnerDTO : {}", id);
        bo2mOwnerDTOService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
