package org.avd.kamin.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.avd.kamin.repository.StatusEvidencijeRepository;
import org.avd.kamin.service.StatusEvidencijeQueryService;
import org.avd.kamin.service.StatusEvidencijeService;
import org.avd.kamin.service.criteria.StatusEvidencijeCriteria;
import org.avd.kamin.service.dto.StatusEvidencijeDTO;
import org.avd.kamin.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.avd.kamin.domain.StatusEvidencije}.
 */
@RestController
@RequestMapping("/api/status-evidencijes")
public class StatusEvidencijeResource {

    private static final Logger log = LoggerFactory.getLogger(StatusEvidencijeResource.class);

    private static final String ENTITY_NAME = "statusEvidencije";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatusEvidencijeService statusEvidencijeService;

    private final StatusEvidencijeRepository statusEvidencijeRepository;

    private final StatusEvidencijeQueryService statusEvidencijeQueryService;

    public StatusEvidencijeResource(
        StatusEvidencijeService statusEvidencijeService,
        StatusEvidencijeRepository statusEvidencijeRepository,
        StatusEvidencijeQueryService statusEvidencijeQueryService
    ) {
        this.statusEvidencijeService = statusEvidencijeService;
        this.statusEvidencijeRepository = statusEvidencijeRepository;
        this.statusEvidencijeQueryService = statusEvidencijeQueryService;
    }

    /**
     * {@code POST  /status-evidencijes} : Create a new statusEvidencije.
     *
     * @param statusEvidencijeDTO the statusEvidencijeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statusEvidencijeDTO, or with status {@code 400 (Bad Request)} if the statusEvidencije has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StatusEvidencijeDTO> createStatusEvidencije(@Valid @RequestBody StatusEvidencijeDTO statusEvidencijeDTO)
        throws URISyntaxException {
        log.debug("REST request to save StatusEvidencije : {}", statusEvidencijeDTO);
        if (statusEvidencijeDTO.getId() != null) {
            throw new BadRequestAlertException("A new statusEvidencije cannot already have an ID", ENTITY_NAME, "idexists");
        }
        statusEvidencijeDTO = statusEvidencijeService.save(statusEvidencijeDTO);
        return ResponseEntity.created(new URI("/api/status-evidencijes/" + statusEvidencijeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, statusEvidencijeDTO.getId().toString()))
            .body(statusEvidencijeDTO);
    }

    /**
     * {@code PUT  /status-evidencijes/:id} : Updates an existing statusEvidencije.
     *
     * @param id the id of the statusEvidencijeDTO to save.
     * @param statusEvidencijeDTO the statusEvidencijeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusEvidencijeDTO,
     * or with status {@code 400 (Bad Request)} if the statusEvidencijeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statusEvidencijeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StatusEvidencijeDTO> updateStatusEvidencije(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StatusEvidencijeDTO statusEvidencijeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StatusEvidencije : {}, {}", id, statusEvidencijeDTO);
        if (statusEvidencijeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statusEvidencijeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statusEvidencijeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        statusEvidencijeDTO = statusEvidencijeService.update(statusEvidencijeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statusEvidencijeDTO.getId().toString()))
            .body(statusEvidencijeDTO);
    }

    /**
     * {@code PATCH  /status-evidencijes/:id} : Partial updates given fields of an existing statusEvidencije, field will ignore if it is null
     *
     * @param id the id of the statusEvidencijeDTO to save.
     * @param statusEvidencijeDTO the statusEvidencijeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusEvidencijeDTO,
     * or with status {@code 400 (Bad Request)} if the statusEvidencijeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the statusEvidencijeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the statusEvidencijeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StatusEvidencijeDTO> partialUpdateStatusEvidencije(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StatusEvidencijeDTO statusEvidencijeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StatusEvidencije partially : {}, {}", id, statusEvidencijeDTO);
        if (statusEvidencijeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statusEvidencijeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statusEvidencijeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StatusEvidencijeDTO> result = statusEvidencijeService.partialUpdate(statusEvidencijeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statusEvidencijeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /status-evidencijes} : get all the statusEvidencijes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statusEvidencijes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StatusEvidencijeDTO>> getAllStatusEvidencijes(StatusEvidencijeCriteria criteria) {
        log.debug("REST request to get StatusEvidencijes by criteria: {}", criteria);

        List<StatusEvidencijeDTO> entityList = statusEvidencijeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /status-evidencijes/count} : count all the statusEvidencijes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countStatusEvidencijes(StatusEvidencijeCriteria criteria) {
        log.debug("REST request to count StatusEvidencijes by criteria: {}", criteria);
        return ResponseEntity.ok().body(statusEvidencijeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /status-evidencijes/:id} : get the "id" statusEvidencije.
     *
     * @param id the id of the statusEvidencijeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statusEvidencijeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatusEvidencijeDTO> getStatusEvidencije(@PathVariable("id") Long id) {
        log.debug("REST request to get StatusEvidencije : {}", id);
        Optional<StatusEvidencijeDTO> statusEvidencijeDTO = statusEvidencijeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statusEvidencijeDTO);
    }

    /**
     * {@code DELETE  /status-evidencijes/:id} : delete the "id" statusEvidencije.
     *
     * @param id the id of the statusEvidencijeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatusEvidencije(@PathVariable("id") Long id) {
        log.debug("REST request to delete StatusEvidencije : {}", id);
        statusEvidencijeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
