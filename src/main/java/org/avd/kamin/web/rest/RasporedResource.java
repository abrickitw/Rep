package org.avd.kamin.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.avd.kamin.repository.RasporedRepository;
import org.avd.kamin.service.RasporedQueryService;
import org.avd.kamin.service.RasporedService;
import org.avd.kamin.service.criteria.RasporedCriteria;
import org.avd.kamin.service.dto.RasporedDTO;
import org.avd.kamin.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.avd.kamin.domain.Raspored}.
 */
@RestController
@RequestMapping("/api/rasporeds")
public class RasporedResource {

    private static final Logger log = LoggerFactory.getLogger(RasporedResource.class);

    private static final String ENTITY_NAME = "raspored";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RasporedService rasporedService;

    private final RasporedRepository rasporedRepository;

    private final RasporedQueryService rasporedQueryService;

    public RasporedResource(
        RasporedService rasporedService,
        RasporedRepository rasporedRepository,
        RasporedQueryService rasporedQueryService
    ) {
        this.rasporedService = rasporedService;
        this.rasporedRepository = rasporedRepository;
        this.rasporedQueryService = rasporedQueryService;
    }

    /**
     * {@code POST  /rasporeds} : Create a new raspored.
     *
     * @param rasporedDTO the rasporedDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rasporedDTO, or with status {@code 400 (Bad Request)} if the raspored has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RasporedDTO> createRaspored(@Valid @RequestBody RasporedDTO rasporedDTO) throws URISyntaxException {
        log.debug("REST request to save Raspored : {}", rasporedDTO);
        if (rasporedDTO.getId() != null) {
            throw new BadRequestAlertException("A new raspored cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rasporedDTO = rasporedService.save(rasporedDTO);
        return ResponseEntity.created(new URI("/api/rasporeds/" + rasporedDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rasporedDTO.getId().toString()))
            .body(rasporedDTO);
    }

    /**
     * {@code PUT  /rasporeds/:id} : Updates an existing raspored.
     *
     * @param id the id of the rasporedDTO to save.
     * @param rasporedDTO the rasporedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rasporedDTO,
     * or with status {@code 400 (Bad Request)} if the rasporedDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rasporedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RasporedDTO> updateRaspored(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RasporedDTO rasporedDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Raspored : {}, {}", id, rasporedDTO);
        if (rasporedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rasporedDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rasporedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rasporedDTO = rasporedService.update(rasporedDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rasporedDTO.getId().toString()))
            .body(rasporedDTO);
    }

    /**
     * {@code PATCH  /rasporeds/:id} : Partial updates given fields of an existing raspored, field will ignore if it is null
     *
     * @param id the id of the rasporedDTO to save.
     * @param rasporedDTO the rasporedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rasporedDTO,
     * or with status {@code 400 (Bad Request)} if the rasporedDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rasporedDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rasporedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RasporedDTO> partialUpdateRaspored(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RasporedDTO rasporedDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Raspored partially : {}, {}", id, rasporedDTO);
        if (rasporedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rasporedDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rasporedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RasporedDTO> result = rasporedService.partialUpdate(rasporedDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rasporedDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rasporeds} : get all the rasporeds.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rasporeds in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RasporedDTO>> getAllRasporeds(
        RasporedCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Rasporeds by criteria: {}", criteria);

        Page<RasporedDTO> page = rasporedQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rasporeds/count} : count all the rasporeds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRasporeds(RasporedCriteria criteria) {
        log.debug("REST request to count Rasporeds by criteria: {}", criteria);
        return ResponseEntity.ok().body(rasporedQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rasporeds/:id} : get the "id" raspored.
     *
     * @param id the id of the rasporedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rasporedDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RasporedDTO> getRaspored(@PathVariable("id") Long id) {
        log.debug("REST request to get Raspored : {}", id);
        Optional<RasporedDTO> rasporedDTO = rasporedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rasporedDTO);
    }

    /**
     * {@code DELETE  /rasporeds/:id} : delete the "id" raspored.
     *
     * @param id the id of the rasporedDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRaspored(@PathVariable("id") Long id) {
        log.debug("REST request to delete Raspored : {}", id);
        rasporedService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
